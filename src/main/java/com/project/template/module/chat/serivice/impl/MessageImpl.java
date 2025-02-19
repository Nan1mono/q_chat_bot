package com.project.template.module.chat.serivice.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.project.template.module.chat.pojo.bo.BiliHotData;
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
import org.springframework.web.client.RestClientException;
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
                GroupMessage res = Friend.packageFriendMsg(friendList);
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
    public String bilibiliHot() {
        List<BiliHotData> biliHotDataList = Lists.newArrayList();
        JSONArray jsonArray = null;
        try {
            JSONObject response = JSON.parseObject(restTemplate.getForObject("https://api-hot.imsyy.top/bilibili?cache=true", String.class));
            jsonArray = response.getJSONArray("data");
        } catch (RestClientException e) {
            return null;
        }
        int i = 1;
        if (CollectionUtils.isNotEmpty(jsonArray)) {
            for (Object jsonObject : jsonArray) {
                if (i >= 6){
                    break;
                }
                JSONObject data = JSON.parseObject(jsonObject.toString());
                BiliHotData biliHotData = new BiliHotData();
                biliHotData.setAuthor(data.getString("author"));
                biliHotData.setCover(data.getString("cover"));
                biliHotData.setDesc(data.getString("desc"));
                biliHotData.setTitle(data.getString("title"));
                biliHotData.setUrl(data.getString("url"));
                biliHotDataList.add(biliHotData);
                i++;
            }
        }
        return JSON.toJSONString(BiliHotData.packageBiliHotMessage(biliHotDataList));
    }

}
