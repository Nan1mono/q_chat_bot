package com.project.bot.module.hs.core.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;

@Slf4j
@Configuration
public class HsWebSocketListener {

    @ServiceActivator(inputChannel = "webSocketInboundChannel")
    public void listen(String message) {
        log.info("receive web socket message: {}", message);
    }

}
