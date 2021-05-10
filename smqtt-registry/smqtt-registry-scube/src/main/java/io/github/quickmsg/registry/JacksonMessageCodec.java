package io.github.quickmsg.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.cluster.transport.api.MessageCodec;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author luxurong
 */
public class JacksonMessageCodec implements MessageCodec {

    private final ObjectMapper delegate;

    public JacksonMessageCodec() {
        this(DefaultObjectMapper.OBJECT_MAPPER);
    }

    public JacksonMessageCodec(ObjectMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public Message deserialize(InputStream stream) throws Exception {
        return this.delegate.readValue(stream, Message.class);
    }

    @Override
    public void serialize(Message message, OutputStream stream) throws Exception {
        this.delegate.writeValue(stream, message);
    }
}
