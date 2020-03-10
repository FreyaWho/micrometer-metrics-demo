package micrometer.application;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class MessageService implements InitializingBean {

    private static final BlockingQueue<Message> QUEUE = new ArrayBlockingQueue<>(500);
    private static BlockingQueue<Message> REAL_QUEUE;
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Random R = new Random();

    static {
        REAL_QUEUE = Metrics.gauge("message.gauge", Tags.of("message.gauge", "message.queue.size"), QUEUE, Collection::size);
    }

    private DebugLogger log;

    public void sendMessage(Message message) {
        try {
            REAL_QUEUE.put(message);
        } catch (InterruptedException e) {
            //no-op
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EXECUTOR.execute(() -> {
            while (true) {
                try {
                    REAL_QUEUE.take();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }
}

