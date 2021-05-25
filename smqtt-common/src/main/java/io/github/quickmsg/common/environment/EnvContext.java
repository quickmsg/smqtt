package io.github.quickmsg.common.environment;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author luxurong
 */
@Getter
public class EnvContext {

    private final Map<String, String> environments;

    public EnvContext() {
        this.environments = new HashMap<>();
    }


    public EnvContext(Map<String, String> environments) {
        this.environments = environments;
    }

    public Optional<String> obtainKey(String key) {
        return Optional.ofNullable(environments)
                .map(envs -> envs.get(key));
    }

    public String obtainKeyOrDefault(String key, String defaultValue) {
        return Optional.ofNullable(environments)
                .map(envs -> envs.get(key)).orElse(defaultValue);
    }

    public void put(String key, String defaultValue) {
        Optional.ofNullable(environments)
                .ifPresent(envs -> envs.put(key, defaultValue));
    }

    public static EnvContext empty() {
        return new EnvContext(null);
    }


}
