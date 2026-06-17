package com.example.experiment2.interceptor;

import com.example.experiment2.entity.SysUser;
import com.example.experiment2.mapper.SysUserMapper;
import com.example.experiment2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final SysUserMapper sysUserMapper;

    public AuthInterceptor(JwtUtil jwtUtil, SysUserMapper sysUserMapper) {
        this.jwtUtil = jwtUtil;
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            writeUnauthorized(response);
            return false;
        }

        JwtUtil.Claims claims = jwtUtil.parseToken(header.substring(7));
        if (claims == null) {
            writeUnauthorized(response);
            return false;
        }

        SysUser user = sysUserMapper.findById(claims.getUserId());
        if (user == null || !"NORMAL".equals(user.getStatus())) {
            writeUnauthorized(response);
            return false;
        }

        request.setAttribute("currentUserId", user.getId());
        request.setAttribute("currentUsername", user.getUsername());
        request.setAttribute("currentRole", user.getRole());
        return true;
    }

    private void writeUnauthorized(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"success\":false,\"message\":\"登录状态已失效\",\"data\":null}");
    }
}
