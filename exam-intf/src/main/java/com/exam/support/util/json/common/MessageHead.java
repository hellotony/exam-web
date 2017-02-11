package com.exam.support.util.json.common;
/*
 * 客户端和注册中心通信的消息的数据结构
 */
public class MessageHead {
    
	//消息类型
	private int msgType;
    //发送时间
	private long sendTime; //System.currentTimeMillis()
    //接收者
	private String sender;
    //发送者
	private String receiver;
    
    //通用客户端版本号
	private String version;
    
    //发送的消息数据
	private String data;//json type
	
	public void setMsgType(int mt) {
		this.msgType = mt;
	}
	public int getMsgType() {
		return this.msgType;
	}
	public void setSendTime(long st) {
		this.sendTime = st;
	}
	public long getSendTime() {
		return this.sendTime;
	}
	public void setSender(String send) {
		this.sender = send;
	}
	public String getSender() {
		return this.sender;
	}
	public void setReceiver(String rec) {
		this.receiver = rec;
	}
	public String getReceiver() {
		return this.receiver;
	}
	public void setVersion(String ver) {
		this.version = ver;
	}
	public String getVersion() {
		return this.version;
	}
	public void setData(String dat) {
		this.data = dat;
	}
	public String getData() {
		return this.data;
	}
}
