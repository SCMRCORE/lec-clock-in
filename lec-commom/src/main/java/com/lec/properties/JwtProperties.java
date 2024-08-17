package com.lec.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lec.jwt")
@Data
public class JwtProperties {

    /**
     * 用户端生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

    /**
     * 微信登录jwt令牌相关配置
     */
    private String userWxSecretKey;
    private long userWxTtl;
    private String userWxTokenName;

}
