package io.github.quickmsg.jar;

import io.github.quickmsg.AbstractStarter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luxurong
 */
@Slf4j
public class JarStarter extends AbstractStarter {

    public static void main(String[] args) {
        log.info("JarStarter start args {}", String.join(",", args));
        if (args.length > 0) {
            start(System::getProperty, args[0]);
        } else {
            start(System::getProperty, null);
        }
    }
}
