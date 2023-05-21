package com.nhjclxc.chatgpt4nhjclxc.model.dto;

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
public class ChatParamDTO {
    // private List<ChatGPTMessage> messages;
    private String content;
    // 用户聊天记录在redis里面的序号Sn（phone + UUID）
    private String recordSn;
}