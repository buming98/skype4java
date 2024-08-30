package com.bumingzeyi.authentication.service.impl;

import com.alibaba.fastjson.JSON;
import com.bumingzeyi.authentication.constant.SkypeTokenConstant;
import com.bumingzeyi.common.dto.LoginToken.LoginTokenRespDto;
import com.bumingzeyi.common.dto.SkypeToken.SkypeTokenRespDto;
import com.bumingzeyi.authentication.service.LoginService;
import com.bumingzeyi.authentication.service.SkypeTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :BuMing
 * @date : 2022-03-28 20:56
 */
@Service
@Slf4j
public class SkypeTokenServiceImpl implements SkypeTokenService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private LoginService loginService;

    @Override
    @Cacheable(value = "skypeToken", key = "'skypeToken'")
    public SkypeTokenRespDto getSkypeToken() {
        LoginTokenRespDto loginTokenRespDto = loginService.login();
        String responseBody = requestEdge(loginTokenRespDto.getBody().getRequestSecurityTokenResponseCollection()
                .getRequestSecurityTokenResponse().getRequestedSecurityToken().getBinarySecurityToken());
        return JSON.parseObject(responseBody, SkypeTokenRespDto.class);
    }

    @Override
    @CachePut(value = "skypeToken", key = "'skypeToken'")
    public SkypeTokenRespDto reGetSkypeToken() {
        LoginTokenRespDto loginTokenRespDto = loginService.login();
        String responseBody = requestEdge(loginTokenRespDto.getBody().getRequestSecurityTokenResponseCollection()
                .getRequestSecurityTokenResponse().getRequestedSecurityToken().getBinarySecurityToken());
        return JSON.parseObject(responseBody, SkypeTokenRespDto.class);
    }

    public String requestEdge(String loginToken){
        // 请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 请求体
        Map<String, Object> params = new HashMap<>();
        params.put("partner", 999);
        params.put("access_token", loginToken);
        params.put("scopes", "client");
        // 请求HttpEntity
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(params), httpHeaders);
        // 发起请求
        ResponseEntity<String> responseEntity = null;
        try {
            log.info("开始请求edge, req:{}", JSON.toJSONString(httpEntity));
            responseEntity = restTemplate.postForEntity(SkypeTokenConstant.SKYPE_TOKEN_URL, httpEntity, String.class);
            log.info("结束请求edge, resp:{}", JSON.toJSONString(responseEntity));
        } catch (RestClientException e) {
            log.error("请求edge异常", e);
            if(e instanceof HttpClientErrorException && ((HttpClientErrorException) e).getStatusCode() == HttpStatus.UNAUTHORIZED){
                LoginTokenRespDto loginTokenRespDto = loginService.reLogin();
                return requestEdge(loginTokenRespDto.getBody().getRequestSecurityTokenResponseCollection()
                        .getRequestSecurityTokenResponse().getRequestedSecurityToken().getBinarySecurityToken());
            }
        }
        return responseEntity.getBody();
    }

}
