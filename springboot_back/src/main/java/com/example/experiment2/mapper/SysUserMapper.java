package com.example.experiment2.mapper;

import com.example.experiment2.entity.SysUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper {

    SysUser findByUsername(String username);

    SysUser findByEmail(String email);

    SysUser findById(Long id);

    int insert(SysUser user);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    List<SysUser> findAdminPage(@Param("username") String username,
                                @Param("email") String email,
                                @Param("role") String role,
                                @Param("status") String status,
                                @Param("offset") int offset,
                                @Param("pageSize") int pageSize);

    long countAdmin(@Param("username") String username,
                    @Param("email") String email,
                    @Param("role") String role,
                    @Param("status") String status);

    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
