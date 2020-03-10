package micrometer.application;

import io.micrometer.core.instrument.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    MeterRegistry registry;

    private Counter counter_index;
    String[] counter_method = new String[]{"get","post","put"};
    String[] counter_status = new String[]{"200","300","400"};

    private Timer timer;
    private DistributionSummary summary;

    @PostConstruct
    private void init(){
        counter_index = registry.counter("http_requests_method_count","method",counter_method[new Random().nextInt(counter_method.length-1)],
                "status",counter_status[new Random().nextInt(counter_status.length-1)]);
        timer = Timer.builder("http_requests")
                .tag("key","histo")
                .publishPercentiles(0.95,0.99)
                .publishPercentileHistogram()
                .register(registry);
        summary = DistributionSummary.builder("simple")
                .description("simple distribution summary")
                .publishPercentiles(0.5, 0.75, 0.9)
                .register(registry);
        summary.record(timer.totalTime(TimeUnit.SECONDS));
    }

    @GetMapping(value = "/index")
    public ResponseEntity<String> getIndex(){
        counter_index.increment();
        return ResponseEntity.ok("get index success");
    }

    @PostMapping(value = "/order")
    public ResponseEntity<String> createOrder(HttpServletRequest request, HttpServletResponse response){
        timer.wrap(()->orderService.createOrder());
        orderService.createOrder();
        return ResponseEntity.ok(request.getParameter("name")+request.getParameter("id"));
    }


}
