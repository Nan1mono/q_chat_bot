package com.project.bot.module.chat.serivice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.bot.module.chat.pojo.entity.HomeAssistantHandshake;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lee
 * @since 2025-02-25
 */
public interface HomeAssistantHandshakeService extends IService<HomeAssistantHandshake> {

    HomeAssistantHandshake getBySessionId(String sessionId);

}
