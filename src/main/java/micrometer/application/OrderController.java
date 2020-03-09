package micrometer.application;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.Callable;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    MeterRegistry registry;

    private Counter counter_index;
    private Counter counter_order;

    private Timer timer;

    @PostConstruct
    private void init(){
        counter_index = registry.counter("http_requests_method_count","method","get","status","success");
        counter_order = registry.counter("http_requests_method_count","method","post","status","failed");
//        counter_index = Metrics.counter("http_requests_method_count","method","get","status","success");
//        counter_order = Metrics.counter("http_requests_method_count","method","post","status","success");
        timer = Timer.builder("hisogram")
                .tag("key","histo")
                .publishPercentiles(0.95,0.99)
                .publishPercentileHistogram()
                .register(registry);

    }

    @PostMapping(value = "/order")
    public ResponseEntity<Boolean> createOrder(@RequestBody Order order){
        counter_order.increment();
        Callable callable = timer.wrap(()->orderService.createOrder(order));
        return ResponseEntity.ok(orderService.createOrder(order));
//        return ResponseEntity.ok(timer.record(()->orderService.createOrder(order)));
    }
}
