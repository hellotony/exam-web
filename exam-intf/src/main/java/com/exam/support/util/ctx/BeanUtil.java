package com.exam.support.util.ctx;

import org.springframework.web.context.WebApplicationContext;

public class BeanUtil {
	private static WebApplicationContext ctx;
	public static void putContext(WebApplicationContext ctx){
		BeanUtil.ctx = ctx;
	}
	
	public static WebApplicationContext getCtx() {
		return ctx;
	}

	public static <T> T getBean(String id,Class<T> clazz){
		return BeanUtil.ctx.getBean(id, clazz);
	}

	public static <T> T getBean(Class<T> clazz) {
		return ctx.getBean(clazz);
	}

	public static Object getBean(String id) {
		return ctx.getBean(id);
	}

}
