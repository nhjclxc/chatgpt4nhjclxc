package com.nhjclxc.chatgpt4nhjclxc.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.nhjclxc.chatgpt4nhjclxc.config.ApplicationConst;
import com.nhjclxc.chatgpt4nhjclxc.config.ContextHolder;
import com.nhjclxc.chatgpt4nhjclxc.dao.RedisHandler;
import com.nhjclxc.chatgpt4nhjclxc.exception.ProjectException;
import com.nhjclxc.chatgpt4nhjclxc.model.model.TbUser;
import com.nhjclxc.chatgpt4nhjclxc.response.ReturnCodeEnum;
import com.nhjclxc.chatgpt4nhjclxc.utils.IPUtils;
import com.nhjclxc.chatgpt4nhjclxc.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 请求拦截器
 *
 * @author LuoXianchao
 * @since 2023/5/21 9:46
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final RedisHandler redisHandler;

    public RequestInterceptor(RedisHandler redisHandler) {
        this.redisHandler = redisHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // System.out.println("拦截器生效");
        //获取token
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            // return true;
            throw new ProjectException(ReturnCodeEnum.NOT_REQUIRED_TOKEN);
        }
        //获取token数据
        Claims claimsBody = JWTUtils.verifyToken(token);
        String phone = String.valueOf(claimsBody.get("phone"));

        // 请求限制
        requestLimit(phone, request);

        //判断本次请求和token过期时间是不是在10分钟内
        Date expiration = claimsBody.getExpiration();
        long expirationTimeMillis = expiration.getTime();
        long currentTimeMillis = System.currentTimeMillis();
        //是的话生成新的token
        if (expirationTimeMillis - currentTimeMillis < 10*60*1000){
            System.out.println("生成新的token去吧！！！");
            //给前端返回一个错误码code，让他发起一个刷新token的请求
            //在哪个请求里面生成新的token返回给前端，以达到在前端的localstorage更新token
            Map<String, Object> info = new HashMap<>();
            info.put("phone", claimsBody.get("phone"));
            String newToken = JWTUtils.getToken(info);
            response.setHeader("token", newToken);
        }

        //将当前用户信息放到内存里面
        String sessionValue = redisHandler.getSessionValue(phone);
        if (Objects.isNull(sessionValue)){
            throw new ProjectException(ReturnCodeEnum.NOT_REQUIRED_SESSION_DATA);
        } 
        ContextHolder.setUser(JSONObject.parseObject(sessionValue, TbUser.class));

        //请求往后传递
        return true;
    }

    /**
     * 判断这个用户今日请求是否上限
     * @param phone 手机号
     * @param request 请求
     */
    private void requestLimit(String phone, HttpServletRequest request) {
        // 白名单用户直接通过
        List<String> whiteListValue = redisHandler.getListValue(ApplicationConst.USER_LIST_WHITE);
        if (whiteListValue.contains(phone)){
            return;
        }

        // 其他用户检测当天访问次数
        String ipAddress = IPUtils.getIPAddress(request);
        doIsLimit(phone, ipAddress);
    }

    /**
     * 判断请求是否受限
     * @param phone 手机号
     * @param ipAddress ip地址
     */
    private void doIsLimit(String phone, String ipAddress){

        // 从缓存中获取，当前这个请求访问了几次
        String key = ApplicationConst.REQUEST_LIMIT_PREFIX + phone;// + ":" + ipAddress;
        String stringValue = redisHandler.getStringValue(key);
        // 以下过期时间的作用是第二天自动过期key操作
        long nextDay = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long now = System.currentTimeMillis();
        long expire = nextDay - now;
         if(Objects.isNull(stringValue)){
            // 初始 次数
            redisHandler.setStringValue(key, String.valueOf(1), expire, TimeUnit.MILLISECONDS);
        }else{
             int nowValue = Integer.parseInt(stringValue);
             if(nowValue == ApplicationConst.REQUEST_LIMIT_TIMES){
                throw new ProjectException(ReturnCodeEnum.REQUEST_LIMIT);
             }
             // 次数自增
             redisHandler.setStringValue(key, String.valueOf(nowValue + 1), expire, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 销毁当前请求在内存中的数据
        ContextHolder.removeUser(); // help GC
    }
}
