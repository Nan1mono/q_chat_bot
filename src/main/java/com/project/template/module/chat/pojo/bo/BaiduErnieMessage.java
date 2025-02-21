package com.project.template.module.chat.pojo.bo;

import com.google.common.collect.Lists;
import com.project.template.module.chat.pojo.vo.GroupMessage;
import com.project.template.module.chat.pojo.vo.Message;
import com.project.template.module.chat.pojo.vo.MessageData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BaiduErnieMessage {

    private Double temperature;

    private Double top_p;

    private Double penalty_score;

    private Boolean enable_system_memory;

    private Boolean disable_search;

    private Boolean enable_citation;

    private List<ErnieMessage> messages;

    @Data
    @Accessors(chain = true)
    public static class ErnieMessage{

        private String role;

        private String content;

    }

    public static GroupMessage packageMessage(String response){
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroup_id("953136144");
        Message message = new Message();
        message.setType("text");
        MessageData messageData = new MessageData();
        messageData.setText(response);
        message.setData(messageData);
        groupMessage.setMessage(Lists.newArrayList(message));
        return groupMessage;
    }

}
