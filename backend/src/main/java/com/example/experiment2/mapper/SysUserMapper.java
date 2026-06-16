package com.example.experiment2.mapper;

import com.example.experiment2.entity.SysUser;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper {

    SysUser findByUsername(String username);

    SysUser findByEmail(String email);

    SysUser findById(Long id);

    int insert(SysUser user);

    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
