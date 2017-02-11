package com.exam.support.exceptions;
/**
 * 数据已经存在异常
 * @author hehuabing
 *
 */
public class FavorDataException extends RuntimeException {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public FavorDataException(String msg)
	{
		super(msg);
	}
}
