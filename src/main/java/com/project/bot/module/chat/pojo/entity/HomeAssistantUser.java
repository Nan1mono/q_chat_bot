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
@TableName("home_assistant_user")
@Schema(name = "HomeAssistantUser", description = "")
public class HomeAssistantUser extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息id
     */
    @TableField("message_id")
    @Schema(description = "消息id")
    private Long messageId;

    /**
     * QQ账号
     */
    @TableField("qq_id")
    @Schema(description = "QQ账号")
    private Long qqId;

    /**
     * 电报id
     */
    @TableField("telegram_id")
    @Schema(description = "电报id")
    private Long telegramId;

    /**
     * HA设备主要识别标识，由HA生成
     */
    @TableField("primary_config_entry")
    @Schema(description = "HA设备主要识别标识，由HA生成")
    private String primaryConfigEntry;

    /**
     * 状态 1启用 0停用
     */
    @TableField("status")
    @Schema(description = "状态 1启用 0停用")
    private Integer status;
}
