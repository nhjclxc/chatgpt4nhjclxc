package com.nhjclxc.chatgpt4nhjclxc.web;

import com.nhjclxc.chatgpt4nhjclxc.aop.SpeedAnnotation;
import com.nhjclxc.chatgpt4nhjclxc.model.TbUser;
import com.nhjclxc.chatgpt4nhjclxc.response.JsonResult;
import com.nhjclxc.chatgpt4nhjclxc.response.ReturnCodeEnum;
import com.nhjclxc.chatgpt4nhjclxc.serviec.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LuoXianchao
 * @since 2023/5/21 9:48
 */
@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/test")
    @SpeedAnnotation
    public Object test(@RequestBody TbUser tbUser, String param,
                       HttpServletRequest request, HttpServletResponse response){
        return new JsonResult<>(ReturnCodeEnum.DEFAULT_SUCCESS, chatService.test(tbUser, param, request, response));
    }

}
