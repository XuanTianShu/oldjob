package com.yuepei.web.controller.order;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.service.MyLeaseOrderService;
import com.yuepei.system.domain.Order;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.vo.LeaseOrderVO;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public TableDataInfo LeaseList(LeaseOrderVO leaseOrderVO){
        startPage();
        List<UserLeaseOrder> list = myLeaseOrderService.leaseOrderList(leaseOrderVO);
        return getDataTable(list);
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
}
