package com.bumingzeyi.send.service;

import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;

/**
 * @author :BuMing
 * @date : 2022-03-28 22:02
 */
public interface SendTextService {

    void sendText(RegistrationTokenRespDto registrationToken, String sendObj, String content);

}
