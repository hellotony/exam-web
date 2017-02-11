package com.exam.support.mybatis.multiEnv;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.Constants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.PropertyPlaceholderHelper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Created by ljun on 2016/10/15.
 */
public class PropertyPlaceholderConfigurerSupportMultiEnvironment extends PropertyPlaceholderConfigurer {
    private String[] placeholderConfigLocations;
    private PropertyPlaceholderHelper helper;
    private String fileEncoding;
    private String placeholderPrefix = "${";
    private String placeholderSuffix = "}";
    private String valueSeparator = ":";
    private boolean ignoreUnresolvablePlaceholders;
    private static final Constants constants = new Constants(PropertyPlaceholderConfigurerSupportMultiEnvironment.class);
    private int systemPropertiesMode;
    public static final int SYSTEM_PROPERTIES_MODE_NEVER = 0;
    public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
    public static final int SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;
    public static final String SYSTEM_PREFIX = "system.";

    public PropertyPlaceholderConfigurerSupportMultiEnvironment() {
    }

    private synchronized void initPlaceholderHelper() {
        if(this.helper == null) {
            this.helper = new PropertyPlaceholderHelper(this.placeholderPrefix, this.placeholderSuffix, this.valueSeparator, this.ignoreUnresolvablePlaceholders);
        }

    }

    private PropertyPlaceholderHelper getPlaceholderHelper() {
        if(this.helper == null) {
            this.initPlaceholderHelper();
        }

        return this.helper;
    }

    public String[] getPlaceholderConfigLocations() {
        return this.placeholderConfigLocations;
    }

    public void setPlaceholderConfigLocations(String[] placeholderConfigLocations) {
        this.placeholderConfigLocations = placeholderConfigLocations;
    }

    protected Properties mergeProperties() throws IOException {
        final Properties properties = super.mergeProperties();
        this.setSystemEnvProperties(properties);
        String[] arr$ = this.getPlaceholderConfigLocations();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String configLocation = arr$[i$];
            this.resolvePlaceholder(configLocation, properties);
            PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver = new PropertyPlaceholderHelper.PlaceholderResolver() {
                public String resolvePlaceholder(String placeholderName) {
                    return PropertyPlaceholderConfigurerSupportMultiEnvironment.this.resolvePlaceholder(placeholderName, properties, PropertyPlaceholderConfigurerSupportMultiEnvironment.this.systemPropertiesMode);
                }
            };
            String envConfigLocation = this.getPlaceholderHelper().replacePlaceholders(configLocation, placeholderResolver);
            ClassPathResource resource = new ClassPathResource(envConfigLocation.substring("classpath:".length()), ClassUtils.getDefaultClassLoader());
            PropertiesLoaderUtils.fillProperties(properties, new EncodedResource(resource, this.fileEncoding));
        }

        return properties;
    }

    private void setSystemEnvProperties(Properties properties) {
        Set keys = properties.keySet();

        try {
            Iterator e = keys.iterator();

            while(true) {
                String key;
                do {
                    Object obj;
                    do {
                        if(!e.hasNext()) {
                            return;
                        }

                        obj = e.next();
                    } while(!(obj instanceof String));

                    key = (String)obj;
                } while(!key.startsWith("system.") && !key.startsWith("system.".toUpperCase()));

                String subfix = null;
                if(key.indexOf("system.") != -1) {
                    subfix = key.substring(key.indexOf("system.") + "system.".length());
                }

                if(key.indexOf("system.".toUpperCase()) != -1) {
                    subfix = key.substring(key.indexOf("system.".toUpperCase()) + "system.".length());
                }

                System.setProperty(subfix, properties.getProperty(key));
            }
        } catch (Exception var7) {
            ;
        }
    }

    public void setPlaceholderPrefix(String placeholderPrefix) {
        this.placeholderPrefix = placeholderPrefix;
        this.initPlaceholderHelper();
        super.setPlaceholderPrefix(placeholderPrefix);
    }

    public void setPlaceholderSuffix(String placeholderSuffix) {
        this.placeholderSuffix = placeholderSuffix;
        this.initPlaceholderHelper();
        super.setPlaceholderSuffix(placeholderSuffix);
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
        this.initPlaceholderHelper();
        super.setValueSeparator(valueSeparator);
    }

    public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
        this.initPlaceholderHelper();
        super.setIgnoreUnresolvablePlaceholders(ignoreUnresolvablePlaceholders);
    }

    public void setSystemPropertiesModeName(String constantName) throws IllegalArgumentException {
        this.systemPropertiesMode = constants.asNumber(constantName).intValue();
        super.setSystemPropertiesModeName(constantName);
    }

    public void setFileEncoding(String encoding) {
        this.fileEncoding = encoding;
        super.setFileEncoding(encoding);
    }
}

