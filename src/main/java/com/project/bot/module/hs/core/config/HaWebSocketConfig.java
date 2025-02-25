package com.project.bot.module.hs.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Slf4j
@Configuration
@EnableIntegration
public class HaWebSocketConfig {

    public static final String HA_WS_URL = "ws://192.168.66.130:8123/api/websocket";

    // 初始化webSocket客户端
    @Bean
    public WebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }

    // 初始化webSocket发送通道
    @Bean
    public MessageChannel webSocketOutboundChannel() {
        return new DirectChannel();
    }

    // 初始化webSocket接收通道
    @Bean
    public MessageChannel webSocketInboundChannel() {
        return new DirectChannel();
    }

    // 初始化webSocket适配器，将webSocket消息发送到webSocketInboundChannel
    @Bean
    public WebSocketInboundChannelAdapter webSocketInboundChannelAdapter(ClientWebSocketContainer container) {
        WebSocketInboundChannelAdapter adapter = new WebSocketInboundChannelAdapter(container);
        adapter.setOutputChannel(webSocketInboundChannel());
        adapter.setAutoStartup(true);
        return adapter;
    }

    // 简历webSocket链接容器
    @Bean
    public ClientWebSocketContainer webSocketContainer(WebSocketClient webSocketClient) {
        ClientWebSocketContainer container = new ClientWebSocketContainer(webSocketClient, HA_WS_URL);
        container.setConnectionTimeout(5000); // 连接超时 5 秒
        container.setAutoStartup(true); // 自动启动
        container.start();
        return container;
    }


    // 用于发送消息，将指定的消息通过webSocket链接容器发送到指定的webSocket服务端
    @Bean
    public WebSocketOutboundMessageHandler webSocketOutboundMessageHandler(ClientWebSocketContainer container) {
        return new WebSocketOutboundMessageHandler(container);
    }

}
