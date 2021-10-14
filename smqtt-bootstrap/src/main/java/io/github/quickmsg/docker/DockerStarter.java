package io.github.quickmsg.docker;

import io.github.quickmsg.AbstractStarter;
import io.github.quickmsg.common.utils.FileExtension;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author luxurong
 */
@Slf4j
public class DockerStarter extends AbstractStarter {

    private final static String CONFIG_DIR_PATH = "/config";

    public static void main(String[] args) {
        log.info("DockerStarter start args {}", String.join(",", args));
        start(findConfigByPath());
    }

    private static String findConfigByPath() {
        File configFile = new File(CONFIG_DIR_PATH);
        File[] files = configFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                String filename = file.getName();
                if (filename.endsWith(FileExtension.PROPERTIES_SYMBOL)
                        || filename.endsWith(FileExtension.YAML_SYMBOL_1)
                        || filename.endsWith(FileExtension.YAML_SYMBOL_2)) {
                    return file.getAbsolutePath();
                }
            }
        }

        return null;
    }


}
