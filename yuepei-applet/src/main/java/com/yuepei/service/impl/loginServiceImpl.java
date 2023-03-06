package com.yuepei.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.yuepei.common.constant.Constants;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.domain.model.LoginUser;
import com.yuepei.common.exception.ServiceException;
import com.yuepei.common.exception.user.UserPasswordNotMatchException;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.MessageUtils;
import com.yuepei.common.utils.ServletUtils;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.ip.IpUtils;
import com.yuepei.mapper.LoginMapper;
import com.yuepei.service.LoginService;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.service.ISysUserService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

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
 * @create ：2022/12/2 15:58
 **/
@Service
@Validated
public class loginServiceImpl implements LoginService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ISysUserService userService;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secrect}")
    private String secrect;

    @Override
    public AjaxResult login(String code, SysUser sysUser) {
        AjaxResult ajax = AjaxResult.success();
        // 用户验证
        //请求微信接口，通过用户code解析成session_key、openid
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secrect + "&js_code=" + code + "&grant_type=authorization_code";
        //发送请求，并接收响应
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        //获取响应体
        String body = forEntity.getBody();
        if(StringUtils.isNull(JSONObject.parse(body).get("openid"))){
            String errcode = JSONObject.parse(body).get("errcode").toString();
            String errmsg = JSONObject.parse(body).get("errmsg").toString();
            return AjaxResult.error(Integer.parseInt(errcode),errmsg);
        }
        //响应体转json对象
        JSONObject jsonObject = JSONObject.parse(body);
        //断言 值不为空
        assert jsonObject != null;
        //获取openid
        String openid = (String) jsonObject.get("openid");
        //判断 用户是否存在
        // 用户验证
        SysUser sysUserList = userMapper.selectUserByOpenid(openid);
        if(!StringUtils.isNull(sysUserList)){
            if ("1".equals(sysUserList.getStatus())){
                return AjaxResult.error("账号已被封禁");
            }
            recordLoginInfo(sysUserList.getUserId());
            sysUser.setOpenid(openid);
            return ajax.put(Constants.TOKEN, tokenUtils.createToken(sysUserList));
        }else {
            sysUser.setOpenid(openid);
            sysUser.setRoleId(2L);
            userService.insertUser(sysUser);
            SysUser sysUser1 = userMapper.selectUserByOpenid(openid);
            recordLoginInfo(sysUser1.getUserId());
            return ajax.put(Constants.TOKEN, tokenUtils.createToken(sysUser1));
        }
    }

    @Override
    public AjaxResult APPLogin(SysUser sysUser) {
        AjaxResult ajax = AjaxResult.success();
        //判断 用户是否存在
        // 用户验证
        SysUser sysUserList = userMapper.selectUserByUserName(sysUser.getUserName());
        if(!StringUtils.isNull(sysUserList)){
            if ("1".equals(sysUserList.getStatus())){
                return AjaxResult.error("账号已被封禁");
            }
            recordLoginInfo(sysUserList.getUserId());
            return ajax.put(Constants.TOKEN, tokenUtils.createToken(sysUserList));
        }else {
            sysUser.setRoleId(2L);
            userService.insertUser(sysUser);
            recordLoginInfo(sysUserList.getUserId());
            return ajax.put(Constants.TOKEN, tokenUtils.createToken(sysUserList));
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
