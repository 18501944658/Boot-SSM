package com.itszt.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.itszt.common.CommonResponse;
import com.itszt.common.StatusCode;
import com.itszt.exception.QueryNeo4jTimeOutException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionControllerHandler {


    private static final Log LOG = LogFactory.getLog(ExceptionControllerHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error(exception.getMessage(), exception);
        }
        return new CommonResponse().setCode(StatusCode.INTERNAL_ERROR.getCode()).setMsg("系统内部错误");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Object handleException(HttpRequestMethodNotSupportedException exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error(exception.getMessage());
        }
        return new CommonResponse().setCode(StatusCode.METHOD_NOT_ALLOWED.getCode()).setMsg("请求方法不正确");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public Object handleException(HttpMediaTypeNotSupportedException exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error(exception.getMessage());
        }
        return new CommonResponse().setCode(StatusCode.NOT_ACCEPTABLE.getCode()).setMsg("请求Content-Type不正确");
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Object illegalArgumentException(IllegalArgumentException exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error(exception.getMessage(), exception);
        }
        return new CommonResponse().setCode(StatusCode.BAD_REQUEST_PARAM.getCode()).setMsg("请求参数错误");
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (LOG.isErrorEnabled()) {
            LOG.error(e.getMessage(), e);
        }
        return new CommonResponse().setCode(StatusCode.BAD_REQUEST_PARAM.getCode()).setMsg("请求参数错误");
    }

    @ExceptionHandler(QueryNeo4jTimeOutException.class)
    @ResponseBody
    public Object handleHttpMessageNotReadableException(QueryNeo4jTimeOutException e) {
        return new CommonResponse().setCode(StatusCode.REQUEST_TIMEOUT.getCode()).setMsg("查询超时");
    }


}
