package com.project.bot.module.hs.core.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HsWebSocketSender {

    private final WebSocketOutboundMessageHandler messageHandler;

    @Autowired
    public HsWebSocketSender(WebSocketOutboundMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @ServiceActivator(inputChannel = "webSocketOutboundChannel")
    public void sendToWebSocket(String message) {
        messageHandler.handleMessage(MessageBuilder.withPayload(message).build());
    }
}
