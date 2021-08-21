package io.github.quickmsg.jar;

import io.github.quickmsg.AbstractStarter;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author luxurong
 */
@Slf4j
public class JarStarter extends AbstractStarter {

    public static void main(String[] args) {
        log.info("JarStarter start args {}", String.join(",", args));
        if (args.length > 0) {
            start(args[0]);
        } else {
            start(null);
        }
    }
}
