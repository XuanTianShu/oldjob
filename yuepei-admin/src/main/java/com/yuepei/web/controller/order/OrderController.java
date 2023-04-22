package com.yuepei.web.controller.order;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.R;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.service.MyLeaseOrderService;
import com.yuepei.service.UserRefundService;
import com.yuepei.system.domain.Order;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.system.service.OrderService;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
 * @create ：2022/12/5 15:20
 * 订单公共类
 **/
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MyLeaseOrderService myLeaseOrderService;

    @Autowired
    private UserRefundService userRefundService;

    /**
     * 查询商品订单列表
     * @param order 商品订单类
     * @return
     */
    @PreAuthorize("@ss.hasPermi('order:order:list')")
    @GetMapping("/goodsList")
    public TableDataInfo Goodslist(Order order)
    {
        startPage();
        List<Order> list = orderService.selectOrderList(order);
        return getDataTable(list);
    }

    /**
     * 查询租赁订单
     * @param leaseOrderVO
     * @return
     */
    @PreAuthorize("@ss.hasPermi('order:order:list')")
    @GetMapping("/leaseList")
    public AjaxResult LeaseList(LeaseOrderVO leaseOrderVO){
        startPage();
        Map<String,Object> map = new HashMap<>();
        List<UserLeaseOrder> list = myLeaseOrderService.leaseOrderList(leaseOrderVO);
        map.put("leaseList",getDataTable(list));
        OrderSumAndMoneyVO orderSumAndMoneyVO = myLeaseOrderService.selectDayOrder(new LeaseOrderVO());
        map.put("todayOrder",orderSumAndMoneyVO);
        System.out.println(orderSumAndMoneyVO.getTodayOrderMoney()+"--------");
        ConditionOrderVO conditionOrderVO = myLeaseOrderService.selectConditionOrder(leaseOrderVO);
        map.put("conditionOrder",conditionOrderVO);
        return AjaxResult.success(map);
    }



    /**
     * 获取商品订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return AjaxResult.success(orderService.selectOrderByOrderId(orderId));
    }
    /**
     * 修改商品订单
     */
    @PreAuthorize("@ss.hasPermi('order:order:edit')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Order order)
    {
        return toAjax(orderService.updateOrder(order));
    }

    /**
     * 押金列表
     * @return
     */
    @GetMapping("/depositList")
    public TableDataInfo depositList(OrderDepositListVO orderDepositListVO){
        startPage();
        return getDataTable(orderService.depositList(orderDepositListVO));
    }

    @GetMapping("/depositSum")
    public AjaxResult depositSum(OrderDepositListVO orderDepositListVO){
        return AjaxResult.success(orderService.depositSum(orderDepositListVO));
    }

    //TODO 后台处理用户退款申请
    @PostMapping("/orderRefund")
    public AjaxResult orderRefund(@RequestBody UserLeaseOrder userLeaseOrder){
        return userRefundService.orderRefund(userLeaseOrder);
    }

    //TODO 后台押金退款
    @PostMapping("/depositRefund")
    public AjaxResult depositRefund(@RequestBody OrderDepositListVO orderDepositListVOt){
        return userRefundService.depositRefund(orderDepositListVOt);
    }

    //TODO 充值列表
    @GetMapping("/rechargeList")
    public TableDataInfo recharge(RechargeVO rechargeVO){
        startPage();
        return getDataTable(orderService.recharge(rechargeVO));
    }

    @GetMapping("/rechargeSum")
    public AjaxResult rechargeSum(RechargeVO rechargeVO){
        return AjaxResult.success(orderService.rechargeSum(rechargeVO));
    }

    //TODO 后台充值退款
    @PostMapping("/rechargeRefund")
    public AjaxResult rechargeRefund(@RequestBody RechargeVO rechargeVO){
        return userRefundService.rechargeRefund(rechargeVO);
    }
}
