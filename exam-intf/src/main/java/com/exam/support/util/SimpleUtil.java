package com.exam.support.util;

import java.sql.Timestamp;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;


/**
 * Date: 2014-3-25
 * 
 * @author hewen_deng
 * @version 1.0
 */
public class SimpleUtil {

	/** Use ObjectMapper for converting <code>Object</code> to JSON string. */
	private static final ObjectMapper JSON_GENOR = new ObjectMapper();

	public static void fixSuffix(StringBuilder strBuilder, int repLen, String newSuffix) {
		int len = strBuilder.length();
		strBuilder.replace(len - repLen, len, newSuffix);
	}

	public static void delSuffix(StringBuilder strBuilder, int sfxLen) {
		int len = strBuilder.length();
		strBuilder.delete(len - sfxLen, len);
	}

	/**
	 * Format a float value correspond to the given precision.
	 * 
	 * @param f
	 * @param prec Use Integer as declearation type, so can be null.
	 * @return
	 */
	public static String formatFloat(float f, int prec) {
		return String.format("%." + prec + "f", f);
	}

	/**
	 * Format SQL Data.
	 * 
	 * @param date
	 * @return
	 */
	public static String formatSqlDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		return new Timestamp(date.getTime()).toString();
	}

	/**
	 * A simple filter to escape special characters for building sql.
	 * Now only '[Single quotation mark].
	 * 
	 * <p>TODO: Lower performance??? 
	 *  
	 * @param value
	 * @return
	 */
	public static String formatSqlSFiled(String value) {
		if (value == null) {
			return null;
		}
		value = value.replace("'", "''");
		return value;
	}

	/**
	 * Delegate of <code>ObjectMapper.writeValueAsString(Object)</code>
	 * method to generate JSON string.
	 * 
	 * @see ObjectMapper#writeValueAsString(Object object)
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		try {
			return JSON_GENOR.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}