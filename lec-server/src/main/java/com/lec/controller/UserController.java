package com.lec.controller;

import com.lec.constant.JwtClaimsConstant;
import com.lec.dto.UserDto;
import com.lec.dto.UserLoginDto;
import com.lec.dto.UserWxLoginDto;
import com.lec.entity.User;
import com.lec.properties.JwtProperties;
import com.lec.result.Result;
import com.lec.service.UserService;
import com.lec.utils.JwtUtil;
import com.lec.vo.UserLoginVo;
import com.lec.vo.UserWxLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param userLoginDto
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVo> login(@RequestBody UserLoginDto userLoginDto){
        log.info("登录:{}",userLoginDto);
        User user = userService.login(userLoginDto);

        //登陆成功，下发JWT令牌

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
            jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                    claims);

        UserLoginVo userLoginVo =  UserLoginVo.builder()
                .id(user.getId())
                .account(user.getAccount())
                .token(token)
                .build();

        return Result.success(userLoginVo);
    }

    /**
     * 注册
     * @param userDto
     * @return
     */
    @PostMapping("/add")
    public Result register(@RequestBody UserDto userDto){
        log.info("注册：{}", userDto);
        userService.register(userDto);
        return Result.success();
    }


    /**
     * 微信登录
     * @param userWxLoginDto
     * @return
     */
    @PostMapping("/wx/login")
    public Result<UserWxLoginVo> WxLogin(@RequestBody UserWxLoginDto userWxLoginDto) {
        log.info("微信登录：{}", userWxLoginDto);
        User user = userService.wxLogin(userWxLoginDto);

        // 登录成功，下发JWT令牌
        Map<String, Object> claims=new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserWxLoginVo userWxLoginVo = UserWxLoginVo.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userWxLoginVo);
    }

}

