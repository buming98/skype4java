package com.bumingzeyi.events.service.impl;

import com.alibaba.fastjson.JSON;
import com.bumingzeyi.authentication.service.RegistrationTokenService;
import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;
import com.bumingzeyi.common.dto.events.EventsRespDto;
import com.bumingzeyi.events.constant.EventsConstant;
import com.bumingzeyi.events.entity.dto.events.RegistrationEndpointsRespDto;
import com.bumingzeyi.events.service.EventsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: BuMing
 * @date: 2022/3/29 21:07
 */
@Service
@Slf4j
public class EventsServiceImpl implements EventsService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RegistrationTokenService registrationTokenService;

    @Override
    public EventsRespDto getEvents(RegistrationTokenRespDto registrationTokenRespDto) {
        // 请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.set("RegistrationToken", registrationTokenRespDto.getRegistrationToken());
        // 请求体

        // 请求httpEntity
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);
        // 发起请求
        String url = EventsConstant.GET_EVENTS_URL.replace("{location}", registrationTokenRespDto.getLocation());
        ResponseEntity<String> responseEntity = null;
        try {
            log.info("开始请求事件API, req:{}", JSON.toJSONString(httpEntity));
            responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
            log.info("结束请求事件API, resp:{}", JSON.toJSONString(responseEntity));
        } catch (RestClientException e) {
            log.error("请求事件API异常:{}", e.getMessage());
            if(e instanceof HttpClientErrorException){
                HttpClientErrorException exp = (HttpClientErrorException) e;
                if(exp.getStatusCode() == HttpStatus.NOT_FOUND){
                    for(int i = 0; i < 2; i++){
                        log.info("NOT_FOUND,重新请求subscribe");
                        String subscribe = subscribe(registrationTokenRespDto);
                        if(subscribe == null){
                            return null;
                        }
                    }
                    return getEvents(registrationTokenRespDto);
                }else if (exp.getStatusCode() == HttpStatus.UNAUTHORIZED || exp.getStatusCode() == HttpStatus.MOVED_PERMANENTLY){
                    RegistrationTokenRespDto registrationToken = registrationTokenService.reGetRegistrationToken();
                    BeanUtils.copyProperties(registrationToken, registrationTokenRespDto);
                    return getEvents(registrationTokenRespDto);
                }
            }
            if("Read timed out".equals(e.getCause().getMessage())){
                log.info("开始请求事件API超时");
                return null;
            }
        }
        return responseEntity == null ? null : JSON.parseObject(responseEntity.getBody(), EventsRespDto.class);
    }

    @Override
    public void ping(RegistrationTokenRespDto registrationTokenRespDto) {
        // 请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.set("RegistrationToken", registrationTokenRespDto.getRegistrationToken());
//        httpHeaders.set("SKPY_DEBUG_HTTP", "1");
//        httpHeaders.set("BehaviorOverride", "redirectAs404");
        // 请求体
        Map<String, Integer> map = new HashMap<>();
        map.put("timeout", 12);
        // 请求httpEntity
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(map), httpHeaders);
        // 发起请求
        String url = EventsConstant.PING_URL.replace("{location}", registrationTokenRespDto.getLocation());
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(
                    url,
                    httpEntity,
                    String.class
            );
        } catch (RestClientException e) {
            log.error("ping异常", e.getMessage());
        }
        if(responseEntity != null){
            System.out.println(responseEntity);
        }
    }

    @Override
    public RegistrationEndpointsRespDto registrationEndpoints(RegistrationTokenRespDto registrationTokenRespDto) {
        // 请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.set("RegistrationToken", registrationTokenRespDto.getRegistrationToken());
        // 请求体
        Map<String, String> map = new HashMap<>();
        map.put("endpointFeatures", "Agent,Presence2015,MessageProperties,CustomUserProperties,Casts,ModernBots,AutoIdleForWebApi,secureThreads,notificationStream,InviteFree,SupportsReadReceipts");

        // 请求httpEntity
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(map), httpHeaders);
        // 发起请求
        String url = EventsConstant.REGISTRATION_ENDPOINTS.replace("{location}", registrationTokenRespDto.getLocation());
        ResponseEntity<String> responseEntity = null;
        try {
            log.info("开始请求注册端点API, req:{}", JSON.toJSONString(httpEntity));
            responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
            log.info("结束请求注册端点API, resp:{}", JSON.toJSONString(responseEntity));
        } catch (RestClientException e) {
            log.error("请求注册端点API异常", e);
            if(e instanceof HttpClientErrorException){
                HttpClientErrorException exp = (HttpClientErrorException) e;
                if(exp.getStatusCode() == HttpStatus.UNAUTHORIZED || exp.getStatusCode() == HttpStatus.MOVED_PERMANENTLY){
                    RegistrationTokenRespDto registrationToken = registrationTokenService.reGetRegistrationToken();
                    BeanUtils.copyProperties(registrationToken, registrationTokenRespDto);
                    return registrationEndpoints(registrationTokenRespDto);
                }
            }
        }
        return responseEntity == null ? null : JSON.parseObject(responseEntity.getBody(), RegistrationEndpointsRespDto.class);
    }

    @Override
    public String subscribe(RegistrationTokenRespDto registrationTokenRespDto) {
        // 请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.set("RegistrationToken", registrationTokenRespDto.getRegistrationToken());
        // 请求体
        Map<String, Object> map = new HashMap<>();
        List<String> interestedResourcesList = new ArrayList<>();
        interestedResourcesList.add("/v1/users/ME/conversations/ALL/messages");
        map.put("interestedResources", interestedResourcesList);
        map.put("channelType", "HttpLongPoll");
        map.put("conversationType", 2047);
        // 请求httpEntity
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(map), httpHeaders);
        // 发起请求
        String url = EventsConstant.SUBSCRIBE_URL.replace("{location}", registrationTokenRespDto.getLocation());
        ResponseEntity<String> responseEntity = null;
        try {
            log.info("开始请求注册监听类型API, req:{}", JSON.toJSONString(httpEntity));
            responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
            log.info("结束请求注册监听类型API, resp:{}", JSON.toJSONString(responseEntity));
        } catch (RestClientException e) {
            log.error("请求注册监听类型API异常", e);
            if(e instanceof HttpClientErrorException){
                HttpClientErrorException exp = (HttpClientErrorException) e;
                if(exp.getStatusCode() == HttpStatus.NOT_FOUND){
                    RegistrationEndpointsRespDto registrationEndpointsRespDto = registrationEndpoints(registrationTokenRespDto);
                    if(Objects.isNull(registrationEndpointsRespDto)){
                        return null;
                    }
                    return subscribe(registrationTokenRespDto);
                }else if (exp.getStatusCode() == HttpStatus.UNAUTHORIZED || exp.getStatusCode() == HttpStatus.MOVED_PERMANENTLY){
                    RegistrationTokenRespDto registrationToken = registrationTokenService.reGetRegistrationToken();
                    BeanUtils.copyProperties(registrationToken, registrationTokenRespDto);
                    return subscribe(registrationTokenRespDto);
                }
            }
        }
        return responseEntity == null ? null : responseEntity.getBody();
    }

}
