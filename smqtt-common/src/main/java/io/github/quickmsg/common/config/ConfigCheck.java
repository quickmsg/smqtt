package io.github.quickmsg.common.config;

import io.github.quickmsg.common.metric.MeterType;

import java.util.Objects;

/**
 * @author luxurong
 */
public class ConfigCheck {

    public static void checkMeterConfig(BootstrapConfig.MeterConfig config) {
        if (config != null) {
            if (config.getMeterType() == MeterType.INFLUXDB) {
                if (config.getInfluxdb() != null) {
                    Objects.requireNonNull(config.getInfluxdb().getUri());
                    Objects.requireNonNull(config.getInfluxdb().getDb());
                }
            }
        }
    }

}
