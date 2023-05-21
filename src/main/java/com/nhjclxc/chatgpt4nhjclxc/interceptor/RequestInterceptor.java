package com.nhjclxc.chatgpt4nhjclxc.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.nhjclxc.chatgpt4nhjclxc.config.ApplicationConst;
import com.nhjclxc.chatgpt4nhjclxc.config.ContextHolder;
import com.nhjclxc.chatgpt4nhjclxc.exception.ProjectException;
import com.nhjclxc.chatgpt4nhjclxc.model.TbUser;
import com.nhjclxc.chatgpt4nhjclxc.response.ReturnCodeEnum;
import com.nhjclxc.chatgpt4nhjclxc.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求拦截器
 *
 * @author LuoXianchao
 * @since 2023/5/21 9:46
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    // @Qualifier("stringRedisTemplate")
    private final StringRedisTemplate stringRedisTemplate;

    public RequestInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // System.out.println("拦截器生效");
        //获取token
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            // return true;
            throw new ProjectException(ReturnCodeEnum.NOTR_EQUIRE_TOKEN);
        }
        //获取token数据
        Claims claimsBody = JWTUtils.verifyToken(token);

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
            info.put("id", claimsBody.get("id"));
            String newToken = JWTUtils.getToken(info);
            response.setHeader("token", newToken);
        }

        //将当前用户信息放到内存里面
        String redisData = stringRedisTemplate.opsForValue().get(ApplicationConst.USER_SESSION_PREFIX + claimsBody.get("id"));
        if (redisData != null){
            ContextHolder.setUserId(JSONObject.parseObject(redisData, TbUser.class));
        }

        //请求往后传递
        return true;
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
