package com.nhjclxc.chatgpt4nhjclxc.model.gpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author LuoXianchao
 * @since 2023/5/21 14:25
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTParamDTO {
    /*
    {
        "model": "gpt-3.5-turbo",
        "messages": [{"role": "user", "content": "Hello!"}]
     }
     */
    private String model;
    private List<ChatGPTMessage> messages;
    private Float temperature;
}