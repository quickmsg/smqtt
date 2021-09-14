package io.github.quickmsg.common.rule.source;

import lombok.Data;

import java.util.Map;

/**
 * @author luxurong
 */
@Data
public class SourceDefinition {

    private Source source;

    private String sourceName;

    private String replace;

    private Map<String, Object> sourceAttributes;
}
