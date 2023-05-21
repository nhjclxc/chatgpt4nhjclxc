package com.nhjclxc.chatgpt4nhjclxc.config;


/**
 * 项目常量配置类
 *
 * @author LuoXianchao
 * @since 2023/5/21 11:50
 */
public class ApplicationConst {

    /**
     * 用户数据redis前缀
     */
    public static final String USER_SESSION_PREFIX= "USER:LOGIN:";

    public static final String CHATGPT_API_KEY_REDIS = "CHATGPT:API:KEY";

    public static final String CHATGPT_RECORD_PREFIX = "CHATGPT:RECORD:";

    public static final String CHATGPT_RECORD_HASH_PREFIX = "CHATGPT:RECORD:HASH:";

    public interface CHATGPT{
        String MODEL = "gpt-3.5-turbo";

        Float TEMPERATURE = 1f;

        String ROLE_USER = "user"; //role为system可以限定AI的角色，role为user是用户发送的内容，role为assistant是AI回答的内容

        String ROLE_ASSISTANT = "assistant";

        String ROLE_SYSTEM = "system";

        /**
         * OpenAI官网接口地址 需VPN
         */
        // String ADDRESS = "https://api.openai.com/v1/chat/completions";

        /**
         * 互联网找的OpenAI代理接口地址 无需VPN
         */
        String ADDRESS = "https://api.openai-proxy.com/v1/chat/completions";
    }
}