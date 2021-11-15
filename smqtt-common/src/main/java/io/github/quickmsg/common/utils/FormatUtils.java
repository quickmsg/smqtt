package io.github.quickmsg.common.utils;

import java.text.DecimalFormat;

/**
 * @author luxurong
 */
public class FormatUtils {

    /**
     * 单位转换
     *
     * @param byteNumber number
     * @return desc
     */
    public static String formatByte(long byteNumber) {
        double format = 1024.0;
        double kbNumber = byteNumber / format;
        if (kbNumber < format) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / format;
        if (mbNumber < format) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / format;
        if (gbNumber < format) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / format;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }

    /**
     * 单位转换
     *
     * @param byteNumber number
     * @return desc
     */
    public static String formatByte(double byteNumber) {
        double format = 1024.0;
        double kbNumber = byteNumber / format;
        if (kbNumber < format) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / format;
        if (mbNumber < format) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / format;
        if (gbNumber < format) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / format;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }
}
