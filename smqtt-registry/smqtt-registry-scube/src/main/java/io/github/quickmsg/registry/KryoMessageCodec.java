package io.github.quickmsg.registry;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.cluster.transport.api.MessageCodec;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author luxurong
 * @date 2021/5/8 17:42
 * @description
 */
public class KryoMessageCodec implements MessageCodec {

    private final Kryo kryo;

    public KryoMessageCodec() {
        this.kryo = new Kryo();
        kryo.register(Message.class);
    }


    @Override
    public Message deserialize(InputStream stream) throws Exception {
        Input input = new Input(stream);
        return kryo.readObject(input, Message.class);
    }

    @Override
    public void serialize(Message message, OutputStream stream) throws Exception {
        Output output = new Output(stream);
        kryo.writeObject(output, message);
    }
}
