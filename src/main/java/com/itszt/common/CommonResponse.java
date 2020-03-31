package com.itszt.common;

public class CommonResponse {
    private int code;
    private String msg;

    public CommonResponse() {
    }

    public CommonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public CommonResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public CommonResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
