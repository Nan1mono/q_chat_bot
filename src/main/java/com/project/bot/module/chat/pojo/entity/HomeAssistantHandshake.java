package com.project.bot.module.chat.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.bot.module.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lee
 * @since 2025-02-25
 */
@Getter
@Setter
@ToString
@TableName("home_assistant_handshake")
@Schema(name = "HomeAssistantHandshake", description = "")
@Accessors(chain = true)
public class HomeAssistantHandshake extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("payload_id")
    private String payloadId;

    @TableField("chat_id")
    private String chatId;

    @TableField("home_assistant_user_id")
    private Long homeAssistantUserId;

    /**
     * 接受方式：telegram/qq
     */
    @TableField("session_method")
    @Schema(description = "会话方式：telegram/qq")
    private String sessionMethod;

    /**
     * 状态 1启用 0停用
     */
    @TableField("status")
    @Schema(description = "状态 1启用 0停用")
    private Integer status;
}
