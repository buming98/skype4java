package com.bumingzeyi.events.service;

import com.bumingzeyi.common.dto.RegistrationToken.RegistrationTokenRespDto;
import com.bumingzeyi.common.dto.events.EventsRespDto;
import com.bumingzeyi.events.entity.dto.events.RegistrationEndpointsRespDto;

/**
 * @author :BuMing
 * @date : 2022-04-04 21:24
 */
public interface EventsService {

    EventsRespDto getEvents(RegistrationTokenRespDto registrationTokenRespDto);

    void ping(RegistrationTokenRespDto registrationTokenRespDto);

    RegistrationEndpointsRespDto registrationEndpoints(RegistrationTokenRespDto registrationTokenRespDto);

    String subscribe(RegistrationTokenRespDto registrationTokenRespDto);

}
