package com.github.smqtt.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author luxurong
 * @date 2021/4/22 18:36
 * @description
 */
@Slf4j
public class PropertiesLoader {


    public static Map<String, String> loadProperties(String filePath) {
        Map<String, String> map = new HashMap<>(16);
        Properties prop = new Properties();
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            prop.load(inputStream);
        } catch (IOException e) {
        }
        return map;
    }


    private static void getProperties(Properties props, Map<String, String> map) {
        @SuppressWarnings("rawtypes")
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String value = props.getProperty(key);
            map.put(key, value);
        }
    }


}
