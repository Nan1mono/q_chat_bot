package com.project.bot.module.chat.controller;

import com.alibaba.fastjson2.JSONObject;
import com.project.bot.common.util.qq.QUtils;
import com.project.bot.module.chat.core.qq.impl.GroupMessageService;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final Random random = new Random();

    private final GroupMessageService groupMessageService;


    @Autowired
    public ChatController(GroupMessageService groupMessageService) {
        this.groupMessageService = groupMessageService;
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
        QGroupMessage response;
        if (message.contains("介绍-")) {
            response = groupMessageService.getFriendByName(jsonObject);
        } else if (message.contains("添加-")) {
            response = groupMessageService.saveFriend(jsonObject);
        } else if (message.contains("帮助-如何添加介绍")) {
            response = groupMessageService.helpSaveFriend(jsonObject);
        } else if (message.contains("设备列表")) {
            response = groupMessageService.getDeviceList(jsonObject);
        } else {
            response = groupMessageService.chatWithAi(jsonObject);
        }
        groupMessageService.sendMessage(response);
    }


}
