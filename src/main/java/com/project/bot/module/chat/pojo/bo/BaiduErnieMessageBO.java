package com.project.bot.module.chat.pojo.bo;

import com.google.common.collect.Lists;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import com.project.bot.module.chat.pojo.vo.qq.QMessage;
import com.project.bot.module.chat.pojo.vo.qq.QMessageData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BaiduErnieMessageBO {

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

    public static QGroupMessage packageMessage(String response){
        QGroupMessage QGroupMessage = new QGroupMessage();
        QGroupMessage.setGroup_id("953136144");
        QMessage QMessage = new QMessage();
        QMessage.setType("text");
        QMessageData QMessageData = new QMessageData();
        QMessageData.setText(response);
        QMessage.setData(QMessageData);
        QGroupMessage.setMessage(Lists.newArrayList(QMessage));
        return QGroupMessage;
    }

}
