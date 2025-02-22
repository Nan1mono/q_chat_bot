package com.project.bot.module.chat.pojo.bo;

import com.google.common.collect.Lists;
import com.project.bot.module.base.entity.BaseEntity;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import com.project.bot.module.chat.pojo.vo.qq.QMessage;
import com.project.bot.module.chat.pojo.vo.qq.QMessageData;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Data
public class BiliHotDataBO extends BaseEntity {

    private String author;

    private String cover;

    private String desc;

    private String title;

    private String url;


    public static QGroupMessage packageBiliHotMessage(List<BiliHotDataBO> biliHotDataBOList) {
        QGroupMessage QGroupMessage = new QGroupMessage();
        QGroupMessage.setGroup_id("953136144");
        QMessage QMessage = new QMessage();
        QMessage.setType("text");
        QMessageData QMessageData = new QMessageData();
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isEmpty(biliHotDataBOList)) {
            return null;
        }
        for (BiliHotDataBO biliHotDataBO : biliHotDataBOList) {
            String format = String.format("""
                    标题: %s
                    作者：%s
                    url: %s
                    """, getOrDefault(biliHotDataBO.getTitle()), getOrDefault(biliHotDataBO.getAuthor()), getOrDefault(biliHotDataBO.getUrl()));
            builder.append(format).append("\n");
        }
        QMessageData.setText(builder.toString());
        QMessage.setData(QMessageData);
        QGroupMessage.setMessage(Lists.newArrayList(QMessage));
        return QGroupMessage;
    }

}
