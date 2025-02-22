package com.project.bot.module.chat.serivice.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.project.bot.module.chat.pojo.bo.BaiduErnieMessage;
import com.project.bot.module.chat.serivice.AiChatInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class BaiduErnieImpl implements AiChatInterface {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${baidu.chat.api-key:replace-your-api-key}")
    private String apiKey;

    @Value("${baidu.chat.secret-key:replace-your-secret-key}")
    private String secretKey;

    @Override
    public String chat(String message) {
        String regex = "ai-(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        if (!matcher.find()) {
            return null;
        }
        message = matcher.group(1);
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=%s";
        BaiduErnieMessage.ErnieMessage ernieMessage = new BaiduErnieMessage.ErnieMessage().setRole("user").setContent(message);
        BaiduErnieMessage baiduErnieMessage = new BaiduErnieMessage()
                .setTemperature(0.95)
                .setTop_p(0.8)
                .setPenalty_score(1.0)
                .setEnable_system_memory(false)
                .setDisable_search(false)
                .setEnable_citation(false)
                .setMessages(Lists.newArrayList(ernieMessage));
        String accessToken = getAccessToken();
        log.info("accessToken: {}", accessToken);
        url = String.format(url, accessToken);
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<BaiduErnieMessage> httpEntity = new HttpEntity<>(baiduErnieMessage, headers);
        JSONObject jsonObject = JSON.parseObject(restTemplate.postForObject(url, httpEntity, String.class));
        if (ObjectUtils.isEmpty(jsonObject)) {
            return null;
        }
        log.info("baidu ernie response: {}", jsonObject);
        return JSON.toJSONString(BaiduErnieMessage.packageMessage(jsonObject.getString("result")));
    }

    @Override
    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String url = String.format("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s", apiKey, secretKey);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        JSONObject jsonObject = JSON.parseObject(restTemplate.postForObject(url, httpEntity, String.class));
        if (ObjectUtils.isEmpty(jsonObject)) {
            return null;
        }
        return jsonObject.getString("access_token");
    }

}
