package io.github.quickmsg.rule.source;

import io.github.quickmsg.common.rule.source.Source;

/**
 * @author luxurong
 * @date 2021/8/24 16:14
 * @description
 */
public interface SourceFactory {

    SourceLoader instance(Source source);

}
