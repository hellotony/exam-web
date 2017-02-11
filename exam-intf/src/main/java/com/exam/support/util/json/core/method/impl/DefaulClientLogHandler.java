package com.exam.support.util.json.core.method.impl;

import com.exam.support.util.json.core.method.IclientLogHandler;
import com.exam.support.util.json.core.utils.ClientLogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: liurui
 * Date: 14-5-23
 * Time: 上午10:58
 * 默认的客户端log处理类 用来记录请求的数据以及回传的数据
 */
public class DefaulClientLogHandler implements IclientLogHandler {

    //log
    private static final Logger log = LoggerFactory.getLogger(DefaulClientLogHandler.class);

    //log级别
    private String logLevel = "info";

    @Override
    public void logRequest(ClientLogData clientLogData) {
        logByLogger("request:"+clientLogData.toString());
    }

    @Override
    public void logResponse(ClientLogData clientLogData) {
        logByLogger("response:"+clientLogData.toString());
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    //logger
    private void logByLogger(String data) {
         if (null != data) {
             if (logLevel.equalsIgnoreCase("ALL")) {
                 log.info(data);
             } else if (logLevel.equalsIgnoreCase("TRACE")) {
                 log.trace(data);
             } else if (logLevel.equalsIgnoreCase("DEBUG")) {
                 log.debug(data);
             } else if (logLevel.equalsIgnoreCase("INFO")) {
                 log.info(data);
             } else if (logLevel.equalsIgnoreCase("WARN")) {
                 log.warn(data);
             } else if (logLevel.equalsIgnoreCase("ERROR")) {
                 log.error(data);
             } else if (logLevel.equalsIgnoreCase("OFF")) {
             }
         }
    }
}
