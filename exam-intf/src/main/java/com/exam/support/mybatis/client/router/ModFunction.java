package com.exam.support.mybatis.client.router;

import org.apache.commons.lang3.Validate;

/**
 * 取模计算函数
 * 
 * @author wangchong
 * 
 */
public class ModFunction {

	/** 模数 */
	private int modDenominator;

	public ModFunction(int modDenominator) {
		Validate.notNull(modDenominator);
		this.modDenominator = modDenominator;
	}

	public long apply(Long input) {
		Validate.notNull(input);
		if (this.modDenominator == 1) { // 如果模数为1，不做取模操作
			return 1;
		}
		long result = input % this.modDenominator;
		return result;
	}
}
