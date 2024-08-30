package com.bumingzeyi.common.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: BuMing
 * @date: 2022/3/29 21:17
 */
@NoArgsConstructor
@Data
public class EventsRespDto {


    @JsonProperty("eventMessages")
    private List<EventMessagesDTO> eventMessages;

    @NoArgsConstructor
    @Data
    public static class EventMessagesDTO {
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("type")
        private String type;
        @JsonProperty("resourceType")
        private String resourceType;
        @JsonProperty("time")
        private String time;
        @JsonProperty("resourceLink")
        private String resourceLink;
        @JsonProperty("resource")
        private ResourceDTO resource;

        @NoArgsConstructor
        @Data
        public static class ResourceDTO {
            @JsonProperty("ackrequired")
            private String ackrequired;
            @JsonProperty("type")
            private String type;
            @JsonProperty("from")
            private String from;
            @JsonProperty("clientmessageid")
            private String clientmessageid;
            @JsonProperty("receiverdisplayname")
            private String receiverdisplayname;
            @JsonProperty("version")
            private String version;
            @JsonProperty("messagetype")
            private String messagetype;
            @JsonProperty("counterpartymessageid")
            private String counterpartymessageid;
            @JsonProperty("imdisplayname")
            private String imdisplayname;
            @JsonProperty("content")
            private String content;
            @JsonProperty("composetime")
            private String composetime;
            @JsonProperty("origincontextid")
            private String origincontextid;
            @JsonProperty("originalarrivaltime")
            private String originalarrivaltime;
            @JsonProperty("threadtopic")
            private String threadtopic;
            @JsonProperty("contenttype")
            private String contenttype;
            @JsonProperty("mlsEpoch")
            private String mlsEpoch;
            @JsonProperty("conversationLink")
            private String conversationLink;
            @JsonProperty("isactive")
            private Boolean isactive;
            @JsonProperty("id")
            private String id;
        }
    }
}
