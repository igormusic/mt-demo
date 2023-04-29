package com.example.setup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;

@SpringBootApplication
@Slf4j
public class SetupApplication {

    private final SetupRunner setupRunner;

    public SetupApplication( SetupRunner setupRunner) {
        this.setupRunner = setupRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(SetupApplication.class, args);
    }

    @PostConstruct
    public void executeAfterApplicationRunners() {

        setupRunner.run();

        log.info("Setup completed successfully");
        System.exit(0);

    }



}
