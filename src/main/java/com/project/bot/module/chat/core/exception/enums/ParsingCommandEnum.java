package com.project.bot.module.chat.core.exception.enums;

import lombok.Getter;

@Getter
public enum ParsingCommandEnum {

    SUCCESS(1000,"PCE-1000", "成功"),
    ERROR(2000,"PCE-2000", "解析命令失败"),
    NOT_FOUND(2001,"PCE-2001", "未找到命令"),
    ;

    private final Integer codeNo;

    private final String code;

    private final String message;

    ParsingCommandEnum(Integer codeNo, String code, String message) {
        this.codeNo = codeNo;
        this.code = code;
        this.message = message;
    }
}
