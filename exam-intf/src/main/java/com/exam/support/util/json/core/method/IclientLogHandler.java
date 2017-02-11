package com.exam.support.util.json.core.method;

import com.exam.support.util.json.core.utils.ClientLogData;

/**
 * Created with IntelliJ IDEA.
 * User: liurui
 * Date: 14-5-23
 * Time: 上午10:47
 * To change this template use File | Settings | File Templates.
 */
public interface IclientLogHandler {

    public void logRequest(ClientLogData clientLogData);

    public void logResponse(ClientLogData clientLogData);
}
