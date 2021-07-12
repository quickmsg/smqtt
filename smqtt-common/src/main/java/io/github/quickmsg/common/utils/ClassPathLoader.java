package io.github.quickmsg.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.io.*;

/**
 * @author luxurong
 */
@Slf4j
public class ClassPathLoader {


    public  static Mono<ByteBuf> readClassPathFile(String path) {
        try {
            InputStream inputStream = ClassPathLoader.class.getResourceAsStream(path);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int n ;
            while ((n = bufferedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, n);
            }
            return Mono.just(PooledByteBufAllocator.DEFAULT.buffer(out.size()).writeBytes(out.toByteArray()));
        } catch (IOException e) {
           log.error("readClassPathFile error {}",path,e);
        }
        return Mono.empty();
    }

}
