package com.yuepei.service.impl;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.redis.RedisCache;
import com.yuepei.common.utils.uuid.UUID;
import com.yuepei.service.MyLeaseOrderService;
import com.yuepei.service.WechatCreateOrderService;
import com.yuepei.system.domain.UserCoupon;
import com.yuepei.system.domain.UserDepositOrder;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.UserPayOrder;
import com.yuepei.system.mapper.*;
import com.yuepei.utils.DictionaryEnum;
import com.yuepei.utils.RequestUtils;
import com.yuepei.utils.WXPayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
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
 * @create ：2022/12/23 14:31
 **/
@Slf4j
@Service
public class WechatCreateOrderServiceImpl implements WechatCreateOrderService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private UserPayOrderMapper userPayOrderMapper;

    @Autowired
    private UserDepositOrderMapper userDepositOrderMapper;

    @Autowired
    private WXPayUtils wxPayUtils;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private RequestUtils requestUtils;

    @Override
    public AjaxResult payPrepaymentOrder(Long userId, Long price) {
        String outTradeNo = UUID.randomUUID().toString().replace("-", "");
        SysUser user = userMapper.selectUserById(userId);
        String notifyUrl = "https://www.yp10000.com/prod-api/wechat/user/order/payCallBack";
        HashMap<String, String> pay = wxPayUtils.sendPay(user.getOpenid(), new BigDecimal(price), outTradeNo,notifyUrl);
        if (pay != null){
            if (pay.get("message")==null){
                //创建 充值 预支付订单
                UserPayOrder userPayOrder = new UserPayOrder();
                userPayOrder.setOpenid(user.getOpenid());
                userPayOrder.setOutTradeNo(outTradeNo);
                BigDecimal divide = new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100), 2);
//                userPayOrder.setPrice(Long.parseLong(String.valueOf(divide)));
                userPayOrder.setPrice(Long.parseLong(price.toString()));
                userPayOrder.setStatus(0);
                userPayOrder.setEndTime(new Date());
                userPayOrderMapper.insertUserPayOrder(userPayOrder);
            }else {
                return AjaxResult.error(pay.get("message"));
            }
        }
        return AjaxResult.success(pay);
    }

    @Override
    public AjaxResult depositPrepaymentOrder(Long userId, Long price,String deviceNumber) {
        String outTradeNo = UUID.randomUUID().toString().replace("-", "");
        SysUser user = userMapper.selectUserById(userId);
        String notifyUrl = "https://www.yp10000.com/prod-api/wechat/user/order/depositCallBack";
        HashMap<String, String> deposit = wxPayUtils.sendPay(user.getOpenid(), new BigDecimal(price), outTradeNo,notifyUrl);
        if (deposit != null){
            if (deposit.get("message")==null){
                //创建 押金 预支付订单
                UserDepositOrder userDepositOrder = new UserDepositOrder();
                userDepositOrder.setDeviceNumber(deviceNumber);
                BigDecimal divide = new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100), 2);
//                userDepositOrder.setDepositNum(Long.parseLong(String.valueOf(divide)));
                userDepositOrder.setDepositNum(Long.parseLong(price.toString()));
                userDepositOrder.setOpenid(user.getOpenid());
                userDepositOrder.setDepositStatus(0);
                userDepositOrder.setOrderNumber(outTradeNo);
                userDepositOrder.setStatus(0);
                userDepositOrder.setCreateTime(new Date());
                userDepositOrderMapper.insertUserDepositOrder(userDepositOrder);
            }else {
                return AjaxResult.error(deposit.get("message"));
            }
        }
        return AjaxResult.success(deposit);
    }

    @Override
    public AjaxResult paymentPrepaymentOrder(String openid,UserLeaseOrder userLeaseOrder,Integer couponId) {
        String notifyUrl = "https://www.yp10000.com/prod-api/wechat/user/order/paymentCallBack";
        Long price = null;
        UserCoupon userCoupon = null;
        if(couponId == null){
//            price  = userLeaseOrder.getPrice();
        }else {
            userCoupon = userCouponMapper.selectUserCouponById(Long.parseLong(couponId.toString()));
//            price  = userLeaseOrder.getPrice() - userCoupon.getDiscountAmount()*100;
//            TODO 修改使用后的优惠卷
        log.info("优惠卷状态更新成功");
        }
        HashMap<String, String> pay = wxPayUtils.sendPay(openid,userLeaseOrder.getPrice(), userLeaseOrder.getOrderNumber(),notifyUrl);
        if (pay != null){
            if (pay.get("message")==null){
                // 存储 支付信息 回调支付成功 处理
                HashMap hashMap = new HashMap();
                hashMap.put("couponId",couponId);
                hashMap.put("orderNumber",userLeaseOrder.getOrderNumber());
                if(userCoupon != null ){
                    hashMap.put("couponPrice",userCoupon.getDiscountAmount() * 100 );
                }
                redisCache.setCacheMap(userLeaseOrder.getOrderNumber(),hashMap);


                //修改租赁订单状态
                UserLeaseOrder userOrder = new UserLeaseOrder();
                userOrder.setOrderNumber(userLeaseOrder.getOrderNumber());
                userOrder.setStatus(String.valueOf(DictionaryEnum.ORDER_STATUS_2.getCode()));
                userOrder.setCreateTime(new Date());
                //租借时长
//                userOrder.setPlayTime();
                BigDecimal divide = new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100), 2);
//                userOrder.setNetAmount(Long.parseLong(String.valueOf(divide)));
//                userOrder.setNetAmount(Long.parseLong(price.toString()));
                userOrder.setRestoreTime(new Date());
                userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userOrder);
            }else {
                return AjaxResult.error(pay.get("message"));
            }
        }
        return AjaxResult.success(pay);
    }

    @Override
    public AjaxResult weChatWithdrawal(String openid, Long amount, String remark, String bankMemo) {
        SysUser user = userMapper.selectUserByOpenid(openid);
        HashMap<String, String> pay = wxPayUtils.withdrawal(openid, amount, remark, bankMemo);
        System.out.println("pay======================================"+pay);
        if (user.getBalance() < amount * 100) {
            return AjaxResult.error("提现金额不能大于余额");
        }
        return AjaxResult.success(pay);
    }
}
