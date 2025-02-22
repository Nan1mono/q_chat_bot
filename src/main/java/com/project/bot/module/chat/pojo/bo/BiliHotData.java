package com.project.bot.module.chat.pojo.bo;

import com.google.common.collect.Lists;
import com.project.bot.module.base.entity.BaseEntity;
import com.project.bot.module.chat.pojo.vo.GroupMessage;
import com.project.bot.module.chat.pojo.vo.Message;
import com.project.bot.module.chat.pojo.vo.MessageData;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Data
public class BiliHotData extends BaseEntity {

    private String author;

    private String cover;

    private String desc;

    private String title;

    private String url;


    public static GroupMessage packageBiliHotMessage(List<BiliHotData> biliHotDataList) {
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroup_id("953136144");
        Message message = new Message();
        message.setType("text");
        MessageData messageData = new MessageData();
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isEmpty(biliHotDataList)) {
            return null;
        }
        for (BiliHotData biliHotData : biliHotDataList) {
            String format = String.format("""
                    标题: %s
                    作者：%s
                    url: %s
                    """, getOrDefault(biliHotData.getTitle()), getOrDefault(biliHotData.getAuthor()), getOrDefault(biliHotData.getUrl()));
            builder.append(format).append("\n");
        }
        messageData.setText(builder.toString());
        message.setData(messageData);
        groupMessage.setMessage(Lists.newArrayList(message));
        return groupMessage;
    }

}
