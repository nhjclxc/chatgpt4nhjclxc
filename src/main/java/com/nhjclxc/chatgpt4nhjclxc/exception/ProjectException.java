package com.nhjclxc.chatgpt4nhjclxc.exception;

import com.nhjclxc.chatgpt4nhjclxc.response.ReturnCode;

import java.text.MessageFormat;

/**
 * 统一异常处理
 *
 * @author LuoXianchao
 * @since 2023/5/21 9:56
 */
public class ProjectException extends RuntimeException {
    private final String exCode;
    private final String exInfo;
    private final String innerMessage;
    private final String stacktrace;

    public String getExCode() {
        return this.exCode;
    }

    public String getExInfo() {
        return this.exInfo;
    }

    public String getInnerMessage() {
        return this.innerMessage;
    }

    public String getStacktrace() {
        return this.stacktrace;
    }

    public ProjectException(ReturnCode returnCode, Object... arguments) {
        this(returnCode.getExCode(), arguments != null && arguments.length != 0 ? MessageFormat.format(returnCode.getExInfo(), arguments) : MessageFormat.format(returnCode.getExInfo(), ""), new Exception());
    }

    public ProjectException(String code, String info) {
        this(code, info, new Exception());
    }

    public ProjectException(String code, String info, Exception ex) {
        this.exCode = code;
        this.exInfo = info;
        this.innerMessage = ex.getMessage();
        StringBuilder stc = new StringBuilder();
        StackTraceElement[] var5 = ex.getStackTrace();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            StackTraceElement elem = var5[var7];
            stc.append(elem + "\n");
        }

        this.stacktrace = stc.toString();
        Throwable throwable = new Throwable(info);
        super.initCause(throwable);
    }

    public ProjectException(String code, String info, String message, String stc) {
        this.exCode = code;
        this.exInfo = info;
        this.innerMessage = message;
        this.stacktrace = stc;
        Throwable throwable = new Throwable(info);
        super.initCause(throwable);
    }
}
