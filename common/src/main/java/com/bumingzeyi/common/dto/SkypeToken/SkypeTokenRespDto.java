package com.bumingzeyi.common.dto.SkypeToken;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :BuMing
 * @date : 2022-03-28 21:14
 */
@Data
public class SkypeTokenRespDto implements Serializable {
    private static final long serialVersionUID = 1954924131753408524L;

    @JSONField(name = "skypetoken")
    private String skypeToken;

    @JSONField(name = "expiresIn")
    private Integer expiresIn;

    @JSONField(name = "skypeid")
    private String skypeId;

    @JSONField(name = "signinname")
    private String signinName;

    @JSONField(name = "anid")
    private String anId;

    @JSONField(name = "isBusinessTenant")
    private String isBusinessTenant;

    @JSONField(name = "status")
    private SkypeTokenRespStatus status;

}
