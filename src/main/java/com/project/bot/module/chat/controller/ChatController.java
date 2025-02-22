package com.project.bot.module.chat.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.project.bot.common.util.qq.QUtils;
import com.project.bot.module.chat.core.qq.impl.GroupMessageService;
import com.project.bot.module.chat.pojo.vo.qq.QMessage;
import com.project.bot.module.chat.pojo.vo.qq.QMessageData;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import com.project.bot.module.chat.serivice.impl.BaiduErnieImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final Random random = new Random();

    @Value("${chat.napcat.client.group-url:you-client-url}")
    private String clientGroupUrl;

    private final GroupMessageService groupMessageService;

    private final BaiduErnieImpl baiduErnieImpl;

    @Autowired
    public ChatController(GroupMessageService groupMessageService,
                          BaiduErnieImpl baiduErnieImpl) {
        this.groupMessageService = groupMessageService;
        this.baiduErnieImpl = baiduErnieImpl;
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
        }
        if (ObjectUtils.isNotEmpty(response)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("content-type", "application/json;charset=UTF-8");
            HttpEntity<QGroupMessage> formEntity = new HttpEntity<>(response, headers);
            restTemplate.postForObject(clientGroupUrl, formEntity, String.class);
        }

    }

    /**
     * <p>
     *  前端控制器
     * </p>
     *
     * @author lee
     * @since 2025-02-19
     */
    @RestController
    @RequestMapping("/friend")
    public static class FriendController {

    }

    public static String closeMessage(){
        QGroupMessage QGroupMessage = new QGroupMessage();
        QGroupMessage.setGroup_id("953136144");
        QMessage QMessage = new QMessage();
        QMessage.setType("text");
        QMessageData QMessageData = new QMessageData();
        QMessageData.setText("再见！");
        QMessage.setData(QMessageData);
        QGroupMessage.setMessage(Lists.newArrayList(QMessage));
        return JSON.toJSONString(QGroupMessage);
    }
}
