package com.exam.support.util.json.core.message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.exam.support.util.json.core.utils.ClientCompressFactory;
import com.exam.support.util.json.core.utils.IClientCompressUtils;
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

/**
 * Created by liurui on 14-6-23.
 */
public class CompressHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    //log
    private static final Logger log = LoggerFactory.getLogger(CompressHttpMessageConverter.class);

    //Converter 类型
    private int converterType;
    //
    private int compressType;

    public CompressHttpMessageConverter() {
        super();
        initialConverter();
    }

    public CompressHttpMessageConverter(int converterType, int compressType) {
        super();
        this.converterType = converterType;
        this.compressType = compressType;
        initialConverter();
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        try {
            byte[] bytes = getObjectMapper().writeValueAsBytes(object);
            byte[] lastBytes = null;
            IClientCompressUtils iClientCompressUtils = null;
            switch (converterType) {
                case MessageConverterTpyeUtil.MESSAGE_CONVERTER_TYPE_JSON:
                    //不使用base64
                    break;
                case MessageConverterTpyeUtil.MESSAGE_CONVERTER_TYPE_JSON_BASE64:
                    bytes = Base64.encodeBase64(bytes);
                    break;
                default:
                    bytes = Base64.encodeBase64(bytes);
                    break;
            }
            switch (compressType) {
                case ClientCompressFactory.COMPRESS_GZIP:
                    iClientCompressUtils = ClientCompressFactory.getCompressUtils(ClientCompressFactory.COMPRESS_GZIP);
                    lastBytes = iClientCompressUtils.compress(bytes);
                    outputMessage.getHeaders().set(ClientCompressFactory.HTTP_HEAD_DATA_COMPRESSTYPE, ClientCompressFactory.COMPRESS_NAME_GZIP);
                    break;
                case ClientCompressFactory.COMPRESS_SNAPPY:
                    iClientCompressUtils = ClientCompressFactory.getCompressUtils(ClientCompressFactory.COMPRESS_SNAPPY);
                    lastBytes = iClientCompressUtils.compress(bytes);
                    outputMessage.getHeaders().set(ClientCompressFactory.HTTP_HEAD_DATA_COMPRESSTYPE, ClientCompressFactory.COMPRESS_NAME_SNAPPY);
                    break;
                case ClientCompressFactory.COMPRESS_UNKNOW:
                    lastBytes = bytes;
                    break;
                default:
                    lastBytes = bytes;
                    break;
            }
            FileCopyUtils.copy(lastBytes, outputMessage.getBody());
        } catch (JsonProcessingException ex) {
            log.error("Could not write JSON: " + ex.getMessage(), ex);
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    private void initialConverter() {
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
}
