package com.project.template.module.chat.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;
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
 * @since 2025-02-19
 */
@Getter
@Setter
@ToString
@TableName("friend")
@Schema(name = "Friend", description = "")
public class Friend extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uid
     */
    @TableField("uid")
    @Schema(description = "uid")
    private String uid;

    /**
     * uni
     */
    @TableField("uin")
    @Schema(description = "uni")
    private Long uin;

    /**
     * 姓名拼音简拼
     */
    @TableField("simple_spelling")
    @Schema(description = "姓名拼音简拼")
    private String simpleSpelling;

    /**
     * 全名
     */
    @TableField("full_name")
    @Schema(description = "全名")
    private String fullName;

    /**
     * 昵称1
     */
    @TableField("nick_name1")
    @Schema(description = "昵称1")
    private String nickName1;

    /**
     * 昵称2
     */
    @TableField("nick_name2")
    @Schema(description = "昵称2")
    private String nickName2;

    /**
     * 昵称3
     */
    @TableField("nick_name3")
    @Schema(description = "昵称3")
    private String nickName3;

    /**
     * 状态 1启用 0停用
     */
    @TableField("status")
    @Schema(description = "状态 1启用 0停用")
    private Integer status;
}
