package com.bumingzeyi.authentication.service;

import com.bumingzeyi.common.dto.SkypeToken.SkypeTokenRespDto;

/**
 * @author :BuMing
 * @date : 2022-03-28 20:54
 */
public interface SkypeTokenService {

    /**
     * 获取SkypeToken
     * @return
     */
    SkypeTokenRespDto getSkypeToken();

    /**
     * 重新获取SkypeToken
     * @return
     */
    SkypeTokenRespDto reGetSkypeToken();

}
