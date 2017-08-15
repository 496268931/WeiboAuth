package com.wiseweb.cat.base;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Sane on 2016/10/24.
 */
public class SchedulerConfig {

    private static Properties properties;


    static {

        try {
            properties = new Properties();
            String filePath = System.getProperty("user.dir")
                    + "/config.properties";
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getObject(String prepKey) {
        return properties.getProperty(prepKey);

    }
}