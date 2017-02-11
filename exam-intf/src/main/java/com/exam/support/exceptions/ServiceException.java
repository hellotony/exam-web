package com.exam.support.exceptions;

/**
 * Created by huajianbing on 2015/12/8.
 */
public class ServiceException extends RuntimeException {
    private ErrorCode errorCode;

    private Integer code;

    private String msg;

    public ServiceException(ErrorCode errorCode,String errorMsg){
        super(errorCode.getMsg().concat(errorMsg));
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg() + errorMsg;
    }

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public ServiceException(ErrorCode errorCode, Exception e){
        super(e.getMessage(), e);
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg() + "";

    }
    public ServiceException(Integer errorCode, String msg) {
        super(msg);
        this.code = errorCode;
        this.msg = msg;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
