package com.exam.support.util;

public class JsonLogUtil {
	public static final String twoTabs = "        ";
	public static final String newLine = "\n";
	public static String formatEx(Exception e){
		
		if(null == e){
			return null;
		}
		String exName = e.toString() + ": " + e.getMessage();
		int maxLine = 20;
		StackTraceElement[] stackTraceElements = e.getStackTrace();
	    int errLength = stackTraceElements.length;
		int printLength = maxLine>errLength?errLength:maxLine;
	    StringBuffer sb = new StringBuffer();
	    sb.append(exName);
	    for(int i = 0; i < printLength; i++){
	    	sb.append(twoTabs).append(stackTraceElements[i]).append(newLine);
	    }
		return sb.toString();
	}
}
