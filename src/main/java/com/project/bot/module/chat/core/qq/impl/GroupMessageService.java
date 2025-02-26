package com.project.bot.module.chat.core.qq.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.project.bot.common.util.qq.QUtils;
import com.project.bot.module.chat.core.ernie.BaiduErnieService;
import com.project.bot.module.chat.core.qq.QMessageInterface;
import com.project.bot.module.chat.pojo.entity.Friend;
import com.project.bot.module.chat.pojo.entity.HomeAssistantUser;
import com.project.bot.module.chat.pojo.vo.BotMessage;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import com.project.bot.module.chat.serivice.FriendService;
import com.project.bot.module.chat.serivice.HomeAssistantUserService;
import com.project.bot.module.hs.HaSocketTemplate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GroupMessageService implements QMessageInterface {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${chat.napcat.client.group-url:you-client-url}")
    private String clientGroupUrl;

    private final FriendService friendService;

    private final BaiduErnieService baiduErnieService;

    private final HomeAssistantUserService homeAssistantUserService;

    private final HaSocketTemplate haSocketTemplate;

    @Autowired
    public GroupMessageService(FriendService friendService,
                               BaiduErnieService baiduErnieService,
                               HomeAssistantUserService homeAssistantUserService,
                               HaSocketTemplate haSocketTemplate) {
        this.friendService = friendService;
        this.baiduErnieService = baiduErnieService;
        this.homeAssistantUserService = homeAssistantUserService;
        this.haSocketTemplate = haSocketTemplate;
    }

    @Override
    public void sendMessage(BotMessage message) {
        if (ObjectUtils.isNotEmpty(message)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("content-type", "application/json;charset=UTF-8");
            HttpEntity<BotMessage> formEntity = new HttpEntity<>(message, headers);
            restTemplate.postForObject(clientGroupUrl, formEntity, String.class);
        }
    }

    @Override
    public QGroupMessage getFriendByName(Object message) {
        JSONObject jsonObject = JSON.parseObject(message.toString());
        String rawMessage = jsonObject.getString(QUtils.RAW_MESSAGE);
        String regex = "介绍-(.*)";
        String text = parsingCommand(rawMessage, regex);
        List<Friend> friendList = friendService.getFriendByName(text);
        String response = Friend.convert2Msg(friendList);
        return QGroupMessage.buildQGroupMsg(response).toGroup(QUtils.getGroupId(jsonObject));
    }

    @Override
    public QGroupMessage saveFriend(Object message) {
        String regex = "添加-(.*)";
        JSONObject jsonObject = JSON.parseObject(message.toString());
        String rawMessage = jsonObject.getString(QUtils.RAW_MESSAGE);
        String text = parsingCommand(rawMessage, regex);
        String[] split = text.split(",");
        if (friendService.save(split[0], split[1], split[2], split[3], split[4])) {
            return QGroupMessage.buildQGroupMsg("添加成功").toGroup(QUtils.getGroupId(jsonObject));
        } else {
            return QGroupMessage.buildQGroupMsg("添加失败").toGroup(QUtils.getGroupId(jsonObject));
        }
    }

    @Override
    public QGroupMessage helpSaveFriend(Object message) {
        return QGroupMessage.buildQGroupMsg("""
                🫡
                按照如下格式输入：
                添加-简称,姓名,昵称1,昵称2,昵称3
                即可添加信息捏
                ❤️
                """).toGroup(QUtils.getGroupId(JSON.parseObject(message.toString())));
    }

    @Override
    public QGroupMessage chatWithAi(Object message) {
        JSONObject jsonObject = JSON.parseObject(message.toString());
        String rawMessage = jsonObject.getString(QUtils.RAW_MESSAGE);
        rawMessage = rawMessage.replace(String.format(QUtils.AT_MESSAGE, QUtils.getQid(jsonObject)), "");
        String chat = baiduErnieService.chat(rawMessage);
        return QGroupMessage.buildQGroupMsg(chat).toGroup(QUtils.getGroupId(jsonObject));
    }

    @Override
    public QGroupMessage getDeviceList(Object message) {
        JSONObject jsonObject = JSON.parseObject(message.toString());
        String qid = jsonObject.getString("user_id");
        HomeAssistantUser assistantUser = homeAssistantUserService.getByQqId(Long.valueOf(qid));
        if (ObjectUtils.isEmpty(assistantUser)) {
            return QGroupMessage.buildQGroupMsg(String.format("%s 用户不支持", qid)).toGroup(QUtils.getGroupId(jsonObject));
        }
        // 将chatId记录为需要发送的群组id
        haSocketTemplate.searchDevice("qq", assistantUser.getId(), QUtils.getGroupId(jsonObject));
        return null;
    }

    @Override
    public BotMessage sendDeviceList(Object message, String groupId) {
        return QGroupMessage.buildQGroupMsg(message.toString()).toGroup(groupId);
    }

}
