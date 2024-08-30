package com.bumingzeyi.authentication.service;

import com.bumingzeyi.common.dto.LoginToken.LoginTokenRespDto;

/**
 * @author :BuMing
 * @date : 2022-03-27 21:33
 */
public interface LoginService {

    /**
     * 登录方法
     * @return 账号密码登录成功后的token
     */
    LoginTokenRespDto login();

    /**
     * token过期,清除缓存,重新登录
     * @return
     */
    LoginTokenRespDto reLogin();

}
