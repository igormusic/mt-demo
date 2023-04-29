package com.example.appmock;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

public abstract class MessageGenerator implements Runnable {
    @Value("${app.mock.generator.mindelay}")
    int minDelay;
    @Value("${app.mock.generator.maxdelay}")
    int maxDelay;

    @Value("${app.mock.generator.senderbic}")
    protected String senderBic;

    protected void delay() throws InterruptedException {
        Thread.sleep((long) (Math.random() * (maxDelay - minDelay)) + minDelay);
    }

    @SneakyThrows
    @Override
    public void run() {
        createMessage();
        delay();
    }

    abstract void createMessage();
}
