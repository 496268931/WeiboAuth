package com.wiseweb.cat.base;

/**
 * Created by 贾承斌 on 15/11/7.
 */
public class ConfigerFactory {
    private static Configer uniqueInstance = null;

    private ConfigerFactory() {}

    public static void initConfiger(Configer configer) {
        if (configer != null) {
            uniqueInstance = configer;
        }
    }

    public static Configer getConfiger() {
        if (uniqueInstance == null) {
            uniqueInstance = new DefaultConfiger();
        }
        return uniqueInstance;
    }
}
