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
public class ChatGPTUsage {
/*
{
    "prompt_tokens": 9,
    "completion_tokens": 12,
    "total_tokens": 21
}
 */
    private Integer prompt_tokens;
    private Integer completion_tokens;
    private Integer total_tokens;
}
