package io.github.quickmsg.common.rule;

import lombok.Data;

import java.util.Map;

/**
 * @author luxurong
 */

@Data
public class SourceDefinition {

    private String sourceType;

    private String sourceName;

    private Map<String, Object> sourceAttributes;
}
