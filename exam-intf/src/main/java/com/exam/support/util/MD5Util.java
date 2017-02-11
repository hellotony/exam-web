package com.exam.support.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ljun on 2016/8/25.
 */
public class MD5Util {

    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);

    public static String Md5Encode(String str) {
        StringBuilder sign = new StringBuilder();

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 转换异常：{}", e);
        }
        byte[] bytes = md.digest(str.getBytes());

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.Md5Encode("root"));
    }
}
