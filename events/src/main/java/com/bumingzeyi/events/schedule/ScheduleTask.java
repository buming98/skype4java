package com.bumingzeyi.events.schedule;

import com.bumingzeyi.authentication.service.RegistrationTokenService;
import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;
import com.bumingzeyi.events.service.EventsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @author :BuMing
 * @date : 2022-04-10 18:28
 */

@Configuration
@EnableScheduling
@Slf4j
public class ScheduleTask {

    @Resource
    private EventsService eventsService;
    @Resource
    private RegistrationTokenService registrationTokenService;

    @Scheduled(cron = "0 0 4 * * ?", zone = "GMT+8")
    public void reSubscribeAndRegistrationEndpoints(){
        log.info("开始重新刷新端点事件");
        RegistrationTokenRespDto registrationToken = registrationTokenService.getRegistrationToken();
        eventsService.registrationEndpoints(registrationToken);
        for(int i = 0; i < 2; i++){
            eventsService.subscribe(registrationToken);
        }
    }

}