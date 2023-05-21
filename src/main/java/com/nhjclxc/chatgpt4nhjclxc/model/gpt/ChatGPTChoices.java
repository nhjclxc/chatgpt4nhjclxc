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
public class ChatGPTChoices {
/*
{
    "index": 0,
    "message": {
      "role": "assistant",
      "content": "\n\nHello there, how may I assist you today?",
    },
    "finish_reason": "stop"
}
*/
    private Integer index;
    private ChatGPTMessage message;
    private String finish_reason;
}
