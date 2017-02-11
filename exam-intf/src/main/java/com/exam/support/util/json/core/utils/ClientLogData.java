package com.exam.support.util.json.core.utils;

/**
 * Created with IntelliJ IDEA.
 * User: liurui
 * Date: 14-5-23
 * Time: 上午11:02
 * 客户端请求
 */
public class ClientLogData {

    //请求url
    private String requestUrl;

    //请求的序列号
    private String requestSequenceId;

    //请求方的系统标识 三字码等 如果对方没有走TSP可能这里为空
    private String requestAgent;

    //请求数据
    private String requestData;

    //返回数据
    private String reponseData;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestSequenceId() {
        return requestSequenceId;
    }

    public void setRequestSequenceId(String requestSequenceId) {
        this.requestSequenceId = requestSequenceId;
    }

    public String getRequestAgent() {
        return requestAgent;
    }

    public void setRequestAgent(String requestAgent) {
        this.requestAgent = requestAgent;
    }

    public String getReponseData() {
        return reponseData;
    }

    public void setReponseData(String reponseData) {
        this.reponseData = reponseData;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    @Override
    public String toString() {
        //设定默认大小为500
        StringBuffer sb = new StringBuffer(500);
        sb.append("{requestUrl=");
        if (null != this.requestUrl) {
            sb.append(this.requestUrl);
        }
        sb.append(", requestSequenceId=");
        if (null != this.requestSequenceId) {
            sb.append(this.requestSequenceId);
        }
        sb.append(", requestAgent=");
        if (null != this.requestAgent) {
            sb.append(this.requestAgent);
        }
        sb.append(", requestData=");
        if (null != this.requestData) {
            sb.append(this.requestData);
        }
        sb.append(", reponseData=");
        if (null != this.reponseData) {
            sb.append(this.reponseData);
        }
        sb.append("}");
        return sb.toString();

    }
}
