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

    public void recordDataSend(Integer bufferSize) {
        writeAllBufferSize.apply(bufferSize);
    }

    public void recordDataReceived(Integer bufferSize) {
        readAllBufferSize.apply(bufferSize);
    }

    public void recordConnect(Integer size) {
        connectCounter.apply(size);
    }


    @Override
    public JSONObject metrics() {
        JSONObject window = new JSONObject();
        window.put("read_size", FormatUtils.formatByte(readAllBufferSize.allCount()));
        window.put("read_hour_size", FormatUtils.formatByte(readAllBufferSize.intervalCount()));
        window.put("write_size", FormatUtils.formatByte(writeAllBufferSize.allCount()));
        window.put("write_hour_size", FormatUtils.formatByte(writeAllBufferSize.intervalCount()));
        window.put("connect_size", connectCounter.allCount());
        return window;
    }


}
