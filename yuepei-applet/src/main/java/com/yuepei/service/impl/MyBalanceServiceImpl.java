package com.yuepei.service.impl;

import com.yuepei.service.MyBalanceService;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.mapper.UserBalanceDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
 * @create ：2023/1/7 15:49
 **/
@Service
public class MyBalanceServiceImpl implements MyBalanceService {

    @Autowired
    private UserBalanceDetailMapper userBalanceDetailMapper;

    @Override
    public List<UserIntegralBalanceDepositVo> selectUserBalanceByOpenid(String openid, Integer status) {
        return userBalanceDetailMapper.selectUserBalanceDetailByOpenid(openid,status);
    }

    @Override
    public List<UserIntegralBalanceDepositVo> selectBalanceDetailList(UserIntegralBalanceDepositVo userIntegralBalanceDepositVo) {
        return userBalanceDetailMapper.selectBalanceDetailList(userIntegralBalanceDepositVo);
    }
}
