package com.bumingzeyi.common.dto.LoginToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author :BuMing
 * @date : 2022-04-03 13:42
 */

@NoArgsConstructor
@Data
public class LoginTokenRespDto implements Serializable {
    private static final long serialVersionUID = 1954924131753408524L;

    @JsonProperty("header")
    private String header;

    @JsonProperty("Body")
    private BodyDTO body;

    @NoArgsConstructor
    @Data
    public static class BodyDTO implements Serializable{
        private static final long serialVersionUID = 1954924131753408524L;
        @JsonProperty("RequestSecurityTokenResponseCollection")
        private RequestSecurityTokenResponseCollectionDTO requestSecurityTokenResponseCollection;

        @NoArgsConstructor
        @Data
        public static class RequestSecurityTokenResponseCollectionDTO implements Serializable{
            private static final long serialVersionUID = 1954924131753408524L;
            @JsonProperty("RequestSecurityTokenResponse")
            private RequestSecurityTokenResponseDTO requestSecurityTokenResponse;

            @NoArgsConstructor
            @Data
            public static class RequestSecurityTokenResponseDTO implements Serializable{
                private static final long serialVersionUID = 1954924131753408524L;
                @JsonProperty("TokenType")
                private String tokenType;
                @JsonProperty("RequestedSecurityToken")
                private RequestedSecurityTokenDTO requestedSecurityToken;
                @JsonProperty("LifeTime")
                private LifeTimeDTO lifeTime;
                @JsonProperty("AppliesTo")
                private AppliesToDTO appliesTo;

                @NoArgsConstructor
                @Data
                public static class RequestedSecurityTokenDTO implements Serializable{
                    private static final long serialVersionUID = 1954924131753408524L;
                    @JsonProperty("BinarySecurityToken")
                    private String binarySecurityToken;
                }

                @NoArgsConstructor
                @Data
                public static class LifeTimeDTO implements Serializable{
                    private static final long serialVersionUID = 1954924131753408524L;
                    @JsonProperty("Expires")
                    private LocalDateTime expires;
                    @JsonProperty("Created")
                    private LocalDateTime created;
                }

                @NoArgsConstructor
                @Data
                public static class AppliesToDTO implements Serializable{
                    private static final long serialVersionUID = 1954924131753408524L;
                    @JsonProperty("EndpointReference")
                    private EndpointReferenceDTO endpointReference;

                    @NoArgsConstructor
                    @Data
                    public static class EndpointReferenceDTO implements Serializable{
                        private static final long serialVersionUID = 1954924131753408524L;
                        @JsonProperty("Address")
                        private String address;
                    }
                }
            }
        }
    }
}
