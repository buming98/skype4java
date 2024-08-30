package com.bumingzeyi.events.listener;

import com.alibaba.fastjson.JSON;
import com.bumingzeyi.authentication.service.RegistrationTokenService;
import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;
import com.bumingzeyi.common.dto.events.EventsRespDto;
import com.bumingzeyi.events.service.EventsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: Jace
 * @date: 2022/4/1 9:41
 */
@Component
@Slf4j
public class EventsListener implements ApplicationRunner {

    @Resource
    private EventsService eventsService;
    @Resource
    private RegistrationTokenService registrationTokenService;
    @Resource
    private AmqpTemplate rabbitTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("***********程序启动skype事件监听开始***********");
        // 开启线程处理
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RegistrationTokenRespDto registrationToken = registrationTokenService.getRegistrationToken();
                int count = 0;
                while (true){
                    count++;
                    log.info("开始请求events,请求次数:{}" ,count);
                    EventsRespDto events = null;
                    try {
                        events = eventsService.getEvents(registrationToken);
                    } catch (Exception e) {
                        log.error("请求events失败,请求次数:{}", count, e);
                    }
                    log.info("结束请求events,请求次数:{}" ,count);
                    if(events != null){
                        rabbitTemplate.convertAndSend("eventsExchange", "events", JSON.toJSONString(events));
                    }
                }
            }
        });
        thread.setName("EventsListenerThread");
        thread.start();
    }

}
