package com.yuepei.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.vo.OrderDepositListVO;
import com.yuepei.system.domain.vo.RechargeVO;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.util.HashMap;

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
 * @create ：2023/1/14 14:08
 **/
public interface UserRefundService {
    AjaxResult userRefund(String openid, String orderNumber);

    HashMap<String, String> userRefundCallBack(HttpServletRequest request) throws GeneralSecurityException;

    AjaxResult userDepositRefund(String openid);

    AjaxResult unlocking(HttpServletRequest request);

    AjaxResult orderRefund(UserLeaseOrder userLeaseOrder);

    HashMap<String, String> orderRefundCallBack(HttpServletRequest request) throws GeneralSecurityException;

    AjaxResult depositRefund(OrderDepositListVO orderDepositListVOt);

    HashMap<String, String> depositRefundCallback(HttpServletRequest request) throws GeneralSecurityException;

    AjaxResult rechargeRefund(RechargeVO rechargeVO);
}
