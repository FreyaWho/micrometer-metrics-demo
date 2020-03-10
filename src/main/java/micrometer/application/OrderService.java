package micrometer.application;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderService {

    private static final Random R = new Random();

    @Autowired
    private MessageService messageService;

    @Autowired
    MeterRegistry registry;

    public Boolean createOrder() {
        try {
            int ms = R.nextInt(50) + 50;
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (Exception e) {
            //no-op
        }

        Message message = new Message();
        messageService.sendMessage(message);
        return true;
    }
}
