package com.exam.support.base;

public class EnvContext {
    /**
     * 开发环境
     */
    public static final String ENV_DEV = "dev";
    /**
     * 环境名称
     */
    private static String ENV_NAME;


    protected static void setEnvName(String envName) {
        ENV_NAME = envName;
    }


    /**
     * 获取当前WEB环境，如果变量为空，默认返回开发环境
     * @return
     */
    public static String getEnvName() {
        if (ENV_NAME == null)
            return ENV_DEV;
        return ENV_NAME;
    }

    /**
     * 是否是开发环境
     * @return
     */
    public static boolean isDev() {
        return ENV_DEV.equals(getEnvName());
    }

}
