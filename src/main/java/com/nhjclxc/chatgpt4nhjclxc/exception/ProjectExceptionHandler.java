package com.nhjclxc.chatgpt4nhjclxc.exception;

import com.nhjclxc.chatgpt4nhjclxc.response.JsonResult;
import com.nhjclxc.chatgpt4nhjclxc.response.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 *
 * @author LuoXianchao
 * @since 2023/5/21 9:56
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.nhjclxc.chatgpt4nhjclxc.web")
public class ProjectExceptionHandler {

    //统一处理所有的Exception异常
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public Object doException(Exception ex) {
        ex.printStackTrace();
        if (ex instanceof ProjectException) {
            ProjectException projectException = (ProjectException) ex;
            JsonResult<Object> jsonResult = new JsonResult<>();
            jsonResult.setCode(projectException.getExCode());
            jsonResult.setMsg(projectException.getExInfo());
            jsonResult.setData(null);
            return jsonResult;
        }
        return new JsonResult<>(ReturnCodeEnum.DEFAULT_SYSTEM_ERROR);
    }
}