package com.exam.support.exceptions;

/**
 * Created by huajianbing on 2015/12/2.
 */
public enum ErrorCode {
    SUCCESS(10000,"success"),
    SYSTEM_ERROR(10001, "系统异常"),
    DB_ERROR(10002, "操作数据库异常"),
    PARAM_VALIDATE_ERROR(10003, "参数校验异常"),
    FILE_NAME_ENCODE_ERROR(10004, "文件名编码错误"),

    /**************************************
     * 其他异常
     **********************************************/
    PARAMS_NULL(11001, "参数异常"),
    CONVERT_JSON_ERROR(11002, "转换到Json字符串异常"),

    /**
     * DB
     */
    VISA_NB_DEPOSIT_NO_RECORD(15001, "数据库无此记录"),
    VISA_NO_RECORD(15002, "无对应记录");

    private int code;

    private String msg;

    ErrorCode(int errorCode, String errorMsg) {
        this.code = errorCode;
        this.msg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
