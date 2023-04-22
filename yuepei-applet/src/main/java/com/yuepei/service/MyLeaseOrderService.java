package com.yuepei.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.vo.ConditionOrderVO;
import com.yuepei.system.domain.vo.LeaseOrderVO;
import com.yuepei.system.domain.vo.OrderSumAndMoneyVO;

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
 * @create ：2023/1/9 18:34
 **/
public interface MyLeaseOrderService {
    List<UserLeaseOrder> userLeaseOrder(String openid, Integer status);

    AjaxResult insertUserLeaseOrder(String openid, String rows, UserLeaseOrder userLeaseOrder);

    /**
     * 查询租赁订单
     * @param leaseOrderVO
     * @return
     */
    List<UserLeaseOrder> leaseOrderList(LeaseOrderVO leaseOrderVO);

    OrderSumAndMoneyVO selectDayOrder(LeaseOrderVO leaseOrderVO);

    ConditionOrderVO selectConditionOrder(LeaseOrderVO leaseOrderVO);
}
