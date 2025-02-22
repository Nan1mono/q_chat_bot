package com.project.bot.module.chat.core.ernie;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.project.bot.module.chat.core.AiChatInterface;
import com.project.bot.module.chat.pojo.bo.BaiduErnieMessageBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class BaiduErnieService implements AiChatInterface {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${chat.baidu.api-key:replace-your-api-key}")
    private String apiKey;

    @Value("${chat.baidu.secret-key:replace-your-secret-key}")
    private String secretKey;

    @Override
    public String chat(String message) {
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=%s";
        BaiduErnieMessageBO.ErnieMessage ernieMessage = new BaiduErnieMessageBO.ErnieMessage().setRole("user").setContent(message);
        BaiduErnieMessageBO baiduErnieMessageBO = new BaiduErnieMessageBO()
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
        HttpEntity<BaiduErnieMessageBO> httpEntity = new HttpEntity<>(baiduErnieMessageBO, headers);
        JSONObject jsonObject = JSON.parseObject(restTemplate.postForObject(url, httpEntity, String.class));
        if (ObjectUtils.isEmpty(jsonObject)) {
            return null;
        }
        log.info("baidu ernie response: {}", jsonObject);
        return jsonObject.getString("result");
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
