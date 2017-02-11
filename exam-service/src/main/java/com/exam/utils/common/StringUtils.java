package com.exam.utils.common;

public class StringUtils {
    public static boolean StringIsNullOrEnmty(String str) {
        if (str.equals("") || str == null)
            return true;
        else
            return false;
    }
    public static void delLastChar(StringBuffer ... stringBuffers) {
        for (StringBuffer stringBuffer : stringBuffers) {
            if (stringBuffer.length() > 0) {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            }
        }
    }
}
