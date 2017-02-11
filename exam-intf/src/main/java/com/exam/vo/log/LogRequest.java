package com.exam.vo.log;

/**
 * Created by huajianbing on 2015/12/3.
 */
public class LogRequest {
    private int logSysType=1;
    private Object content;

    public int getLogSysType() {
        return logSysType;
    }

    public void setLogSysType(int logSysType) {
        this.logSysType = logSysType;
    }
    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }


}
