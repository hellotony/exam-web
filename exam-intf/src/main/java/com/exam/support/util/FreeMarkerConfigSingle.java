package com.exam.support.util;

import freemarker.template.TemplateException;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by lijun7 on 2016/4/27.
 */
public enum FreeMarkerConfigSingle {
    INSTANCE;
    public FreeMarkerConfigurer getFreeMarkerConfig(){

        FreeMarkerConfigurer freeMarkerConfig = new FreeMarkerConfigurer();
        freeMarkerConfig.setDefaultEncoding("UTF-8");
        try {
            freeMarkerConfig.setTemplateLoaderPath(String.valueOf(ResourceUtils.getURL("classpath:ftl/email/")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            freeMarkerConfig.afterPropertiesSet();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return freeMarkerConfig;
    }
}
