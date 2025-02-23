package com.project.bot.module.chat.core.exception;

import lombok.Getter;

@Getter
public class ChatCoreException extends RuntimeException {

    public ChatCoreException(Exception e) {
        super(e);
    }

}
