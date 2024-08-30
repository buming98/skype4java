package com.bumingzeyi.authentication.service;

import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;

/**
 * @author :BuMing
 * @date : 2022-03-28 21:23
 */
public interface RegistrationTokenService {

    /**
     * 获取注册token
     * @return
     */
    RegistrationTokenRespDto getRegistrationToken();

    /**
     * 重新获取注册token
     * @return
     */
    RegistrationTokenRespDto reGetRegistrationToken();

}
