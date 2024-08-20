package com.lec.service.lmp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lec.constant.MessageConstant;
import com.lec.dto.UserDto;
import com.lec.dto.UserLoginDto;
import com.lec.dto.UserWxLoginDto;
import com.lec.entity.User;
import com.lec.exception.AccountNotFoundException;
import com.lec.exception.LoginFailedException;
import com.lec.exception.PasswordErrorException;
import com.lec.mapper.UserMapper;
import com.lec.properties.WeChatProperties;
import com.lec.service.UserService;
import com.lec.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServicelmp implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private WeChatProperties weChatProperties;

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 登录
     * @param userLoginDto
     * @return
     */
    public User login(UserLoginDto userLoginDto) {
        String account = userLoginDto.getAccount();
        String password = userLoginDto.getPassword();

        //先查询账户
        User user = userMapper.getUserByAccount(account);

        if(user==null){
            throw new AccountNotFoundException("账号未注册");
        }

        //验证密码
        //先进行md5加密再比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(user.getPassword())){
            throw new PasswordErrorException("密码错误");
        }

        return user;
    }

    /**
     * 注册
     * @param userDto
     */
    @Override
    public void register(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        //对密码加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        log.info("注入user:{}", user);
        userMapper.insert(user);
    }

    /**
     * 微信登录
     * @param userWxLoginDto
     * @return
     */
    @Override
    public User wxLogin(UserWxLoginDto userWxLoginDto) {
        //调用微信接口，获取openid
        String res = getString(userWxLoginDto);
        JSONObject jsonObject = JSON.parseObject(res);//res是JSON格式，需要手动转一下字符串
        String openid = jsonObject.getString("openid");

        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user = userMapper.getUserByOpenid(openid);

        if(user==null){
            user= User.builder()
                    .openid(openid)
                    .build();
            userMapper.insert(user);
        }
        return user;
    }

    private String getString(UserWxLoginDto userWxLoginDto) {
        Map<String, String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code", userWxLoginDto.getCode());
        map.put("grant_type","authorization_code");
        String res = HttpClientUtil.doGet(WX_LOGIN, map);
        return res;
    }
}
