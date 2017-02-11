package com.exam.support.util;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JackJsonDateParse extends JsonDeserializer<Date>{
    private static final Logger logger = LoggerFactory.getLogger(JackJsonDateParse.class);

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        SimpleDateFormat format = null;
        if (jp.getText().indexOf("-") > -1) {
            format = new SimpleDateFormat("yyyy-MM-dd");
        } else if (jp.getText().indexOf("/") > -1) {
            format = new SimpleDateFormat("yyyy/MM/dd");
        }

        String date = jp.getText();
        if(date==null || date.startsWith("0000") || date.trim().length()==0){
            return null;
        }
        try {
            return format.parse(date);
        } catch (Exception e) {
            logger.error("日期解析出错"+ JsonLogUtil.formatEx(e));
        }
        return null;
    }

}
