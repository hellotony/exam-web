package com.exam.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Repository;

@Repository("sampleListener")
public class SampleListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("||||||||||||||" + ((TextMessage) message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
