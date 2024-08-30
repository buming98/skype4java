package com.bumingzeyi.common.dto.SkypeToken;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :BuMing
 * @date : 2022-03-28 21:16
 */
@Data
public class SkypeTokenRespStatus implements Serializable {
    private static final long serialVersionUID = 1954924131753408524L;

    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "text")
    private String text;

}
