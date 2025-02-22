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
        String regex = "添加-(.*)";
        JSONObject jsonObject = JSON.parseObject(message.toString());
        String rawMessage = jsonObject.getString(QUtils.RAW_MESSAGE);
        String text = parsingCommand(rawMessage, regex);
        String[] split = text.split(",");
        if (friendService.save(split[0], split[1], split[2], split[3], split[4])) {
            return buildQGroupMsg("添加成功").toGroup(QUtils.getGroupId(jsonObject));
        }else {
            return buildQGroupMsg("添加失败").toGroup(QUtils.getGroupId(jsonObject));
        }
    }

    @Override
    public QGroupMessage helpSaveFriend(Object message) {
        return buildQGroupMsg("""
                🫡
                按照如下格式输入：
                添加-简称,姓名,昵称1,昵称2,昵称3
                即可添加信息捏
                ❤️
                """).toGroup(QUtils.getGroupId(JSON.parseObject(message.toString())));
    }

}
