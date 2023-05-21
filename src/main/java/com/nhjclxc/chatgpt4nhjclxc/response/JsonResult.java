
package com.nhjclxc.chatgpt4nhjclxc.response;

/**
 * 统一返回结果集
 *
 * @author LuoXianchao
 * @since 2023/5/21 10:10
 */
public class JsonResult<T> {
    private String code;
    private String msg;
    private T data;

    public JsonResult() {
    }

    public JsonResult(ReturnCode returnCode) {
        this.code = returnCode.getExCode();
        this.msg = returnCode.getExInfo();
    }

    public JsonResult(ReturnCode returnCode, T data) {
        this(returnCode);
        this.data = data;
    }

    public JsonResult(T data) {
        this(ReturnCodeEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
