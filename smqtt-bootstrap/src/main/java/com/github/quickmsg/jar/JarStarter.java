package com.github.quickmsg.jar;

import com.github.quickmsg.AbstractStarter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luxurong
 * @date 2021/4/14 20:39
 * @description
 */
@Slf4j
public class JarStarter extends AbstractStarter {

    public static void main(String[] args) {
        log.info("JarStarter start args {}", String.join(",", args));
        if (args.length > 0) {
            start(System::getProperty, args[0]);
        } else {
            start(System::getProperty, args[0]);
        }    }
}
