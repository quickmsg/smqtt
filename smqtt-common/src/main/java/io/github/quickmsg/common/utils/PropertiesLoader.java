package io.github.quickmsg.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author luxurong
 */
@Slf4j
public class PropertiesLoader {


    public static Map<String, String> loadProperties(String filePath) {
        Map<String, String> map = new HashMap<>(16);
        Properties prop = new Properties();
        try {

            InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            prop.load(inputStream);
            loadMap(prop,map);
        } catch (IOException e) {
        }
        return map;
    }


    private static void loadMap(Properties props, Map<String, String> map) {
        @SuppressWarnings("rawtypes")
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String value = props.getProperty(key);
            map.put(key, value);
        }
    }


}
