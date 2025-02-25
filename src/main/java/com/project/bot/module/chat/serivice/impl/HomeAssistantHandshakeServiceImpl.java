package com.project.bot.module.chat.serivice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.bot.module.chat.mapper.HomeAssistantHandshakeMapper;
import com.project.bot.module.chat.pojo.entity.HomeAssistantHandshake;
import com.project.bot.module.chat.serivice.HomeAssistantHandshakeService;
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
public class HomeAssistantHandshakeServiceImpl extends ServiceImpl<HomeAssistantHandshakeMapper, HomeAssistantHandshake> implements HomeAssistantHandshakeService {

    @Override
    public HomeAssistantHandshake getBySessionId(String sessionId) {
        return this.lambdaQuery().eq(HomeAssistantHandshake::getSessionId, sessionId).one();
    }
}
