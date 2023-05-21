package com.nhjclxc.chatgpt4nhjclxc.model.gpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author LuoXianchao
 * @since 2023/5/21 14:49
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTError {
/*
{
    "message": "",
    "type": "invalid_request_error",
    "param": null,
    "code": "invalid_api_key"
}
 */
    private String message;
    private String type;
    private String param;
    private String code;
}
