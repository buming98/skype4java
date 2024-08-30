package com.bumingzeyi.events.entity.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Jace
 * @date: 2022/3/31 21:37
 */
@NoArgsConstructor
@Data
public class RegistrationEndpointsRespDto {

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("networks")
    private List<?> networks;
    @JsonProperty("policies")
    private PoliciesDTO policies;
    @JsonProperty("subscriptions")
    private List<?> subscriptions;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("productContext")
    private String productContext;

    @NoArgsConstructor
    @Data
    public static class PoliciesDTO {
    }

}
