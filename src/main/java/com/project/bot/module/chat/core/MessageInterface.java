package com.project.bot.module.chat.core;

import com.project.bot.module.chat.core.exception.ParsingCommandException;
import com.project.bot.module.chat.core.exception.enums.ParsingCommandEnum;
import com.project.bot.module.chat.pojo.vo.BotMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface MessageInterface {

    default String parsingCommand(String message, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        if (!matcher.find()){
            throw new ParsingCommandException(ParsingCommandEnum.NOT_FOUND);
        }
        return matcher.group(1);
    }

    BotMessage getFriendByName(Object message);

    BotMessage saveFriend(Object message);

    BotMessage helpSaveFriend(Object message);

    BotMessage chatWithAi(Object message);

    BotMessage getDeviceList(Object message);

    BotMessage sendDeviceList(Object message, String chatId);

    /**
     * 提供一个手动发送消息的接口
     * @param message 消息
     */
    default void sendMessage(BotMessage message){

    }

}
