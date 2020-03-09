package micrometer.application;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplcationRun {
    public static void main(String[] args) {
        SpringApplication.run(ApplcationRun.class, args);
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> config(@Value("${spring.application.name}") String appName){
        return registry -> registry.config().commonTags("application",appName);
    }
}
