package com.project.bot.module.chat.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobTask {

    @Scheduled(cron = "0 0/5 * * * ?")
    public void job(){

    }

}
