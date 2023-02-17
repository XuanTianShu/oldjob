package com.yuepei.controller.wechat;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.service.CallBackService;
import com.yuepei.service.UserRefundService;
import com.yuepei.service.WechatCreateOrderService;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.ParseException;
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
 * @create ：2022/12/19 10:48
 **/
@RestController
@RequestMapping("/wechat/user/order")
public class WechatCreateOrderController {

    @Autowired
    private WechatCreateOrderService createOrderService;

    @Autowired
    private CallBackService callBackService;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * 充值
     * @param request
     * @param price
     * @return
     */
    @PostMapping("/payPrepaymentOrder")
    public AjaxResult payPrepaymentOrder(HttpServletRequest request ,Long price){
        SysUser user = tokenUtils.analysis(request);
        return createOrderService.payPrepaymentOrder(user.getUserId(),price);
    }

    /**
     * 充值回调
     * @param request
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws ParseException
     */
    @PostMapping("/payCallBack")
    public HashMap<String, String> payCallBack(HttpServletRequest request) throws IOException, GeneralSecurityException, ParseException {
        return callBackService.payCallBack(request);
    }

    /**
     * 押金
     * @param request
     * @param price
     * @param deviceNumber
     * @return
     */
    @PostMapping("/depositPrepaymentOrder")
    public AjaxResult depositPrepaymentOrder(HttpServletRequest request,Long price,String deviceNumber){
        SysUser user = tokenUtils.analysis(request);
        return createOrderService.depositPrepaymentOrder(user.getUserId(),price,deviceNumber);
    }

    /**
     * 押金回调
     * @param request
     * @return
     * @throws GeneralSecurityException
     */
    @PostMapping("/depositCallBack")
    public HashMap<String, String> depositCallBack(HttpServletRequest request) throws GeneralSecurityException {
        return callBackService.depositCallBack(request);
    }


    /**
     * 商品购买及租赁
     * @param request
     * @param userLeaseOrder
     * @param couponId
     * @return
     */
    @PostMapping("/paymentPrepaymentOrder")
    public AjaxResult paymentPrepaymentOrder(HttpServletRequest request,@RequestBody UserLeaseOrder userLeaseOrder, Integer couponId){
        SysUser user = tokenUtils.analysis(request);
        return createOrderService.paymentPrepaymentOrder(user.getOpenid(),userLeaseOrder,couponId);
    }

    /**
     * 租赁回调
     * @param request
     * @return
     * @throws GeneralSecurityException
     */
    @PostMapping("/paymentCallBack")
    public HashMap<String, String> paymentCallBack(HttpServletRequest request) throws GeneralSecurityException {
        return callBackService.paymentCallBack(request);
    }


    /**
     * 余额支付
     * @param request
     * @param userLeaseOrder
     * @param couponId
     * @return
     */
    @PostMapping("/balancePrepaymentOrder")
    public AjaxResult balancePrepaymentOrder(HttpServletRequest request,UserLeaseOrder userLeaseOrder,long couponId){
        SysUser user = tokenUtils.analysis(request);
        return callBackService.balancePrepaymentOrder(user.getOpenid(),couponId,userLeaseOrder);
    }
}
