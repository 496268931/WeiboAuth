package com.wiseweb.cat.base;


/**
 * Created by 贾承斌 on 15/11/7.
 */
public class LoggerFactory {
    private static Logger uniqueInstance = null;

    private LoggerFactory() {};

    public static void initLogger(Logger logger) {
        if (logger != null) {
            uniqueInstance = logger;
        }
    }

    public static Logger getLogger() {
        if (uniqueInstance == null) {
            uniqueInstance = new DefaultLogger();
        }
        return uniqueInstance;
    }
}
