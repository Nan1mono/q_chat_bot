package com.project.bot.module.ha;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.project.bot.module.chat.pojo.entity.HomeAssistantUser;
import com.project.bot.module.chat.serivice.HomeAssistantUserService;
import com.project.bot.module.ha.core.sender.HaWebSocketSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class HaSocketTemplate {

    @Value("${home-assistant.token}")
    private String token;

    private final HaWebSocketSender haWebSocketSender;

    private final HomeAssistantUserService homeAssistantUserService;

    @Autowired
    public HaSocketTemplate(HaWebSocketSender haWebSocketSender,
                            HomeAssistantUserService homeAssistantUserService) {
        this.haWebSocketSender = haWebSocketSender;
        this.homeAssistantUserService = homeAssistantUserService;
    }

    public void auth() {
        String message = sendAuthMessage(token);
        log.info("auth message: {}", message);
        haWebSocketSender.sendToWebSocket(message, null, null, null);
    }

    public void searchDevice(String sessionMethod, Long homeAssistantUserId, String chatId) {
        haWebSocketSender.sendToWebSocket(JSON.toJSONString(getDeviceList()), sessionMethod, homeAssistantUserId, chatId);
    }

    public String filterDeviceListFromTelegram(String message, Long telegramId) {
        HomeAssistantUser homeAssistantUser = homeAssistantUserService.getByTelegramId(telegramId);
        return this.filterDeviceList(message, homeAssistantUser.getPrimaryConfigEntry());
    }

    public String filterDeviceListFromQq(String message, Long qqId) {
        HomeAssistantUser homeAssistantUser = homeAssistantUserService.getByQqId(qqId);
        return this.filterDeviceList(message, homeAssistantUser.getPrimaryConfigEntry());
    }

    public String filterDeviceList(String message, String primaryConfigEntry) {
        JSONObject jsonObject = JSON.parseObject(message);
        List<Object> list = jsonObject.getJSONArray("result").stream().filter(t -> JSON.parseObject(t.toString()).getString("primary_config_entry").equals(primaryConfigEntry)).toList();
        log.info("{}", list);
        List<String> result = Lists.newArrayList();
        for (Object t : list) {
            JSONObject device = JSON.parseObject(t.toString());
            result.add(device.getString("name"));
        }
        String response = String.format("""
                🤖目前您的智能资产如下：
                
                %s
                """, String.join("\r\n", result));
        log.info("{}", response);
        return response;
    }

    protected static String sendAuthMessage(String token) {
        JSONObject message = new JSONObject();
        message.put("type", "auth");
        message.put("access_token", token);
        return JSON.toJSONString(message);
    }

    protected static String subscribeToStates(String messageId) {
        JSONObject message = new JSONObject();
        message.put("id", messageId);
        message.put("type", "subscribe_events");
        message.put("event_type", "state_changed");
        return JSON.toJSONString(message);
    }

    protected static JSONObject getDeviceList() {
        JSONObject subscribeMessage = new JSONObject();
        subscribeMessage.put("id", System.currentTimeMillis());
        subscribeMessage.put("type", "config/device_registry/list");
        return subscribeMessage;
    }

}
