package com.project.bot.module.chat.serivice;

import com.project.bot.module.chat.pojo.entity.Friend;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lee
 * @since 2025-02-19
 */
public interface FriendService extends IService<Friend> {

    List<Friend> getFriendByName(String name);

    boolean save(String simpleName, String fullName, String nickName1, String nickName2, String nickName3);

}
