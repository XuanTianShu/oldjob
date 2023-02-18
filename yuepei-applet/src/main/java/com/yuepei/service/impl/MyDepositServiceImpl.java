package com.yuepei.service.impl;
import com.yuepei.service.MyDepositService;
import com.yuepei.system.domain.Deposit;
import com.yuepei.system.domain.UserDepositOrder;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.mapper.UserDepositDetailMapper;
import com.yuepei.system.mapper.UserDepositOrderMapper;
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
 * @create ：2022/12/9 13:52
 **/
@Service
public class MyDepositServiceImpl implements MyDepositService {

    //押金订单信息
    @Autowired
    private UserDepositOrderMapper userDepositOrderMapper;

    //押金 明细
    @Autowired
    private UserDepositDetailMapper userDepositDetailMapper;


    /**
     * 根据用户唯一标识得到 用户押金
     * @param openid
     * @return
     */
    @Override
    public List<UserDepositOrder> selectUserDepositInfo(String openid, Integer status) {
        return userDepositOrderMapper.selectUserDepositInfo(openid,status);
    }

    @Override
    public List<UserIntegralBalanceDepositVo> selectUserDepositDetailInfo(String openid, Integer status) {
        return userDepositDetailMapper.selectUserDepositDetailByOpenid(openid,status);
    }


}
