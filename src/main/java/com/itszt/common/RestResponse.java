package com.itszt.common;


import lombok.Data;

import java.io.Serializable;

/*******************************************************************************
 * - Copyright (c)  2018  chinadaas.com
 * - File Name: RestResponse
 * - @author: Zain - Initial implementation
 * - Description:
 *      Rest响应实体
 * - Function List:
 *
 * - History:
 * Date         Author          Modification
 * 2019/4/17     Zain            Create the current class
 *******************************************************************************/
@Data
public class RestResponse<T> implements Serializable {

    private Integer code;
    private String msg;


    private T result;

    public RestResponse() {
    }

    private RestResponse(Integer code, String msg) {
        this(null, code, msg);
    }

    public RestResponse(T result) {
        this(result, StatusCode.OK);
    }

    private RestResponse(T result, Integer code, String msg) {
        this.result = result;
        this.code = code;
        this.msg = msg;
    }

    private RestResponse(T result, StatusCode statusCode) {
        this(result, statusCode.getCode(), statusCode.getMsg());
    }

    private RestResponse(StatusCode statusCode) {
        this(statusCode.getCode(), statusCode.getMsg());
    }

    public static RestResponse ok() {
        return new RestResponse<>(StatusCode.OK);
    }

    public static <T> RestResponse ok(T result) {
        return new RestResponse<>(result);
    }

    public static RestResponse ok(StatusCode statusCode) {
        return new RestResponse<>(statusCode);
    }

    public static <T> RestResponse ok(T result, StatusCode statusCode) {
        return new RestResponse<>(result, statusCode);
    }

    public static RestResponse ok(StatusCode statusCode, String msg) {
        return new RestResponse<>(statusCode.getCode(), msg);
    }

    public static <T> RestResponse ok(T result, StatusCode statusCode, String msg) {
        return new RestResponse<>(statusCode.getCode(), msg);
    }

    public static RestResponse failed() {
        return new RestResponse<>(StatusCode.INTERNAL_ERROR);
    }

    public static RestResponse failed(StatusCode statusCode) {
        return new RestResponse<>(statusCode);
    }

    public static RestResponse failed(StatusCode statusCode, String msg) {
        return new RestResponse<>(statusCode.getCode(), msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
