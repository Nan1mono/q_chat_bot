package com.project.template.module.chat.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.project.template.module.chat.pojo.entity.Friend;
import com.project.template.module.chat.pojo.vo.GroupMessage;
import com.project.template.module.chat.serivice.FriendService;
import com.project.template.module.chat.serivice.MessageInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final Random random = new Random();

    private static final String URL = "http://192.168.66.130:3000/send_group_msg";

    private final MessageInterface messageInterface;

    @Autowired
    public ChatController(MessageInterface messageInterface) {
        this.messageInterface = messageInterface;
    }

    @PostMapping("/receive")
    public void receive(@RequestBody JSONObject jsonObject) throws InterruptedException {
        String message = null;
        try {
            message = jsonObject.get("raw_message").toString();
        } catch (Exception e) {
            return;
        }
        log.info("receive message: {}", message);
        if (!message.contains("[CQ:at,qq=3938875617]")) {
            return;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "application/json;charset=UTF-8");
        int randomNumber = random.nextInt(3001) + 2000;
        // 沉睡线程，降低回复速度
        Thread.sleep(randomNumber);
        String jsonString = null;
        if (message.contains("介绍-")) {
            jsonString = messageInterface.searchFriend(message);
        } else if (message.contains("添加-")) {
            messageInterface.addFriend(message);
        } else if (message.contains("哔哩哔哩热搜")){
//            jsonString = messageInterface.bilibiliHot();
            jsonString = "受账号风控影响，该功能已禁用";
        }
        if (StringUtils.isNotBlank(jsonString)) {
            int length = jsonString.length();
            if (length > 50){
                jsonString = jsonString.substring(0, 50);
            }
            jsonString += "\n！由于消息限制只能显示50字符！";
            HttpEntity<String> formEntity = new HttpEntity<>(jsonString, headers);
            restTemplate.postForObject(URL, formEntity, String.class);
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
}
