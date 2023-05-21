package com.nhjclxc.chatgpt4nhjclxc.serviec;

import com.alibaba.fastjson.JSONObject;
import com.nhjclxc.chatgpt4nhjclxc.config.ApplicationConst;
import com.nhjclxc.chatgpt4nhjclxc.config.ContextHolder;
import com.nhjclxc.chatgpt4nhjclxc.model.TbUser;
import com.nhjclxc.chatgpt4nhjclxc.utils.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LuoXianchao
 * @since 2023/5/21 14:02
 */
@Slf4j
@Service
@AllArgsConstructor
public class ChatService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 测试系统环境方法
     */
    public Object test(TbUser tbUser, String param, HttpServletRequest request, HttpServletResponse response){
        System.out.println(tbUser);

        String data = "参数：" + param;
        System.out.println(data);

        // 获取token
        String token = request.getHeader("token");
        System.out.println("token：" + token);

        // 测试redis
        stringRedisTemplate.opsForValue().set("chatgpt4nhjclxc", data);
        System.out.println("redis数据：" + stringRedisTemplate.opsForValue().get("chatgpt4nhjclxc"));
        Map<String, Object> info = new HashMap<>();
        info.put("id", tbUser.getId());
        String token1 = JWTUtils.getToken(info);
        System.out.println(token1);
        response.setHeader("token", token1);
        stringRedisTemplate.opsForValue().set(ApplicationConst.USER_SESSION_PREFIX + tbUser.getId(), JSONObject.toJSONString(tbUser));
        System.out.println("redis数据：" + stringRedisTemplate.opsForValue().get(ApplicationConst.USER_SESSION_PREFIX + tbUser.getId()));


        TbUser userId = ContextHolder.getUser();
        System.out.println(userId);

        // 模拟异常
        // System.out.println(1/0);
        // throw new ProjectException("666", "项目业务异常");

        return userId;
    }
}
