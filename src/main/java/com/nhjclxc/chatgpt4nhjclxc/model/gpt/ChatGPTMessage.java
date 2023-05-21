package com.nhjclxc.chatgpt4nhjclxc.model.gpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author LuoXianchao
 * @since 2023/5/21 14:25
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTMessage {
    /*
    {"role": "system", "content": "Hello!"}
     */
    private String role;
    private String content;
}