package com.example.experiment2.mapper;

import com.example.experiment2.entity.LoginUser;
import org.apache.ibatis.annotations.Param;

public interface LoginUserMapper {

    LoginUser findByUsername(String username);

    int insert(LoginUser user);

    int updatePassword(@Param("username") String username, @Param("password") String password);
}
