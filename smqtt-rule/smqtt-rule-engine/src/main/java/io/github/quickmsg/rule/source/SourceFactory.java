package io.github.quickmsg.rule.source;

import io.github.quickmsg.common.rule.source.Source;

/**
 * @author luxurong
 */
public interface SourceFactory {

    SourceLoader instance(Source source);

}
