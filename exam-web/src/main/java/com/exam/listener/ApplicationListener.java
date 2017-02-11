package com.exam.listener;

import com.exam.support.base.EnvContext;
import com.exam.support.util.ctx.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 应用监听器
 */
public class ApplicationListener extends EnvContext implements ServletContextListener{

    private static Logger runningLogger = LoggerFactory.getLogger(ApplicationListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //环境类型
        EnvContext.setEnvName("dev");

        runningLogger.info("start to WebInit contextInitialized");

		/* used as service locator */
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        BeanUtil.putContext(ctx);
        runningLogger.info("WebInit contextInitialized end");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        runningLogger.info("start WebInit contextDestroyed");

        runningLogger.info("WebInit contextDestroyed end");
    }
}
