package com.fkj.addrlist.util;

public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(1, "请求成功"),

    /* 参数错误：1001-1999 */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误：2001-2999*/
    USER_NOT_LOGGED_IN(2001, "用户未登录"),
    USER_LOGIN_ERROR(2002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(2003, "账号已被禁用"),
    USER_NOT_EXIST(2004, "用户不存在"),
    USER_HAS_EXISTED(2005, "用户已存在"),

    /* 业务错误：3001-3999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(3001, "某业务出现问题"),

    /* 系统错误：4001-4999 */
    SYSTEM_INNER_ERROR(4001, "系统繁忙，请稍后重试"),

    /* 数据错误：5001-5999 */
    RESULE_DATA_NONE(5001, "数据未找到"),
    DATA_IS_WRONG(5002, "数据有误"),
    DATA_ALREADY_EXISTED(5003, "数据已存在"),

    /* 接口错误：6001-6999 */
    INTERFACE_INNER_INVOKE_ERROR(6001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(6002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(6003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(6004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(6005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(6006, "接口负载过高"),

    /* 权限错误：7001-7999 */
    PERMISSION_NO_ACCESS(7001, "无访问权限"),

    /* 通信录更新：8001-8999 */
    REMIND_ADDR(8001,"通信录已更新");
    private Integer code;
    private String message;

    ResultCode(int code, String msg){
        this.code = code;
        this.message = msg;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
