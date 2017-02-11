package com.exam.support.util;

import com.exam.support.util.string.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {
    public static final String DOT = ".";

    public static boolean isRealEmpty(String s) {
        return s == null || s.trim().length() <= 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String upperFirstWord(String name) {
        return name.substring(0, 1).toUpperCase() + name.replaceFirst("\\w", "");
    }

    public static String format(String template, Map<String, String> params) {
        String text = template;
        for(Map.Entry<String, String> entry:params.entrySet()){
            String key = entry.getKey();
            text = text.replaceAll("\\{" + key + "}", safeRegexReplacement(entry.getValue()));
        }
        return text;
    }

    private static String safeRegexReplacement(String replacement) {
        if (replacement == null) {
            return replacement;
        }

        return replacement.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
    }

    public static String removeHtmlTag(String content) {
        Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>");
        Matcher m = p.matcher(content);
        if (m.find()) {
            content = content.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
            content = removeHtmlTag(content);
        }
        return content;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
}
