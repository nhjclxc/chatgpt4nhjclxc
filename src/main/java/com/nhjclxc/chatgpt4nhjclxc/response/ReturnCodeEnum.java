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

    public static final ReturnCode NOTR_EQUIRE_TOKEN = new ReturnCode("10002", "缺少请求token");

    /**
     * 业务异常
     */
    public static final ReturnCode NOTR_EQUIRE_ERROR = new ReturnCode("20000", "未包含必填参数");

}
