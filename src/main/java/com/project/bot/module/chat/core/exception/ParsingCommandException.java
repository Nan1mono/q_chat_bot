package com.project.bot.module.chat.core.exception;

import com.project.bot.module.chat.core.exception.enums.ParsingCommandEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ParsingCommandException extends RuntimeException {

    private final Integer code;

    public ParsingCommandException(ParsingCommandEnum pce) {
        super(String.format("%s-%s", pce.getCode(), pce.getMessage()));
        this.code = pce.getCodeNo();
    }

    public ParsingCommandException(Exception e) {
        super(e.getMessage());
        this.code = ParsingCommandEnum.ERROR.getCodeNo();
    }


}
