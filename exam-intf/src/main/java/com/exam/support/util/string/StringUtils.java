package com.exam.support.util.string;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Created by lijun7 on 2016/10/14.
 */
public class StringUtils {

    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    public static final String LEFT_WHITESPACE_REGEX = "^\\s+";
    public static final String RIGHT_WHITESPACE_REGEX = "\\s+$";
    public static final String WHITESPACE_REGEX = "\\s+";
    public static final int INT_UPPER_A = 65;
    public static final int INT_LOWER_A = 97;
    public static final int INT_UPPER_Z = 90;
    public static final int INT_LOWER_Z = 122;

    public StringUtils() {
    }

    public static String trimLeft(String str) {
        return null == str?str:str.replaceAll("^\\s+", "");
    }

    public static String trimRight(String str) {
        return null == str?str:str.trim();
    }

    public static boolean isEmpty(CharSequence cs) {
        return null == cs || 0 == cs.length();
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return null != cs && 0 != cs.length();
    }

    public static boolean isBlank(CharSequence cs) {
        if(isEmpty(cs)) {
            return true;
        } else {
            for(int index = 0; index < cs.length(); ++index) {
                if(!Character.isWhitespace(cs.charAt(index))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static String trim(String str) {
        return trimRight(str);
    }

    public static String trimBoth(String str) {
        return trimLeft(trimRight(str));
    }

    public static final int stringDifference(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int len = len1 < len2?len1:len2;

        for(int i = 0; i < len; ++i) {
            if(s1.charAt(i) != s2.charAt(i)) {
                return i;
            }
        }

        return len;
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        if(null != str1 && null != str2) {
            int length = Math.max(str1.length(), str2.length());
            return str1 instanceof String && str2 instanceof String?((String)str1).regionMatches(true, 0, (String)str2, 0, length):str1.toString().regionMatches(true, 0, str2.toString(), 0, length);
        } else {
            return str1 == str2;
        }
    }

    public static boolean equalsIgnoreCase2(CharSequence str1, CharSequence str2) {
        return null != str1 && null != str2?upperCase(str1).equals(upperCase(str2)):str1 == str2;
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        return cs1 == null?cs2 == null:cs1.equals(cs2);
    }

    public static int indexOf(CharSequence cs, int search) {
        return indexOf(cs, search, 0);
    }

    public static int indexOf(CharSequence cs, int search, int start) {
        return isEmpty(cs)?-1:cs.toString().indexOf(search, start);
    }

    public static int indexOf(CharSequence cs, CharSequence search) {
        return indexOf(cs, search, 0);
    }

    public static int indexOf(CharSequence cs, CharSequence search, int start) {
        return isEmpty(cs)?-1:cs.toString().indexOf(search.toString(), start);
    }

    public static int lastIndexOf(CharSequence cs, int search) {
        return lastIndexOf(cs, search, 0);
    }

    public static int lastIndexOf(CharSequence cs, int search, int start) {
        return isEmpty(cs)?-1:cs.toString().lastIndexOf(search, start);
    }

    public static int lastIndexOf(CharSequence cs, CharSequence search) {
        return lastIndexOf(cs, search, 0);
    }

    public static int lastIndexOf(CharSequence cs, CharSequence search, int start) {
        return isEmpty(cs)?-1:cs.toString().lastIndexOf(search.toString(), start);
    }

    public static boolean contains(CharSequence cs, int search) {
        return isEmpty(cs)?false:cs.toString().indexOf(search) != -1;
    }

    public static boolean constains(CharSequence cs, CharSequence search) {
        return isEmpty(cs)?false:cs.toString().contains(search);
    }

    public static boolean containsIgnoreCase(CharSequence cs, CharSequence search) {
        if(isEmpty(cs)) {
            return false;
        } else if(null == search) {
            throw new NullPointerException();
        } else {
            return upperCase(cs).contains(upperCase(search));
        }
    }

    public String substring(String str, int start) {
        return str.substring(start);
    }

    public String substring(String str, int start, int end) {
        return str.substring(start, end);
    }

    public String left(String str, int length) {
        return null == str?null:(length < 0?"":str.substring(0, length));
    }

    public String right(String str, int length) {
        if(null == str) {
            return null;
        } else if(length < 0) {
            return "";
        } else {
            int len = str.length();
            return length > len?str:str.substring(len - length);
        }
    }

    public String substringBefore(String str, String separator) {
        if(!isEmpty(str) && !isEmpty(separator)) {
            int end = indexOf(str, separator);
            return end == -1?str:str.substring(0, end);
        } else {
            return str;
        }
    }

    public String substringAfter(String str, String separator) {
        if(!isEmpty(str) && !isEmpty(separator)) {
            int start = indexOf(str, separator);
            if(start == -1) {
                return str;
            } else {
                int length = separator.length();
                return str.substring(start + length);
            }
        } else {
            return str;
        }
    }

    public static int length(CharSequence cs) {
        return cs == null?0:cs.length();
    }

    public static String upperCase(String str) {
        return null == str?null:str.toUpperCase();
    }

    public static String lowerCase(String str) {
        return null == str?null:str.toLowerCase();
    }

    public static String upperCase(String str, Locale locale) {
        return null == str?null:str.toUpperCase(locale);
    }

    public static String lowerCase(String str, Locale locale) {
        return null == str?null:str.toUpperCase(locale);
    }

    public String reverse(String str) {
        return isEmpty(str)?str:(new StringBuilder(str)).reverse().toString();
    }

    public boolean startWith(CharSequence cs1, CharSequence cs2) {
        return this.startWith(cs1, cs2, false);
    }

    public boolean startWithIgnoreCase(CharSequence cs1, CharSequence cs2) {
        return this.startWith(cs1, cs2, true);
    }

    public boolean startWith(CharSequence cs1, CharSequence cs2, boolean isIgnoreCase) {
        return cs1 != null && cs2 != null?(cs2.length() > cs1.length()?false:(cs1 instanceof String && cs2 instanceof String?((String)cs1).regionMatches(isIgnoreCase, 0, (String)cs2, 0, cs2.length()):cs1.toString().regionMatches(isIgnoreCase, 0, cs2.toString(), 0, cs2.length()))):cs1 == null && cs2 == null;
    }

    public boolean endWith(CharSequence cs1, CharSequence cs2) {
        return this.endWith(cs1, cs2, false);
    }

    public boolean endWithIgnoreCase(CharSequence cs1, CharSequence cs2) {
        return this.endWith(cs1, cs2, true);
    }

    public boolean endWith(CharSequence cs1, CharSequence cs2, boolean isIgnoreCase) {
        if(cs1 != null && cs2 != null) {
            if(cs2.length() > cs1.length()) {
                return false;
            } else {
                int start = cs1.length() - cs2.length();
                return cs1 instanceof String && cs2 instanceof String?((String)cs1).regionMatches(isIgnoreCase, start, (String)cs2, 0, cs2.length()):cs1.toString().regionMatches(isIgnoreCase, start, cs2.toString(), 0, cs2.length());
            }
        } else {
            return cs1 == null && cs2 == null;
        }
    }

    public static String deleteWhitespace(String str) {
        if(isEmpty(str)) {
            return str;
        } else {
            int sz = str.length();
            char[] chs = new char[sz];
            int count = 0;

            for(int i = 0; i < sz; ++i) {
                if(!Character.isWhitespace(str.charAt(i))) {
                    chs[count++] = str.charAt(i);
                }
            }

            if(count == sz) {
                return str;
            } else {
                return new String(chs, 0, count);
            }
        }
    }

    public String toString(byte[] buf) throws UnsupportedEncodingException {
        return this.toString(buf, (String)null);
    }

    public String toString(byte[] buf, String charset) throws UnsupportedEncodingException {
        return null == charset?new String(buf):new String(buf, charset);
    }

    public String replace(String str, char oldChar, char newChar) {
        return !isEmpty(str) && oldChar != newChar?str.replace(oldChar, newChar):str;
    }

    public static String replace(String str, String oldString, String newString) {
        return !isEmpty(str) && !isEmpty(oldString) && !isEmpty(newString)?str.replace(oldString, newString):str;
    }

    public static String replace(String str, String oldString, String newString, int count) {
        String ret = str;
        if(!isEmpty(str) && !isEmpty(oldString) && !isEmpty(newString)) {
            int appear = appearNumber(str, oldString);
            int min = count > appear?appear:count;
            if(count <= 0) {
                return str;
            } else {
                for(int cycle = 0; cycle < min; ++cycle) {
                    ret = replace(str, oldString, newString);
                }

                return ret;
            }
        } else {
            return str;
        }
    }

    public static int appearNumber(CharSequence str, CharSequence sub) {
        if(!isEmpty(str) && !isEmpty(sub)) {
            int num = 0;

            for(int pos = 0; -1 != (pos = str.toString().indexOf(sub.toString(), pos)); pos += sub.length()) {
                ++num;
            }

            return num;
        } else {
            return 0;
        }
    }

    public static String remove(String str, String remove) {
        return replace(str, remove, "");
    }

    public static String[] split(String str, String regex) {
        if(isEmpty(str)) {
            String[] ret = new String[]{str};
            return ret;
        } else {
            return str.split(regex);
        }
    }

    private static String upperCase(CharSequence str) {
        StringBuffer sb = new StringBuffer();
        if(null == str) {
            return null;
        } else {
            for(int index = 0; index < str.length(); ++index) {
                char temp = str.charAt(index);
                if(97 < temp && 122 > temp) {
                    sb.append((char)(temp + 65 - 97));
                } else {
                    sb.append(temp);
                }
            }

            return sb.toString();
        }
    }

}
