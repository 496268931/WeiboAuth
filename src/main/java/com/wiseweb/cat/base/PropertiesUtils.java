package com.wiseweb.cat.base;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by Sane on 16/7/5.
 */
public class PropertiesUtils {
    private static Logger logger = Logger.getLogger(PropertiesUtils.class);

    private static String CFG_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/config.properties";

    private static Properties cfgProperties = getProperties(CFG_FILE_PATH);

    /**
     * 获取配置文件"cfg.properties"中的配置信息
     *
     * @param key
     * @return
     */
    public static String getCfgInfo(String key) {
        return cfgProperties == null ? null : cfgProperties.getProperty(key);
    }

    /**
     * 获取指定资源文件指定信息
     *
     * @param propertiesFilePath
     * @param key
     * @return
     */
    public static String getValue(String propertiesFilePath, String key) {
        return getProperties(propertiesFilePath).getProperty(key);
    }


    /**
     * 设置指定资源文件指定信息的值
     *
     * @param propertiesFilePath
     * @param key
     * @param value
     * @return
     */
    public static void setValue(String propertiesFilePath, String key, String value) {
        getProperties(propertiesFilePath).setProperty(key, value);
    }

    /**
     * 获取指定的资源对象
     *
     * @param propertiesFilePath
     * @return
     */
    public static Properties getProperties(String propertiesFilePath) {
        Properties properties = new Properties();
        try {
            logger.info("加载资源[" + propertiesFilePath + "] ...");
            InputStream in = new BufferedInputStream(new FileInputStream(propertiesFilePath));
            properties.load(in);
        } catch (IOException e) {
            logger.error("加载资源[" + propertiesFilePath + "]失败", e);
        }
        return properties;
    }
}

