package com.project.bot.module.chat.serivice.impl;

import com.project.bot.module.chat.pojo.entity.Friend;
import com.project.bot.module.chat.mapper.FriendMapper;
import com.project.bot.module.chat.serivice.FriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-02-19
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    @Override
    public List<Friend> getFriendByName(String name) {
        return this.lambdaQuery()
                .eq(Friend::getSimpleSpelling, name)
                .or().eq(Friend::getFullName, name)
                .or().eq(Friend::getNickName1, name)
                .or().eq(Friend::getNickName2, name)
                .or().eq(Friend::getNickName3, name)
                .list();
    }

    @Override
    public boolean save(String simpleName, String fullName, String nickName1, String nickName2, String nickName3) {
        Friend friend = new Friend().setSimpleSpelling(simpleName)
                .setFullName(fullName)
                .setNickName1(nickName1)
                .setNickName2(nickName2)
                .setNickName3(nickName3)
                .setStatus(1);
        return this.save(friend);
    }
}
