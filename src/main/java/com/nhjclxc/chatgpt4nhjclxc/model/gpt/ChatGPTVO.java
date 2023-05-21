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
public class ChatGPTVO {
/*
{
  "id": "chatcmpl-123",
  "object": "chat.completion",
  "created": 1677652288,
  "choices": [{
    "index": 0,
    "message": {
      "role": "assistant",
      "content": "\n\nHello there, how may I assist you today?",
    },
    "finish_reason": "stop"
  }],
  "usage": {
    "prompt_tokens": 9,
    "completion_tokens": 12,
    "total_tokens": 21
  }
}
*/
    private String id;
    private String object;
    private Integer created;
    private List<ChatGPTChoices> choices;
    private ChatGPTUsage usage;
/*
    请求失败

{
    "error": {
        "message": "",
        "type": "invalid_request_error",
        "param": null,
        "code": "invalid_api_key"
    }
}
 */
    private ChatGPTError error;


}