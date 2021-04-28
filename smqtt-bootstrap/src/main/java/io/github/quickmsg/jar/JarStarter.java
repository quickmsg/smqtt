package io.github.quickmsg.jar;

import io.github.quickmsg.AbstractStarter;
import io.github.quickmsg.common.utils.PropertiesLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author luxurong
 * @date 2021/4/14 20:39
 * @description
 */
@Slf4j
public class JarStarter extends AbstractStarter {

    public static void main(String[] args) {
        Map<String, String> mas= PropertiesLoader.loadProperties("config.properties");
        log.info("JarStarter start args {}", String.join(",", args));
        if (args.length > 0) {
            start(System::getProperty, args[0]);
        } else {
            start(System::getProperty, null);
        }    }
}
