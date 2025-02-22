package com.project.bot.module.chat.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class GroupMessage {

    private String group_id;

    private List<Message> message;

}
