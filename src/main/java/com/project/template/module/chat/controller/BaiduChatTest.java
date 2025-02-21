package com.project.template.module.chat.controller;

import com.alibaba.fastjson2.JSON;
import okhttp3.*;

import java.io.IOException;

public class BaiduChatTest {

    public static final String API_KEY = "1";
    public static final String SECRET_KEY = "1";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static void main(String[] args) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"temperature\":0.95," +
                "\"top_p\":0.8," +
                "\"penalty_score\":1," +
                "\"enable_system_memory\":false," +
                "\"disable_search\":false," +
                "\"enable_citation\":false," +
                "\"messages\":[ {\"role\": \"user\",\"content\": \"你好\"}]"+
                "}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());

    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return JSON.parseObject(response.body().string()).getString("access_token");
    }

}
