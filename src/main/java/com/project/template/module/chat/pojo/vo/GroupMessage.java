package com.project.template.module.chat.pojo.vo;

import com.google.common.collect.Lists;
import com.project.template.module.chat.pojo.entity.Friend;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class GroupMessage {

    private String group_id;

    private List<Message> message;




}
