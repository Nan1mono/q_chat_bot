package com.project.bot.module.chat.pojo.vo.qq.group;

import com.project.bot.module.chat.pojo.vo.qq.QBaseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class QGroupMessage extends QBaseMessage {

    private String group_id;

    private List<com.project.bot.module.chat.pojo.vo.qq.QMessage> QMessage;

    public QGroupMessage toGroup(String groupId){
        return this.setGroup_id(groupId);
    }

}
