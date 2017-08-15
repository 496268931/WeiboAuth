package com.wiseweb.cat.base;

/**
 * Created by 贾承斌 on 15/11/7.
 */
public interface Logger {
    public void info(String message);
    public void debug(String message);
    public void error(String message);
    public void error(String message, Exception e);
    public void warning(String message);
}
