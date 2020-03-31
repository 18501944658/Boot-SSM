package com.itszt.common;

public enum StatusCode {
    OK(200, "成功"),
    BAD_REQUEST_PARAM(400, "请求参数错误"),
    UN_AUTH(401, "认证失败!"),
    FORBIDDEN(403, "鉴权拒绝!"),
    NOT_FOUND(404, "资源无法找到!"),
    METHOD_NOT_ALLOWED(405, "请求的方法不支持！"),
    NOT_ACCEPTABLE(406, "请求头不支持！"),
    REQUEST_TIMEOUT(408, "请求访问超时!"),
    PRECONDITION_FAILED(412, "前置条件不满足!"),
    UNSUPPORTED_MEDIA_TYPE(415, "请求实体格式错误!"),
    LOCKED(425, "资源被锁定!"),
    PARAMETER_NOTFOUND(431, "参数未找到数据!"),
    ENT_NOTFOUND(444, "企业数据为空!"),
    PERSON_NOTFOUND(445, "人员标识为空!"),
    INTERNAL_ERROR(500, "服务器内部错误!"),
    EXTERNAL_RESOURCE_ERROR(503, "服务资源调用异常!"),
    DB_DATA_ERROR(700, "数据库数据异常!"),
    ORDERLIMIT_EXHAUSTED(701, "订单限额查询量已耗尽!"),
    CONTRACT_NOT_EXECUTING(702, "客户合同状态不是执行中!"),
    COLUMN_EMPTY(703, "无有效的查询字段!"),
    USER_STATE_INVALID(800, "用户状态无效!"),
    USER_IP_NOT_ALLOWED(801, "用户IP地址不在允许范围内!"),
    USER_DECRYPTED_FAILED(802, "请求签名信息解密失败!"),
    USER_UID_NOT_MATCHED(803, "用户UID和传入的签名不匹配!"),
    USER_TIMESTAMP_EXPIRED(804, "请求时间戳已过期!"),
    EMAIL_SEND_FAILED(901, "请求成功不收费专用!");

    private int code;
    private String msg;

    private StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
