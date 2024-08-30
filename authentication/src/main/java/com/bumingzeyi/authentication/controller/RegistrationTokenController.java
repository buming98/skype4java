package com.bumingzeyi.authentication.controller;

import com.alibaba.fastjson.JSON;
import com.bumingzeyi.authentication.service.RegistrationTokenService;
import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author :BuMing
 * @date : 2022-04-03 18:02
 */
@RestController
@RequestMapping("registrationToken")
@Slf4j
public class RegistrationTokenController {

    @Resource
    private RegistrationTokenService registrationTokenService;


    @GetMapping("get")
    public RegistrationTokenRespDto get(){
        log.info("开始接受到请求registrationToken");
        RegistrationTokenRespDto registrationToken = registrationTokenService.getRegistrationToken();
        log.info("结束接受到请求registrationToken, resp:{}", JSON.toJSONString(registrationToken));
        return registrationToken;
    }

    @GetMapping("reGet")
    public RegistrationTokenRespDto reGet(){
        log.info("开始接受到重新请求registrationToken");
        RegistrationTokenRespDto registrationToken = registrationTokenService.reGetRegistrationToken();
        log.info("结束接受到重新请求registrationToken, resp:{}", JSON.toJSONString(registrationToken));
        return registrationToken;
    }

}
