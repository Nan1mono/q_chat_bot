package com.project.bot.module.chat.core.qq;

import com.google.common.collect.Lists;
import com.project.bot.module.chat.core.MessageInterface;
import com.project.bot.module.chat.pojo.vo.qq.QMessage;
import com.project.bot.module.chat.pojo.vo.qq.QMessageData;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import org.apache.commons.lang3.StringUtils;

public interface QMessageInterface extends MessageInterface {

    /**
     * 构建QQ群组消息
     *
     * @param message 消息
     * @return {@link QGroupMessage }
     */
    default QGroupMessage buildQGroupMsg(String message) {
        QGroupMessage QGroupMessage = new QGroupMessage();
        QMessage msg = new QMessage();
        msg.setType("text");
        QMessageData QMessageData = new QMessageData();
        QMessageData.setText(message);
        msg.setData(QMessageData);
        QGroupMessage.setMessage(Lists.newArrayList(msg));
        return QGroupMessage;
    }

    /**
     * 构建具有大小限制的QQ群组消息，部分场景下可以有效避免QQ风控
     *
     * @param message 消息
     * @param size    大小
     * @return {@link QGroupMessage }
     */
    default QGroupMessage buildQGroupMsg(String message, int size) {
        if (StringUtils.isNotBlank(message) && message.length() >= size) {
            message = message.substring(0, size);
            message += "\n！由于消息限制只能显示50字符！";
        }
        return this.buildQGroupMsg(message);
    }

}
