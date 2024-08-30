package com.bumingzeyi.authentication.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.bumingzeyi.authentication.constant.RegistrationTokenConstant;
import com.bumingzeyi.authentication.service.RegistrationTokenService;
import com.bumingzeyi.authentication.service.SkypeTokenService;
import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;
import com.bumingzeyi.common.dto.SkypeToken.SkypeTokenRespDto;
import com.bumingzeyi.common.utils.SHAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * @author :BuMing
 * @date : 2022-03-28 21:26
 */
@Service
@Slf4j
public class RegistrationTokenServiceImpl implements RegistrationTokenService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private SkypeTokenService skypeTokenService;

    @Override
    @Cacheable(value = "registrationToken", key = "'registrationToken'")
    public RegistrationTokenRespDto getRegistrationToken() {
        return baseGetRegistrationToken();
    }

    @Override
    @CachePut(value = "registrationToken", key = "'registrationToken'")
    public RegistrationTokenRespDto reGetRegistrationToken() {
        return baseGetRegistrationToken();
    }


    /**
     * 请求Endpoints API
     * @param skypeToken
     * @return
     */
    public ResponseEntity<String> requestEndpoints(String skypeToken, String url){
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        long  secs = System.currentTimeMillis()/1000;
        String sha256 = SHAUtils.SHA256(secs+ RegistrationTokenConstant.APP_ID+RegistrationTokenConstant.MAC256_KEY);
        headers.set("LockAndKey", String.format("appId=msmsgs@msnmsgr.com; time=%s; lockAndKeyResponse=%s", secs, sha256));
        headers.set("Authentication", "skypetoken=" + skypeToken);
        //headers.set("BehaviorOverride", "redirectAs404");
        // 请求体
        HashMap<String, String> map = new HashMap<>();
        map.put("endpointFeatures","Agent");
        // 请求HttpEntity
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(map), headers);
        // 发起请求
        ResponseEntity<String> responseEntity = null;
        try {
            log.info("开始请求注册端点API, req:{}", JSON.toJSONString(httpEntity));
            responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
            log.info("结束请求注册端点API, resp:{}", JSON.toJSONString(responseEntity));
        } catch (RestClientException e) {
            log.error("请求注册端点API异常", e);
            if(e instanceof HttpClientErrorException){
                HttpClientErrorException exp = (HttpClientErrorException) e;
                if(exp.getStatusCode() == HttpStatus.UNAUTHORIZED){
                    SkypeTokenRespDto skypeTokenRespDto = skypeTokenService.reGetSkypeToken();
                    return requestEndpoints(skypeTokenRespDto.getSkypeToken(), RegistrationTokenConstant.REGISTRATION_TOKEN_URL);
                }
            }
        }
        return responseEntity;
    }

    private RegistrationTokenRespDto baseGetRegistrationToken() {
        SkypeTokenRespDto skypeTokenRespDto = skypeTokenService.getSkypeToken();
        ResponseEntity<String> responseEntity = requestEndpoints(skypeTokenRespDto.getSkypeToken(), RegistrationTokenConstant.REGISTRATION_TOKEN_URL);
        RegistrationTokenRespDto registrationTokenRespDto = new RegistrationTokenRespDto();
        HttpHeaders respHeaders = responseEntity.getHeaders();
        List<String> tokenList = respHeaders.get("Set-RegistrationToken");
        if(CollectionUtil.isNotEmpty(tokenList)){
            String token = tokenList.get(0);
            registrationTokenRespDto.setRegistrationToken(token);
            String endpointId = token.substring(token.indexOf("endpointId=") + 11);
            registrationTokenRespDto.setEndpointId(endpointId);
        }
        List<String> locationList = respHeaders.get("Location");
        if(CollectionUtil.isNotEmpty(locationList)){
            String location = locationList.get(0);
            if(location.contains("%7B") && location.contains("%7D")){
                registrationTokenRespDto.setLocation(location);
            }else {
                String encodeEndpointId = URLEncoder.encode(registrationTokenRespDto.getEndpointId());
                registrationTokenRespDto.setLocation(new StringBuilder().append(location).append("/").append(encodeEndpointId).toString());
            }
        }
        return registrationTokenRespDto;
    }

}
