package com.project.bot.module.chat.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.project.bot.common.util.qq.QUtils;
import com.project.bot.module.chat.core.qq.impl.GroupMessageService;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import com.project.bot.module.hs.core.sender.HsWebSocketSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static com.project.bot.module.hs.HomeRestTemplate.ACCESS_TOKEN;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final Random random = new Random();

    @Value("${chat.napcat.client.group-url:you-client-url}")
    private String clientGroupUrl;

    private final GroupMessageService groupMessageService;

    private final HsWebSocketSender hsWebSocketSender;

    @Autowired
    public ChatController(GroupMessageService groupMessageService,
                          HsWebSocketSender hsWebSocketSender) {
        this.groupMessageService = groupMessageService;
        this.hsWebSocketSender = hsWebSocketSender;
    }

    @PostMapping("/receive")
    public void receive(@RequestBody JSONObject jsonObject) throws InterruptedException {
        String message;
        try {
            message = jsonObject.get("raw_message").toString();
        } catch (Exception e) {
            return;
        }
        log.info("receive message: {}", message);
        if (!QUtils.isGroupAt(jsonObject)) {
            return;
        }
        int randomNumber = random.nextInt(3001) + 2000;
        // 沉睡线程，降低回复速度
        Thread.sleep(randomNumber);
        QGroupMessage response = null;
        if (message.contains("介绍-")) {
            response = groupMessageService.getFriendByName(jsonObject);
        } else if (message.contains("添加-")) {
            response = groupMessageService.saveFriend(jsonObject);
        } else if (message.contains("帮助-如何添加介绍")){
            response = groupMessageService.helpSaveFriend(jsonObject);
        } else {
            response = groupMessageService.chatWithAi(jsonObject);
        }
        if (ObjectUtils.isNotEmpty(response)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("content-type", "application/json;charset=UTF-8");
            HttpEntity<QGroupMessage> formEntity = new HttpEntity<>(response, headers);
            restTemplate.postForObject(clientGroupUrl, formEntity, String.class);
        }

    }

    @GetMapping("/socket/test")
    public void socketTest(){
        hsWebSocketSender.sendToWebSocket(sendAuthMessage());
    }


    private static String sendAuthMessage() {
        JSONObject authMessage = new JSONObject();
        authMessage.put("type", "auth");
        authMessage.put("access_token", ACCESS_TOKEN);
        return JSON.toJSONString(authMessage);
    }

}
