package com.bumingzeyi.send.service.impl;

import com.alibaba.fastjson.JSON;
import com.bumingzeyi.authentication.service.RegistrationTokenService;
import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;
import com.bumingzeyi.send.constant.SendDataConstant;
import com.bumingzeyi.send.service.SendTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author :BuMing
 * @date : 2022-03-28 22:04
 */
@Service
@Slf4j
public class SendTextServiceImpl implements SendTextService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RegistrationTokenService registrationTokenService;

    @Override
    public void sendText(RegistrationTokenRespDto registrationToken, String sendObj, String content) {
        //获取对应端点服务器编号
        String location = registrationToken.getLocation();
        String locationNo = location.substring(8, location.indexOf("-client-s.gateway.messenger.live.com/v1/users/ME/endpoints"));
        // 请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.set("RegistrationToken", registrationToken.getRegistrationToken());
        // 请求体
        Map<String, Object> params = new HashMap<>();
        params.put("contenttype", "text");
        params.put("messagetype", "Text");
        params.put("content", content);
        //params.put("imdisplayname", displayName);
        params.put("Has-Mentions", "false");
        params.put("clientmessageid", String.valueOf(System.currentTimeMillis()));
        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(params), httpHeaders);
        try {
            log.info("开始请求发送文本消息API");
            restTemplate.postForEntity(
                String.format(SendDataConstant.SEND_DATA_URL, locationNo, formatChatUser(sendObj)),
                requestEntity,
                String.class);
            log.info("结束请求发送文本消息API,resp:{}", requestEntity.getBody());
        } catch (RestClientException e) {
            log.error("请求发送文本消息API异常", e);
            if(e instanceof HttpClientErrorException && ((HttpClientErrorException) e).getStatusCode() == HttpStatus.UNAUTHORIZED){
                RegistrationTokenRespDto registrationTokenRespDto = registrationTokenService.reGetRegistrationToken();
                sendText(registrationTokenRespDto, sendObj, content);
            }
        }
    }

    /**
     * formatChatUser
     * @param skypeID
     * @return
     */
    public String formatChatUser(String skypeID){
        if(Pattern.matches("^live.*", skypeID)){
            return String.format("%s:%s",8,skypeID);
        }else if(Pattern.matches("^19.*skype", skypeID)){
            return skypeID;
        }else if(Pattern.matches("^8:live:\\.cid\\..*", skypeID)){
            return skypeID;
        }
        return skypeID;
    }

}
