package com.lec.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWxLoginVo implements Serializable {
    //唯一标识
    private Long id;

    //账户
    private String openid;

    //token
    private String token;
}
