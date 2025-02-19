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


    public static GroupMessage packageFriendMsg(List<Friend> friendList) {
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroup_id("953136144");
        Message msg = new Message();
        msg.setType("text");
        MessageData messageData = new MessageData();
        StringBuilder result = new StringBuilder();
        for (Friend friend : friendList){
            result.append(packageFriendText(friend));
        }
        messageData.setText(result.toString());
        msg.setData(messageData);
        groupMessage.setMessage(Lists.newArrayList(msg));
        return groupMessage;
    }

    private static String packageFriendText(Friend friend) {
        return String.format("""
                姓名：%s
                简拼：%s
                别名：%s,%s,%s
                """, getOrDefault(friend.getFullName()), getOrDefault(friend.getSimpleSpelling()),
                getOrDefault(friend.getNickName1()), getOrDefault(friend.getNickName2()), getOrDefault(friend.getNickName3()));
    }



    private static String getOrDefault(String val) {
        if (StringUtils.isBlank(val)){
            return "";
        }else {
            return val;
        }
    }

}
