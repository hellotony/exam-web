package com.exam.support.base;

public class DataSourceContextHolder {
    public static final String MASTER = "master";
    public static final String SLAVE = "slave";
    private static final ThreadLocal<String> dbContext = new ThreadLocal<String>();


    public static void setDataSourceType(String dataSourceType) {
        dbContext.set(dataSourceType);
    }


    public static String getDataSourceType() {
        return dbContext.get();
    }

}
