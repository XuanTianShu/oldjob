package com.yuepei.service.impl;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.redis.RedisCache;
import com.yuepei.common.utils.uuid.UUID;
import com.yuepei.service.MyLeaseOrderService;
import com.yuepei.service.WechatCreateOrderService;
import com.yuepei.system.domain.*;
import com.yuepei.system.mapper.*;
import com.yuepei.utils.DictionaryEnum;
import com.yuepei.utils.RequestUtils;
import com.yuepei.utils.WXPayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private UserDiscountMapper userDiscountMapper;

//    @Autowired
//    private RedisServer redisServer;

    @Value("${coupon.prefix}")
    private String couponPre;



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

    @Transactional
    @Override
    public AjaxResult paymentPrepaymentOrder(String openid,UserLeaseOrder userLeaseOrder,Integer couponId) {
        System.out.println(userLeaseOrder.getPrice()+"===========金额==========");
        String notifyUrl = "https://www.yp10000.com/prod-api/wechat/user/order/paymentCallBack";
        UserDiscount userDiscount1 = null;
        if(couponId != null){
//            userCoupon = userCouponMapper.selectUserCouponById(Long.parseLong(couponId.toString()));
            userDiscount1 = userDiscountMapper.selectUserCouponById(Long.parseLong(couponId.toString()));
//            BigDecimal bigDecimal = new BigDecimal(userCoupon.getDiscountAmount());
//            BigDecimal subtract = userLeaseOrder.getPrice().subtract(bigDecimal);
//            price  = subtract.multiply(new BigDecimal(100));
            //修改使用后的优惠卷

            UserDiscount userDiscount = new UserDiscount();
            userDiscount.setId(Long.parseLong(String.valueOf(couponId)));
            userDiscount.setStatus(1L);
            userDiscountMapper.updateUserDiscount(userDiscount);
            UserLeaseOrder userLeaseOrder1 = new UserLeaseOrder();
            userLeaseOrder1.setOrderNumber(userLeaseOrder.getOrderNumber());
            userLeaseOrder1.setCouponPrice(new BigDecimal(String.valueOf(userDiscount1.getPrice())).multiply(new BigDecimal(100)).longValue());
            userLeaseOrderMapper.updateUserLeaseOrder(userLeaseOrder1);

//            redisServer.deleteObject(couponPre+couponId);
            log.info("优惠卷状态更新成功");
        }
        if (userLeaseOrder.getPrice().compareTo(BigDecimal.ZERO) > 0){
            HashMap<String, String> pay = wxPayUtils.sendPay(openid,userLeaseOrder.getPrice(), userLeaseOrder.getOrderNumber(),notifyUrl);
            if (pay != null){
                if (pay.get("message")==null){
                    // 存储 支付信息 回调支付成功 处理
                    HashMap hashMap = new HashMap();
                    hashMap.put("couponId",couponId);
                    hashMap.put("orderNumber",userLeaseOrder.getOrderNumber());
                    if(userDiscount1 != null ){
                        hashMap.put("couponPrice",new BigDecimal(String.valueOf(userDiscount1.getPrice())).multiply(new BigDecimal(100)) );
                    }
                    redisCache.setCacheMap(userLeaseOrder.getOrderNumber(),hashMap);
                    //修改租赁订单状态
//                UserLeaseOrder userOrder = new UserLeaseOrder();
//                userOrder.setOrderNumber(userLeaseOrder.getOrderNumber());
//                userOrder.setCreateTime(new Date());
                    //租借时长
//                userOrder.setPlayTime();
//                BigDecimal divide = new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100), 2);
//                userOrder.setNetAmount(Long.parseLong(String.valueOf(divide)));
//                userOrder.setNetAmount(Long.parseLong(price.toString()));
//                userOrder.setRestoreTime(new Date());
//                userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userOrder);
                }else {
                    return AjaxResult.error(pay.get("message"));
                }
            }
            System.out.println(pay+"========回调结果========");
            return AjaxResult.success(pay);
        }else {
            //实付金额
            userLeaseOrder.setNetAmount(new BigDecimal(String.valueOf(userLeaseOrder.getPrice())));
            //付款时间
            userLeaseOrder.setCreateTime(new Date());
            //支付方式
            userLeaseOrder.setPayType("0");
            //修改状态
            userLeaseOrder.setStatus("2");
            //支付成功押金订单为0
            userLeaseOrder.setDepositNumber("0");
            //修改用户租赁信息
            userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder);
            return AjaxResult.success();
        }
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
