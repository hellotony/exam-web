package com.exam.support.util.json.core.message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class Base64JsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private static final Logger log = LoggerFactory.getLogger(Base64JsonHttpMessageConverter.class);

    public Base64JsonHttpMessageConverter() {
        super();
        ObjectMapper mapper = getObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Set<String> fliterSet = new HashSet<String>();
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("filter",
                SimpleBeanPropertyFilter.serializeAllExcept(fliterSet));
        mapper.setFilters(filterProvider);
    }

    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {

        // JsonEncoding encoding =
        // getJsonEncoding(outputMessage.getHeaders().getContentType());
        try {
            // if (this.prefixJson) {
            // jsonGenerator.writeRaw("{} && ");
            // }
            byte[] bytes = getObjectMapper().writeValueAsBytes(object);
            FileCopyUtils.copy(Base64.encodeBase64(bytes), outputMessage.getBody());
        } catch (JsonProcessingException ex) {
            log.error("Could not write JSON: " + ex.getMessage(), ex);
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

}
