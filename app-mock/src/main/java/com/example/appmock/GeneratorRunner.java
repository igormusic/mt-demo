package com.example.appmock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class GeneratorRunner implements Runnable {

    private final Mt101Generator mt101Generator;
    private final Mt103Generator mt103Generator;
    private final Mt920Generator mt920Generator;

    @Value("${app.mock.generator.threads}")
    private int NUM_THREADS;
    private static final Random random = new Random();

    public GeneratorRunner(Mt101Generator mt101Generator, Mt103Generator mt103Generator, Mt920Generator mt920Generator) {
        this.mt101Generator = mt101Generator;
        this.mt103Generator = mt103Generator;
        this.mt920Generator = mt920Generator;
    }
    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        while (true) {
            int taskType = random.nextInt(3);

            switch (taskType) {
                case 0:
                    executorService.execute(mt101Generator);
                    break;
                case 1:
                    executorService.execute(mt103Generator);
                    break;
                case 2:
                    // code for task 3
                    executorService.execute(mt920Generator);
                    break;
            }
        }
    }
}
