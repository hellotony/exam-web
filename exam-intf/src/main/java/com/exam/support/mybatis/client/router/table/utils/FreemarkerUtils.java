package com.exam.support.mybatis.client.router.table.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FreemarkerUtils {
	private static Logger logger = LoggerFactory.getLogger(FreemarkerUtils.class);
	private static final String TEMPLATE_NAME = "DEFAULT_TEMPLATE_UTILS";
	private static Configuration configuration = new Configuration();

	private static ConcurrentHashMap<String, Template> templateCache = new ConcurrentHashMap<String, Template>();

	static {
		configuration.setClassicCompatible(true);
	}

	public static String execute(String expression, Map<String, Object> argument) {
		try {
			Template template = null;
			if (templateCache.containsKey(expression)) {
				template = templateCache.get(expression);
			} else {
				template = new Template(TEMPLATE_NAME, new StringReader(expression), configuration);
				templateCache.put(expression, template);
			}
			StringWriter writer = new StringWriter();
			template.process(argument, writer);
			return writer.getBuffer().toString();
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}
}
