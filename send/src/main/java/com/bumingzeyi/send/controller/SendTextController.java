package com.bumingzeyi.send.controller;

import com.bumingzeyi.authentication.service.RegistrationTokenService;
import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;
import com.bumingzeyi.common.dto.sendtext.SendTextReqDto;
import com.bumingzeyi.send.service.SendTextService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author :BuMing
 * @date : 2022-04-03 18:42
 */
@RestController
@RequestMapping("/sendText")
public class SendTextController {

    @Resource
    private SendTextService sendTextService;
    @Resource
    private RegistrationTokenService registrationTokenService;

    @PostMapping
    public String sendText(@RequestBody SendTextReqDto sendTextReqDto){
        RegistrationTokenRespDto registrationToken = registrationTokenService.getRegistrationToken();
        sendTextService.sendText(registrationToken, sendTextReqDto.getSendObj(), sendTextReqDto.getContent());
        return "success";
    }

}
