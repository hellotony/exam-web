package com.exam.support.exceptions;

public class FavorConnectionException extends RuntimeException {

	/**
	 * CLLM连接异常
	 */
	private static final long serialVersionUID = 1L;

	public FavorConnectionException(String msg) {
		super(msg);
	}
}