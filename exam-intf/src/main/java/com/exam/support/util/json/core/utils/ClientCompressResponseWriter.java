package com.exam.support.util.json.core.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liurui on 14-6-23.
 * 根据http的请求头字段：TN-REQ-COMPRESS-TYPE ，获取需求的压缩方式类型： T-SNAPPY/T-GZIP;对返回的数据进行压缩并设置返回的http头字段:TN-DATA-COMPRESS-TYPE
 */
public class ClientCompressResponseWriter {

    /**
     * 根据头的设置进行返回数据的编码；如果不使用注入的方式使用压缩特性，可以使用该api在代码里添加压缩
     */
    public static void write(HttpServletRequest request, HttpServletResponse response, String s) throws Exception {
        String requstCompressType = request.getHeader(ClientCompressFactory.HTTP_HEAD_REQUEST_COMPRESSTYPE);
        if (null != requstCompressType ) {
            int type = ClientCompressFactory.getCompressTypeFromHeanderString(requstCompressType);
            IClientCompressUtils iClientCompressUtils = null;
            String strout = null;
            switch (type) {
                case ClientCompressFactory.COMPRESS_SNAPPY:
                    iClientCompressUtils = ClientCompressFactory.getCompressUtils(ClientCompressFactory.COMPRESS_SNAPPY);
                    strout = new String(iClientCompressUtils.compress(s.getBytes("UTF-8")), "UTF-8");
                    response.getWriter().write(strout);
                    break;
                case ClientCompressFactory.COMPRESS_GZIP:
                    iClientCompressUtils = ClientCompressFactory.getCompressUtils(ClientCompressFactory.COMPRESS_GZIP);
                    strout = new String(iClientCompressUtils.compress(s.getBytes("UTF-8")), "UTF-8");
                    response.getWriter().write(strout);
                    break;
                default:
                    response.getWriter().write(s);
                    break;
            }
        } else {
            response.getWriter().write(s);
        }
    }
}
