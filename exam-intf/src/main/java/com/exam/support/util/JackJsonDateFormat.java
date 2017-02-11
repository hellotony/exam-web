package com.exam.support.util;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期解析
 * @author liuchao2
 *
 */
public class JackJsonDateFormat extends JsonSerializer<Date>{

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        String formattedDate = "";
        if (value != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = formatter.format(value);
        }
        jgen.writeString(formattedDate); 
    }

}
