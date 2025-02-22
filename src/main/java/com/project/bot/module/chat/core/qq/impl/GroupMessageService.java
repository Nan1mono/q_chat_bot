package com.project.bot.module.chat.core.qq.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.project.bot.common.util.qq.QUtils;
import com.project.bot.module.chat.core.qq.QMessageInterface;
import com.project.bot.module.chat.pojo.entity.Friend;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import com.project.bot.module.chat.serivice.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMessageService implements QMessageInterface {

    private final FriendService friendService;

    @Autowired
    public GroupMessageService(FriendService friendService) {
        this.friendService = friendService;
    }


    @Override
    public QGroupMessage getFriendByName(Object message) {
        JSONObject jsonObject = JSON.parseObject(message.toString());
        String rawMessage = jsonObject.getString(QUtils.RAW_MESSAGE);
        String regex = "介绍-(.*)";
        String text = parsingCommand(rawMessage, regex);
        List<Friend> friendList = friendService.getFriendByName(text);
        String response = Friend.convert2Msg(friendList);
        return buildQGroupMsg(response).toGroup(QUtils.getGroupId(jsonObject));
    }

    @Override
    public QGroupMessage saveFriend(Object message) {
        return null;
    }

}
