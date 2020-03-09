package micrometer.application;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryMock {

    @Autowired
    MeterRegistry registry;

    public void memoryMock() throws InterruptedException {
        new Thread(new Runnable() {
            public void run() {
                //initial size 1024M
                int initSize = 1024 * 1024 * 1024;
                long totalUseMemory = initSize;

                Map<String, Object> map = registry.gaugeMapSize("map.size", Collections.<Tag>emptyList(), new HashMap<String,Object>());
                map.put("init", new byte[initSize]);

//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("init", new byte[initSize]);
                int i = 0;
                while (i<10) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                int useMemory = 1024 * 1024 * 10;
                totalUseMemory = totalUseMemory + useMemory;
                map.put("key" + i++, new byte[useMemory]);
                System.out.println("total use :" + totalUseMemory / 1024 / 1024 + "M");
            }
        }
        }).start();

        while (true){
            Thread.sleep(1000*60);
            System.out.println(Thread.currentThread().getId()+" alive");
        }
    }
}

