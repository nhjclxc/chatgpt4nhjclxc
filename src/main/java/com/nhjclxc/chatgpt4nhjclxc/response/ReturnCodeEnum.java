package com.nhjclxc.chatgpt4nhjclxc.response;

/**
 * @author LuoXianchao
 * @since 2023/5/21 10:02
 */
public class ReturnCodeEnum {
    /**
     * 系统异常
     */
    public static final ReturnCode DEFAULT_SUCCESS = new ReturnCode("0", "请求成功");

    public static final ReturnCode DEFAULT_SYSTEM_ERROR = new ReturnCode("10000", "系统内部错误，请稍后再试！");

    public static final ReturnCode DEFAULT_SECURITY_CHECK_ERROR = new ReturnCode("10001", "非法请求，{0}");

    public static final ReturnCode NOT_REQUIRED_TOKEN = new ReturnCode("10002", "缺少请求token");
    
    public static final ReturnCode NOT_REQUIRED_SESSION_DATA = new ReturnCode("10003", "没有被要求会话数据");

    public static final ReturnCode USER_BLACK = new ReturnCode("10004", "该账号已被禁止访问，请联系管理员解除！");

    public static final ReturnCode REQUEST_LIMIT = new ReturnCode("10005", "今日请求已上限，明天再来哦！");

    /**
     * 业务异常
     */
    public static final ReturnCode NOTR_EQUIRE_ERROR = new ReturnCode("20000", "未包含必填参数");

    public static final ReturnCode CHATGPT_API_KEY_ERROR = new ReturnCode("20001", "redis中没有chatGPT的api key");

    public static final ReturnCode CHATGPT_ERROR = new ReturnCode("20002", "ChatGPT返回错误");

}
