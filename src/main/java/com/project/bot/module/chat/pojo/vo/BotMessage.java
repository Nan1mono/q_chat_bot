package com.project.bot.module.chat.pojo.vo;


import com.alibaba.fastjson2.JSON;

public class BotMessage {

    public String toJsonString(){
        return JSON.toJSONString(this);
    }

}
