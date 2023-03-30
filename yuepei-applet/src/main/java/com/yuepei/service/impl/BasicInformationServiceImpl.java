package com.yuepei.service.impl;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.redis.RedisCache;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.sms.AliSMS;
import com.yuepei.service.BasicInformationService;
import com.yuepei.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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
 * @create ：2022/12/20 10:35
 **/
@Service
public class BasicInformationServiceImpl implements BasicInformationService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private AliSMS aliSMS;


    @Autowired
    private RedisCache redisCache;

    @Override
    public SysUser selectUserData(Long userId) {
        return sysUserMapper.selectUserById(userId);
    }

    @Override
    public AjaxResult updateUserPhoneNumber(Long userId, String phoneNumber,String code) {
        if (aliSMS.judge(phoneNumber,code)) {
            sysUserMapper.updateUserPhoneNumber(userId,phoneNumber);
            return AjaxResult.success();
        }
        return AjaxResult.error("验证码不正确");
    }

    @Override
    public AjaxResult getCode(String oldPhoneNumber,String newPhoneNumber,SysUser sysUser) {
        if (oldPhoneNumber != null){
            int b = sysUserMapper.checkUserOldPhoneNumber(oldPhoneNumber,sysUser.getUserId());
            if (b < 0){
                return AjaxResult.error("原手机号不对");
            }
        }
        SysUser user = sysUserMapper.selectUserByPhoneNumber(newPhoneNumber);
        if(StringUtils.isNull(user)){
            String randomNum = aliSMS.getRandomNum(6);
            redisCache.setCacheObject(newPhoneNumber, randomNum, 300, TimeUnit.SECONDS);
            if (aliSMS.sendSmsCode(newPhoneNumber,randomNum)) {
                return AjaxResult.success();
            }
            return AjaxResult.error();
        }
        return AjaxResult.error("手机号已存在");
    }
}
