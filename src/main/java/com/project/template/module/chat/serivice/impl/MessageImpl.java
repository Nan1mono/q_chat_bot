package com.project.template.module.chat.serivice.impl;

import com.alibaba.fastjson2.JSON;
import com.project.template.module.chat.pojo.entity.Friend;
import com.project.template.module.chat.pojo.vo.GroupMessage;
import com.project.template.module.chat.serivice.FriendService;
import com.project.template.module.chat.serivice.MessageInterface;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MessageImpl implements MessageInterface {

    private final FriendService friendService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public MessageImpl(FriendService friendService) {
        this.friendService = friendService;
    }


    @Override
    public String searchFriend(String s) {
        String regex = "介绍-(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            String result = matcher.group(1);
            List<Friend> friendList = friendService.lambdaQuery()
                    .eq(Friend::getSimpleSpelling, result)
                    .or().eq(Friend::getFullName, result)
                    .or().eq(Friend::getNickName1, result)
                    .or().eq(Friend::getNickName2, result)
                    .or().eq(Friend::getNickName3, result)
                    .list();
            if (CollectionUtils.isNotEmpty(friendList)) {
                GroupMessage res = GroupMessage.packageFriendMsg(friendList);
                return JSON.toJSONString(res);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void addFriend(String s) {
        String regex = "添加-(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            String result = matcher.group(1);
            String[] split = result.split(",");
            List<String> list = Arrays.stream(split).toList();
            Friend friend = new Friend();
            friend.setSimpleSpelling(list.get(0));
            friend.setFullName(list.get(1));
            friend.setNickName1(list.get(2));
            friend.setNickName2(list.get(3));
            friend.setNickName3(list.get(4));
            friendService.save(friend);
        }
    }

    @Override
    public void bilibiliHot() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "application/json;charset=UTF-8");
        HttpEntity<String> formEntity = new HttpEntity<>(headers);
        restTemplate.getForObject("https://api-hot.imsyy.top/bilibili?cache=true", formEntity);
    }

}
