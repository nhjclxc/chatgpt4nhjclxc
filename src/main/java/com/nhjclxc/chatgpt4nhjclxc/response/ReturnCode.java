package com.nhjclxc.chatgpt4nhjclxc.response;

/**
 * 统一返回结果集
 *
 * @author LuoXianchao
 * @since 2023/5/21 10:10
 */
public class ReturnCode {
    private final String exCode;
    private final String exInfo;

    public ReturnCode(String exCode, String exInfo) {
        this.exCode = exCode;
        this.exInfo = exInfo;
    }

    public String getExCode() {
        return this.exCode;
    }

    public String getExInfo() {
        return this.exInfo;
    }
}
