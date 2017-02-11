package com.exam.support.exceptions;

/**
 * 系统异常
 * @author wangchong
 * 
 */
public class SystemException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMsg(), cause);
        this.errorCode = errorCode;
    }

    /**
     * @return the errorCode
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
