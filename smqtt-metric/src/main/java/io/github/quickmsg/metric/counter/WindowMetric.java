package io.github.quickmsg.metric.counter;

import com.alibaba.fastjson.JSONObject;
import io.github.quickmsg.metric.MetricsGetter;
import io.github.quickmsg.metric.utils.FormatUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author luxurong
 */
@Getter
@Slf4j
public class WindowMetric implements MetricsGetter {


    public final static WindowMetric WINDOW_METRIC_INSTANCE = new WindowMetric();

    private WindowMetric() {

    }

    private WindowCounter readAllBufferSize = new SideWindowCounter(1, TimeUnit.HOURS, "TRANSPORT-READ-BUFFER-SIZE");

    private WindowCounter writeAllBufferSize = new SideWindowCounter(1, TimeUnit.HOURS, "TRANSPORT-WRITE-BUFFER-SIZE");

    private WindowCounter connectCounter = new SimpleWindowCounter();

    private WindowCounter subscribeCounter = new SimpleWindowCounter();

    public void recordDataSend(Integer bufferSize) {
        writeAllBufferSize.apply(bufferSize);
    }

    public void recordDataReceived(Integer bufferSize) {
        readAllBufferSize.apply(bufferSize);
    }

    public void recordConnect(Integer size) {
        connectCounter.apply(size);
    }

    public void recordSubscribe(Integer size) {
        subscribeCounter.apply(size);
    }


    @Override
    public JSONObject metrics() {
        JSONObject window = new JSONObject();
        long readHour = readAllBufferSize.intervalCount();
        long writeHour = writeAllBufferSize.intervalCount();
        window.put("read_size", FormatUtils.formatByte(readAllBufferSize.allCount() + readHour));
        window.put("read_hour_size", FormatUtils.formatByte(readHour));
        window.put("write_size", FormatUtils.formatByte(writeAllBufferSize.allCount() + writeHour));
        window.put("write_hour_size", FormatUtils.formatByte(writeHour));
        window.put("connect_size", connectCounter.allCount());
        return window;
    }


}
