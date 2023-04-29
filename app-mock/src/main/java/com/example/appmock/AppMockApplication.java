package com.example.appmock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
@Slf4j
public class AppMockApplication {

    private final GeneratorRunner runGenerators;
    private final ApplicationContext applicationContext;


    public AppMockApplication(GeneratorRunner runGenerators, ApplicationContext applicationContext) {
        this.runGenerators = runGenerators;
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(AppMockApplication.class, args);

        System.out.println("press any key to exit...");
        var key = System.in.read();
    }


    @PostConstruct
    public void executeAfterApplicationRunners() {
        List<ApplicationRunner> runners = getApplicationRunners();
        runners.forEach(r -> {
            try {
                r.run(null);
            } catch (Exception e) {
                log.error("Error running application runner {}", r.getClass().getName(), e);
            }
        });

        runGenerators.run();
    }

    private List<ApplicationRunner> getApplicationRunners() {
        return List.of(applicationContext.getBeanProvider(ApplicationRunner.class)
                .orderedStream()
                .toArray(ApplicationRunner[]::new));
    }

}
