package io.github.quickmsg.docker;

import io.github.quickmsg.AbstractStarter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luxurong
 */
@Slf4j
public class DockerStarter extends AbstractStarter {

    private static final String CONFIG_MAPPER_DIR = "config.properties";


    public static void main(String[] args) {
        log.info("DockerStarter start args {}", String.join(",", args));
        start(System::getenv, CONFIG_MAPPER_DIR);
    }


}
