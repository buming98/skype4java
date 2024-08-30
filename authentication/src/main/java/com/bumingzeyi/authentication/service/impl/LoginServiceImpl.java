package com.bumingzeyi.authentication.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumingzeyi.authentication.constant.LoginConstant;
import com.bumingzeyi.authentication.service.LoginService;
import com.bumingzeyi.common.dto.LoginToken.LoginTokenRespDto;
import com.bumingzeyi.common.utils.Dom4jUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author :BuMing
 * @date : 2022-03-27 21:13
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Value("${skype.login.userName}")
    public String userName;

    @Value("${skype.login.passWord}")
    public String passWord;

    @Resource
    private RestTemplate restTemplate;

    @Override
    @Cacheable(value = "loginTokenCache", key = "'loginTokenCache'")
    public LoginTokenRespDto login(){
        String responseData = requestLoginAPI();
        return resolving(responseData);
    }

    @Override
    @CachePut(value = "loginTokenCache", key = "'loginTokenCache'")
    public LoginTokenRespDto reLogin() {
        String responseData = requestLoginAPI();
        return resolving(responseData);
    }


    /**
     * 请求登录API
     * @return
     */
    public String requestLoginAPI(){
        // 请求头s
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_XML);
        // 请求体
        String loginData = String.format(LoginConstant.LOGIN_TEMPLATE, userName, passWord);
        // 请求HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(loginData, requestHeader);
        String responseBody = null;
        try {
            log.info("开始请求登录API, req:{}", JSON.toJSONString(requestEntity));
            responseBody = restTemplate.postForObject(LoginConstant.LOGIN_URL, requestEntity, String.class);
            log.info("结束请求登录API, resp:{}", responseBody);
        } catch (RestClientException e) {
            log.info("请求登录API异常", e);
            return null;
        }
        return responseBody;
    }

    /**
     * 解析登录返回的xml
     * @param responseData
     * @return
     */
    private LoginTokenRespDto resolving(String responseData){
        Document document = null;
        try {
            document = DocumentHelper.parseText(responseData);
        } catch (DocumentException e) {
            log.error("dom4j解析异常", e);
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        Dom4jUtils.dom4j2Json(document.getRootElement(), jsonObject);
        LoginTokenRespDto loginTokenRespDto = JSON.parseObject(jsonObject.toJSONString(), LoginTokenRespDto.class);
        return loginTokenRespDto;
    }

}
