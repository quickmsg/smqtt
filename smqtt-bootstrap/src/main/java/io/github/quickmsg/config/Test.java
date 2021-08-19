package io.github.quickmsg.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sun.org.apache.bcel.internal.util.ClassPath;

import java.io.File;
import java.io.IOException;

/**
 * Created by  lxr.
 * User: luxurong
 * Date: 2021/8/19
 */
public class Test {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        StarterConfig starterConfig=mapper.readValue(new File("D:\\smqtt\\smqtt-bootstrap\\src\\main\\resources\\test.yaml"),StarterConfig.class);
        System.out.println("hjasdb");
    }
}
