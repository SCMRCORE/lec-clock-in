package com.lec.service;

import com.lec.dto.UserDto;
import com.lec.dto.UserLoginDto;
import com.lec.dto.UserWxLoginDto;
import com.lec.entity.User;

public interface UserService {
    User login(UserLoginDto userLoginDto);

    void register(UserDto userDto);

    User wxLogin(UserWxLoginDto userWxLoginDto);
}
