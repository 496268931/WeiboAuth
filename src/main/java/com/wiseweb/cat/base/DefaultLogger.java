package com.wiseweb.cat.base;

/**
 * Created by 贾承斌 on 15/11/7.
 */
public class DefaultLogger implements Logger {
    @Override
    public void info(String message) {
        System.out.println(message);
    }

    @Override
    public void debug(String message) {
        System.out.println(message);
    }

    @Override
    public void error(String message) {
        System.out.println(message);
    }

    @Override
    public void error(String message, Exception e) {
        System.out.println(message);
        e.printStackTrace();
    }

    @Override
    public void warning(String message) {
        System.out.println(message);
    }
}
