package com.project.bot.module.chat.serivice.impl;

import com.project.bot.module.chat.pojo.entity.HomeAssistantUser;
import com.project.bot.module.chat.mapper.HomeAssistantUserMapper;
import com.project.bot.module.chat.serivice.HomeAssistantUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-02-25
 */
@Service
public class HomeAssistantUserServiceImpl extends ServiceImpl<HomeAssistantUserMapper, HomeAssistantUser> implements HomeAssistantUserService {


    @Override
    public HomeAssistantUser getByTelegramId(Long telegramId) {
        return this.lambdaQuery().eq(HomeAssistantUser::getTelegramId, telegramId).one();
    }

    @Override
    public HomeAssistantUser getByQqId(Long qqId) {
        return this.lambdaQuery().eq(HomeAssistantUser::getQqId, qqId).one();
    }
}
