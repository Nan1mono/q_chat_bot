package com.project.bot.module.hs;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableIntegration
public class HomeRestTemplate {


    public static final String HA_WS_URL = "ws://192.168.66.130:8123/api/websocket";

    public static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJkM2MzZWMwZmI3OWE0ZjI5YTQ5MWEzNDcwZDBjM2YxOCIsImlhdCI6MTc0MDM4MDU3MiwiZXhwIjoyMDU1NzQwNTcyfQ.1yRdbZK1PFCCSURerHo45MqIbmatQSFTWZZ3_i9EyXs";

    private static int messageId = 1;

    public static void main(String[] args) {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        URI uri = URI.create(HA_WS_URL);

        client.execute(uri, session -> {
            System.out.println("WebSocket 连接已建立");

            // 接收消息流
            Mono<Void> input = session.receive()
                    .map(WebSocketMessage::getPayloadAsText)
                    .doOnNext(message -> {
                        System.out.println("收到消息: " + message);
                        JSONObject jsonMessage = JSON.parseObject(message);

                        if ("auth_required".equals(jsonMessage.getString("type"))) {
                            sendAuthMessage(session);
                        } else if ("auth_ok".equals(jsonMessage.getString("type"))) {
                            System.out.println("认证成功，HA 版本: " + jsonMessage.getString("ha_version"));
                            getDeviceList(session); // 获取设备列表
                        } else if ("result".equals(jsonMessage.getString("type"))) {
                            if (jsonMessage.getBoolean("success")) {
                                System.out.println("设备列表: " + jsonMessage.getJSONArray("result"));
                            } else {
                                System.err.println("命令失败: " + jsonMessage.getJSONObject("error"));
                            }
                        }
                    })
                    .doOnError(error -> System.err.println("接收流错误: " + error.getMessage()))
                    .then();

            // 保持连接活跃
            return input;
        }).subscribe(
                null,
                error -> System.err.println("连接错误: " + error.getMessage()),
                () -> System.out.println("WebSocket 连接已关闭")
        );

        // 主线程保持运行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendAuthMessage(WebSocketSession session) {
        JSONObject authMessage = new JSONObject();
        authMessage.put("type", "auth");
        authMessage.put("access_token", ACCESS_TOKEN);
        session.send(Mono.just(session.textMessage(authMessage.toString())))
                .subscribe();
        System.out.println("已发送认证消息: " + authMessage);
    }

    private static void subscribeToStates(WebSocketSession session) {
        JSONObject subscribeMessage = new JSONObject();
        subscribeMessage.put("id", messageId++);
        subscribeMessage.put("type", "subscribe_events");
        subscribeMessage.put("event_type", "state_changed");
        session.send(Mono.just(session.textMessage(subscribeMessage.toString())))
                .subscribe();
        System.out.println("已发送订阅请求: " + subscribeMessage);
    }

    private static void getDeviceList(WebSocketSession session) {
        JSONObject subscribeMessage = new JSONObject();
        subscribeMessage.put("id", messageId++);
        subscribeMessage.put("type", "config/device_registry/list");
        session.send(Mono.just(session.textMessage(subscribeMessage.toString())))
                .subscribe();
        System.out.println("已发送设备列表请求: " + subscribeMessage);
    }

}
