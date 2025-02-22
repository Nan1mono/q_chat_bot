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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

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
@Accessors(chain = true)
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

    public static String convert2Msg(List<Friend> friendList) {
        if (CollectionUtils.isEmpty(friendList)) {
            return "❌没有记录请先添加哦";
        }
        List<String> list = Lists.newArrayList();
        for (Friend friend : friendList) {
            list.add(convert2Msg(friend));
        }
        return String.join("\r\n", list);
    }

    public static String convert2Msg(Friend friend) {
        return String.format("""
                        姓名：%s
                        简拼：%s
                        别名：%s,%s,%s
                        """, getOrDefault(friend.getFullName()), getOrDefault(friend.getSimpleSpelling()),
                getOrDefault(friend.getNickName1()), getOrDefault(friend.getNickName2()), getOrDefault(friend.getNickName3()));
    }
}
