package com.yuepei.service.impl;

import com.yuepei.service.MyIntegralService;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.mapper.UserIntegralOrderMapper;
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
 * @create ：2022/12/19 14:43
 **/
@Service
public class MyIntegralServiceImpl implements MyIntegralService {

    @Autowired
    private UserIntegralOrderMapper userIntegralOrderMapper;

    @Override
    public List<UserIntegralBalanceDepositVo> integralList(String openid, UserIntegralBalanceDepositVo userIntegralBalanceDepositVo ) {
        return userIntegralOrderMapper.selectUserIntegralOrderByUserId(openid,userIntegralBalanceDepositVo);
    }

    @Override
    public List<UserIntegralBalanceDepositVo> selectIntegralDetailList(UserIntegralBalanceDepositVo userIntegralBalanceDepositVo) {
        return userIntegralOrderMapper.selectIntegralDetailList(userIntegralBalanceDepositVo);
    }
}
