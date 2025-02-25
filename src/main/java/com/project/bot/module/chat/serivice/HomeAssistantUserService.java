package com.project.bot.module.chat.serivice;

import com.project.bot.module.chat.pojo.entity.HomeAssistantUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lee
 * @since 2025-02-25
 */
public interface HomeAssistantUserService extends IService<HomeAssistantUser> {

    HomeAssistantUser getByTelegramId(Long telegramId);

    HomeAssistantUser getByQqId(Long qqId);

}
