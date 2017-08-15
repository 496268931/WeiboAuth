package com.wiseweb.cat.base;

import org.springframework.util.ClassUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by 贾承斌 on 15/11/7.
 * Modified by 张恺 on 16/15/7
 */
public class Configer {

    private static final String resourceName = System.getProperty("user.dir")
            + "/src/main/resources/config.properties";
    private static final Properties cfgProperties = PropertiesUtils.getProperties(resourceName);

    public String get(String key) {
        return cfgProperties.getProperty(key);
    }

//    public static void set(String key, String value) {
//        cfgProperties.setProperty(key, value);
//        try {
//            //从输入流中读取属性列表（键和元素对）
//            cfgProperties.load(new FileInputStream(resourceName));
//            //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
//            //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
//            OutputStream fos = new FileOutputStream(resourceName);
//            cfgProperties.setProperty(key, value);
//            //以适合使用 load 方法加载到 Properties 表中的格式，
//            //将此 Properties 表中的属性列表（键和元素对）写入输出流
//            cfgProperties.store(fos, "Update '" + key + "' :" + value);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
