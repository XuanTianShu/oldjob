package com.yuepei.utils;

import com.alibaba.fastjson2.JSONObject;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.service.ISysUserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 　　　　 ┏┓       ┏┓+ +
 * 　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　┃　　　　　　 ┃
 * 　　　　┃　　　━　　　┃ ++ + + +
 * 　　　 █████━█████  ┃+
 * 　　　　┃　　　　　　 ┃ +
 * 　　　　┃　　　┻　　　┃
 * 　　　　┃　　　　　　 ┃ + +
 * 　　　　┗━━┓　　　 ┏━┛
 * 　　　　　　┃　　  ┃
 * 　　　　　　┃　　  ┃ + + + +
 * 　　　　　　┃　　　┃　Code is far away from bug with the animal protection
 * 　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码永无bug
 * 　　　　　　┃　　　┃
 * 　　　　　　┃　　　┃　　+
 * 　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　┃ 　　　　　┣-┓
 * 　　　　　　┃ 　　　　　┏-┛
 * 　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 * *********************************************************
 *
 * @author ：AK
 * @create ：2022/12/12 15:58
 **/
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private ISysUserService usersService;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * 访问接口前，检查是否登录
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //获取请求头的Token
        String authorization = request.getHeader("Authorization");
        //判断是否为空
        if (authorization == null || authorization.equals("")) {
            return false;
        }
        try {
            //解析Token
            Jws<Claims> claimsJws = tokenUtils.parseToken(authorization);
            //获取Token的值
            SysUser user = JSONObject.parseObject(JSONObject.toJSONString(claimsJws.getBody().get("user")), SysUser.class);
            if (user.getUserId() == null || user.getUserId().equals("")) {
                return false;
            }
            if (user != null || user.getStatus().equals("0")) {
                return true;
            }
        }catch (Exception e){
            throw new JwtException("请求访问："+request.getRequestURI()+"   认证失败，无法访问系统资源");
        }

        return false;
    }
}