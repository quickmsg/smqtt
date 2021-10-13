package io.github.quickmsg.common.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * banner
 *
 * @author zhaopeng
 */
@Slf4j
public class BannerUtils {

    /**
     * 打印banner
     */
    public static void banner() {
        try (InputStream is = BannerUtils.class.getResourceAsStream("/banner.txt")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            log.error("banner file not exists");
        }
    }


}


