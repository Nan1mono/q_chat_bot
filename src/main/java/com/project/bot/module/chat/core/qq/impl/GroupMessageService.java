package com.project.bot.module.chat.core.qq.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.project.bot.common.util.qq.QUtils;
import com.project.bot.module.chat.core.ernie.BaiduErnieService;
import com.project.bot.module.chat.core.qq.QMessageInterface;
import com.project.bot.module.chat.pojo.entity.Friend;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import com.project.bot.module.chat.serivice.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMessageService implements QMessageInterface {

    private final FriendService friendService;

    private final BaiduErnieService baiduErnieService;

    @Autowired
    public GroupMessageService(FriendService friendService,
                               BaiduErnieService baiduErnieService) {
        this.friendService = friendService;
        this.baiduErnieService = baiduErnieService;
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
        }else {
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

}
