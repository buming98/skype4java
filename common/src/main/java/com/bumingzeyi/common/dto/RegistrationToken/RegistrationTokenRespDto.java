package com.bumingzeyi.common.dto.RegistrationToken;

import lombok.Data;

import java.io.Serializable;

/**
 * @author :BuMing
 * @date : 2022-03-28 21:56
 */
@Data
public class RegistrationTokenRespDto implements Serializable {

    private static final long serialVersionUID = 1954924131753408524L;

    private String registrationToken;

    private String location;

    private String endpointId;

}
