package com.lec.mapper;

import com.lec.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * from user where openid=#{openid}")
    User getUserByOpenid(String openid);

    User getUserByAccount(String account);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);
}
