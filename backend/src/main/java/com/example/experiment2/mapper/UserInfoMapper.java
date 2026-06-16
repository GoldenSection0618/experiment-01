package com.example.experiment2.mapper;

import com.example.experiment2.entity.UserInfo;

import java.util.List;

public interface UserInfoMapper {

    UserInfo findByName(String name);

    List<UserInfo> findAll();

    int insert(UserInfo userInfo);
}
