package com.yuepei.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.redis.RedisCache;
import com.yuepei.common.exception.ServiceException;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.uuid.UUID;
import com.yuepei.domain.Amount;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.vo.*;
import com.yuepei.service.UserRefundService;
import com.yuepei.system.domain.UserDepositOrder;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.mapper.UserDepositDetailMapper;
import com.yuepei.system.mapper.UserDepositOrderMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.utils.RequestUtils;
import com.yuepei.utils.WXCallBackUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.*;

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
@Slf4j
@Service
public class UserRefundServiceImpl implements UserRefundService {

    @Autowired
    private UserDepositOrderMapper userDepositOrderMapper;

    @Autowired
    public CloseableHttpClient wxPayClient;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WXCallBackUtils wxCallBackUtils;

    @Autowired
    private RequestUtils requestUtils;

    @Value("${wechat.mchKey}")
    private String mchKey;

    @Autowired
    private UserDepositDetailMapper userDepositDetailMapper;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    private HashMap<String, String> hashMap = new HashMap<>();


    @Override
    public AjaxResult userRefund(String openid, String orderNumber) {

//        int i = userDepositOrderMapper.checkLeaseOrder(orderNumber);
//        if (i > 0){
//            return AjaxResult.error(DictionaryEnum.LEASE_ORDER.getName());
//        }
        //根据订单获取押金价格
        UserDepositOrder userDepositOrder = userDepositOrderMapper.selectUserDepositOrderByOrderNumber(orderNumber);

        //查询还有进行中和未支付的订单
        int i = userLeaseOrderMapper.selectUserOrderByDeviceNumber(userDepositOrder.getDeviceNumber(),openid);
        if (i > 0){
            return AjaxResult.success();
        }else {
            String outTradeNo = UUID.randomUUID().toString().replace("-", "");
            HashMap<Object, Object> payMap = new HashMap<>();
            payMap.put("out_trade_no", orderNumber);
            payMap.put("out_refund_no", outTradeNo);
            payMap.put("reason", "押金退款");
            payMap.put("notify_url","https://www.yp10000.com/prod-api/wechat/user/refund/userRefundCallBack");
            Amount amount = new Amount();
            long l = new BigDecimal(userDepositOrder.getDepositNum()).multiply(BigDecimal.valueOf(100)).longValue();
            amount.setTotal(l);
            amount.setRefund(l);
            amount.setCurrency("CNY");
            payMap.put("amount", amount);

            //请求地址
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
            // 请求数据
            Gson gson = new Gson();
            String json = gson.toJson(payMap);
            //设置请求信息
            StringEntity stringEntity = new StringEntity(json, "utf-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Accept", "application/json");
            // 3.完成签名并执行请求
            CloseableHttpResponse response = null;
            try {
                response = wxPayClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 4.解析response对象
            HashMap<String, String> resultMap = resolverResponse(response);
            if(resultMap != null) {
                //将订单修改为退款中，防止重复操作
                UserDepositOrder userDepositOrder1 = new UserDepositOrder();
                userDepositOrder1.setOrderNumber(orderNumber);
                userDepositOrder1.setStatus(3);
                userDepositOrderMapper.updateUserDepositOrder(userDepositOrder1);
                //微信退款单号   --  等待后续操作
                String refund_id = resultMap.get("refund_id");
                String transaction_id = resultMap.get("transaction_id");
                String out_trade_no = resultMap.get("out_trade_no");
                String status = resultMap.get("status");
                System.out.println(userDepositOrder.getOrderNumber()+"--"+out_trade_no+"--"+status);
//            userDepositOrder.setDeviceStatus("1");
//            userDepositOrderMapper.updateUserDepositOrder(userDepositOrder);
                return AjaxResult.success();
            }
        }
        return null;
    }

    @Transactional
    @Override
    public HashMap<String, String> userRefundCallBack(HttpServletRequest request) throws GeneralSecurityException {
        System.out.println("退款");
        HashMap params = requestUtils.requestParams(request);
        Map resource = (Map) params.get("resource");

        Object stringBuffer = params.get("stringBuffer");
        String associated_data = (String) resource.get("associated_data");
        String nonce = (String) resource.get("nonce");
        String ciphertext = (String) resource.get("ciphertext");

        JSONObject parseObject;
        boolean isTrue = wxCallBackUtils.parseWXCallBack(request, stringBuffer.toString());
        if (isTrue) {
            AesUtil aesUtil = new AesUtil(mchKey.getBytes());
                parseObject = JSONObject.parseObject(
                        aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext)
                );
                //商户订单号
                String out_trade_no = (String) parseObject.get("out_trade_no");
                String status = (String) parseObject.get("refund_status");
                Object amount = parseObject.get("amount");
                JSONObject jsonObject1 = JSONObject.parseObject(amount.toString());
                Object price = jsonObject1.get("payer_refund");
                if (status.equals("SUCCESS")){
                    //根据商户订单号查询到用户信息
                    UserDepositOrder userDepositOrder = userDepositOrderMapper.selectUserDepositOrderByOrderNumber(out_trade_no);
                    //新增用户押金详细
                    UserIntegralBalanceDepositVo userIntegralBalanceDepositVo = new UserIntegralBalanceDepositVo();
                    userIntegralBalanceDepositVo.setOpenid(userDepositOrder.getOpenid());
                    userIntegralBalanceDepositVo.setStatus(1);
                    userIntegralBalanceDepositVo.setOrderNumber(out_trade_no);
                    userIntegralBalanceDepositVo.setSum(new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100),2));
                    userIntegralBalanceDepositVo.setCreateTime(DateUtils.getNowDate());
                    userDepositDetailMapper.insertUserDepositDetail(userIntegralBalanceDepositVo);

                    //修改用户押金详细
                    UserDepositOrder userDepositOrderList = new UserDepositOrder();
                    userDepositOrderList.setDepositStatus(1);
                    userDepositOrderList.setStatus(1);
                    userDepositOrderList.setOrderNumber(out_trade_no);
                    userDepositOrderList.setUpdateTime(new Date());
                    userDepositOrderMapper.updateUserDepositOrder(userDepositOrderList);
                }else {
                    //修改用户押金详细
                    UserDepositOrder userDepositOrderList = new UserDepositOrder();
                    userDepositOrderList.setStatus(0);
                    userDepositOrderList.setDepositStatus(1);
                    userDepositOrderList.setOrderNumber(out_trade_no);
                    userDepositOrderList.setUpdateTime(new Date());
                    userDepositOrderMapper.updateUserDepositOrder(userDepositOrderList);
                }

                //响应接口
                hashMap.put("code", "SUCCESS");
                hashMap.put("message", "成功");
            return hashMap;
        }else {
            hashMap.put("code", "FAIL");
            hashMap.put("message", "失败");
            return hashMap;
        }
    }

    /**
     * 用户退押金
     * @param openid
     * @return
     */
    @Override
    public AjaxResult userDepositRefund(String openid) {
        System.out.println(openid+"用户标识");
        //查询该用户缴纳的押金
        List<UserDepositVO> userDepositVOList =  userDepositOrderMapper.selectUserDepositList(openid);
        //查询订单绑定中的押金
        List<UserOrderVO> userOrderVOList = userLeaseOrderMapper.selectUserOrderDepositList(openid);

        if (userDepositVOList.size() != 0 && userOrderVOList.size() != 0){
            for (UserOrderVO userOrderVO : userOrderVOList) {
                for (int k = userDepositVOList.size() - 1; k >= 0; k--) {
                    if (userOrderVO.getDepositNumber().equals(userDepositVOList.get(k).getOrderNumber())){
                        userDepositVOList.remove(k);
                    }
                }
            }
        }
        List<String> list = new ArrayList<>();
        if (userDepositVOList.size() != 0){
            System.out.println("更新状态");
            userDepositOrderMapper.bathUpdateUserDeposit(userDepositVOList);
            System.out.println("更新成功");
            for (UserDepositVO userDepositVO : userDepositVOList) {
                String outTradeNo = UUID.randomUUID().toString().replace("-", "");
                HashMap<Object, Object> payMap = new HashMap<>();
                payMap.put("out_trade_no", userDepositVO.getOrderNumber());
                payMap.put("out_refund_no", outTradeNo);
                payMap.put("reason", "押金退款");
                payMap.put("notify_url", "https://www.yp10000.com/prod-api/wechat/user/refund/userRefundCallBack");
                Amount amount = new Amount();
                long l = new BigDecimal(String.valueOf(userDepositVO.getDepositNum())).multiply(BigDecimal.valueOf(100)).longValue();
                amount.setTotal(l);
                amount.setRefund(l);
                amount.setCurrency("CNY");
                payMap.put("amount", amount);

                //请求地址
                HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
                // 请求数据
                Gson gson = new Gson();
                String json = gson.toJson(payMap);
                //设置请求信息
                StringEntity stringEntity = new StringEntity(json, "utf-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                httpPost.setHeader("Accept", "application/json");
                // 3.完成签名并执行请求
                CloseableHttpResponse response = null;
                try {
                    response = wxPayClient.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 4.解析response对象
                HashMap<String, String> resultMap = resolverResponse(response);
                if (resultMap != null) {
                    //微信退款单号   --  等待后续操作
                    //TODO 退款记录
                    String refund_id = resultMap.get("refund_id");
                    String transaction_id = resultMap.get("transaction_id");
                    String out_trade_no = resultMap.get("out_trade_no");
                    list.add(out_trade_no);
                }
            }
            if (list.size() > 0){
                for (String s : list) {
                    for (int k = userDepositVOList.size() - 1; k >= 0; k--) {
                        if (s.equals(userDepositVOList.get(k).getOrderNumber())){
                            userDepositVOList.remove(k);
                        }
                    }
                }
                if (userDepositVOList.size() > 0){
                    userDepositOrderMapper.bathUpdateUserDeposits(userDepositVOList);
                }
            }else {
                userDepositOrderMapper.bathUpdateUserDeposits(userDepositVOList);
            }
            if (userOrderVOList.size() > 0){
                return AjaxResult.success("有正在租赁中的订单，完成订单后可申请退回押金，已完成的订单已申请退回押金");
            }else {
                return AjaxResult.success("已申请退回押金，请注意查收");
            }
        }else {
            return AjaxResult.error("暂无可退押金！");
        }
    }

    @Override
    public AjaxResult unlocking(HttpServletRequest request) {
        try {
            String reader = getAllRequestParam2(request);
            log.info("开锁数据上报:============={}", reader);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("开锁数据上报执行事务回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("开锁数据上报执行回滚成功");
        }
        return null;
    }

    @Transactional
    @Override
    public AjaxResult orderRefund(UserLeaseOrder userLeaseOrder) {
        long l = new BigDecimal(String.valueOf(userLeaseOrder.getNetAmount())).multiply(BigDecimal.valueOf(100)).longValue();
        log.info("{}",userLeaseOrder.getNetAmount());
        if (l <= 0){
            UserLeaseOrder userLeaseOrder1 = new UserLeaseOrder();
            userLeaseOrder1.setOrderNumber(userLeaseOrder.getOrderNumber());
            userLeaseOrder1.setStatus("4");
            userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder1);
        }else {
            if (userLeaseOrder.getPayType().equals("2")){
                //TODO 优惠券退回
                //TODO 扣除分成金额
                log.info(userLeaseOrder.getCouponId()+"优惠券id");
                log.info(userLeaseOrder.getOrderNumber()+"订单号");
                SysUser sysUser = sysUserMapper.selectUserByOpenid(userLeaseOrder.getOpenid());
                BigDecimal balance = sysUser.getBalance();
                BigDecimal add = balance.add(userLeaseOrder.getNetAmount());
                sysUser.setBalance(add);
                sysUserMapper.updateUser(sysUser);

                UserLeaseOrder userLeaseOrder1 = new UserLeaseOrder();
                userLeaseOrder1.setOrderNumber(userLeaseOrder.getOrderNumber());
                userLeaseOrder1.setStatus("4");
                userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder1);
            }else if (userLeaseOrder.getPayType().equals("0")){
                log.info("后台手动退款");
                //将订单修改为退款中，防止重复操作
                UserLeaseOrder userLeaseOrder1 = new UserLeaseOrder();
                userLeaseOrder1.setOrderNumber(userLeaseOrder.getOrderNumber());
                userLeaseOrder1.setStatus("3");
                userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder1);
                String outTradeNo = UUID.randomUUID().toString().replace("-", "");
                HashMap<Object, Object> payMap = new HashMap<>();
                payMap.put("out_trade_no", userLeaseOrder.getOrderNumber());
                payMap.put("out_refund_no", outTradeNo);
                payMap.put("reason", "订单退款");
                payMap.put("notify_url","https://www.yp10000.com/prod-api/wechat/user/refund/orderRefundCallBack");
                Amount amount = new Amount();
                amount.setTotal(l);
                amount.setRefund(l);
                amount.setCurrency("CNY");
                payMap.put("amount", amount);

                //请求地址
                HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
                // 请求数据
                Gson gson = new Gson();
                String json = gson.toJson(payMap);
                //设置请求信息
                StringEntity stringEntity = new StringEntity(json, "utf-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                httpPost.setHeader("Accept", "application/json");
                // 3.完成签名并执行请求
                CloseableHttpResponse response = null;
                try {
                    response = wxPayClient.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 4.解析response对象
                HashMap<String, String> resultMap = resolverResponse(response);
                if(resultMap != null) {
                    //微信退款单号   --  等待后续操作
                    String refund_id = resultMap.get("refund_id");
                    String transaction_id = resultMap.get("transaction_id");
                    String out_trade_no = resultMap.get("out_trade_no");
                    System.out.println(userLeaseOrder.getOrderNumber() + "--" + out_trade_no);
//            userDepositOrder.setDeviceStatus("1");
//            userDepositOrderMapper.updateUserDepositOrder(userDepositOrder);
                }
            }
        }

        return AjaxResult.success();
    }

    @Transactional
    @Override
    public HashMap<String, String> orderRefundCallBack(HttpServletRequest request) throws GeneralSecurityException {
        System.out.println("订单退款");
        HashMap params = requestUtils.requestParams(request);
        Map resource = (Map) params.get("resource");

        Object stringBuffer = params.get("stringBuffer");
        String associated_data = (String) resource.get("associated_data");
        String nonce = (String) resource.get("nonce");
        String ciphertext = (String) resource.get("ciphertext");

        JSONObject parseObject;
        boolean isTrue = wxCallBackUtils.parseWXCallBack(request, stringBuffer.toString());
        if (isTrue) {
            AesUtil aesUtil = new AesUtil(mchKey.getBytes());
            parseObject = JSONObject.parseObject(
                    aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext)
            );
            //商户订单号
            log.info("订单：{}",parseObject.toString());
            String out_trade_no = (String) parseObject.get("out_trade_no");
            String status = (String) parseObject.get("refund_status");
            if (status.equals("SUCCESS")) {
//                Map<String, Object> cacheMap = redisCache.getCacheMap(out_trade_no);
//                log.info("测试：{}",cacheMap.toString());
                //TODO 优惠券退回
                //TODO 扣除分成金额
//                log.info(userLeaseOrder.getCouponId()+"优惠券id");
                log.info(out_trade_no+"订单号");

                UserLeaseOrder userLeaseOrder = new UserLeaseOrder();
                userLeaseOrder.setOrderNumber(out_trade_no);
                userLeaseOrder.setStatus("4");
                userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder);
            }else {
                UserLeaseOrder userLeaseOrder = new UserLeaseOrder();
                userLeaseOrder.setOrderNumber(out_trade_no);
                userLeaseOrder.setStatus("2");
                userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder);
            }

            //响应接口
            hashMap.put("code", "SUCCESS");
            hashMap.put("message", "成功");
            return hashMap;
        }else {
            hashMap.put("code", "FAIL");
            hashMap.put("message", "失败");
            return hashMap;
        }
    }

    @Override
    public AjaxResult depositRefund(OrderDepositListVO orderDepositListVOt) {
        log.info("后台押金手动退款");
        long l = new BigDecimal(String.valueOf(orderDepositListVOt.getDepositNum())).multiply(BigDecimal.valueOf(100)).longValue();
        String outTradeNo = UUID.randomUUID().toString().replace("-", "");
        HashMap<Object, Object> payMap = new HashMap<>();
        payMap.put("out_trade_no", orderDepositListVOt.getOrderNumber());
        payMap.put("out_refund_no", outTradeNo);
        payMap.put("reason", "押金退款");
        payMap.put("notify_url","https://www.yp10000.com/prod-api/wechat/user/refund/depositRefundCallback");
        Amount amount = new Amount();
        amount.setTotal(l);
        amount.setRefund(l);
        amount.setCurrency("CNY");
        payMap.put("amount", amount);

        //请求地址
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
        // 请求数据
        Gson gson = new Gson();
        String json = gson.toJson(payMap);
        //设置请求信息
        StringEntity stringEntity = new StringEntity(json, "utf-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Accept", "application/json");
        // 3.完成签名并执行请求
        CloseableHttpResponse response = null;
        try {
            response = wxPayClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 4.解析response对象
        HashMap<String, String> resultMap = resolverResponse(response);
        if(resultMap != null) {
            //微信退款单号   --  等待后续操作
            String refund_id = resultMap.get("refund_id");
            String transaction_id = resultMap.get("transaction_id");
            String out_trade_no = resultMap.get("out_trade_no");
            String status = resultMap.get("status");
            //将押金订单修改为退款中，防止重复操作
            orderDepositListVOt.setStatus("3");
            userDepositOrderMapper.updateDepositStatus(orderDepositListVOt);
            System.out.println(orderDepositListVOt.getOrderNumber() + "--" + out_trade_no + "--" + status);
//            userDepositOrder.setDeviceStatus("1");
//            userDepositOrderMapper.updateUserDepositOrder(userDepositOrder);
            return AjaxResult.success();
        }
        return null;
    }

    @Transactional
    @Override
    public HashMap<String, String> depositRefundCallback(HttpServletRequest request) throws GeneralSecurityException {
        System.out.println("押金退款回调");
        HashMap params = requestUtils.requestParams(request);
        Map resource = (Map) params.get("resource");

        Object stringBuffer = params.get("stringBuffer");
        String associated_data = (String) resource.get("associated_data");
        String nonce = (String) resource.get("nonce");
        String ciphertext = (String) resource.get("ciphertext");

        JSONObject parseObject;
        boolean isTrue = wxCallBackUtils.parseWXCallBack(request, stringBuffer.toString());
        if (isTrue) {
            AesUtil aesUtil = new AesUtil(mchKey.getBytes());
            parseObject = JSONObject.parseObject(
                    aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext)
            );
            //商户订单号
            log.info("押金：{}",parseObject.toString());
            String out_trade_no = (String) parseObject.get("out_trade_no");
            String status = (String) parseObject.get("refund_status");
            Object amount = parseObject.get("amount");
            JSONObject jsonObject1 = JSONObject.parseObject(amount.toString());
            Object price = jsonObject1.get("payer_refund");
            if (status.equals("SUCCESS")) {
                //修改押金订单
                OrderDepositListVO orderDepositListVO = new OrderDepositListVO();
                orderDepositListVO.setOrderNumber(out_trade_no);
                orderDepositListVO.setDepositStatus("1");
                orderDepositListVO.setStatus("1");
                userDepositOrderMapper.updateDepositStatus(orderDepositListVO);
                //添加押金明细
                //根据商户订单号查询到用户信息
                UserDepositOrder userDepositOrder = userDepositOrderMapper.selectUserDepositOrderByOrderNumber(out_trade_no);
                //新增用户押金详细
                UserIntegralBalanceDepositVo userIntegralBalanceDepositVo = new UserIntegralBalanceDepositVo();
                userIntegralBalanceDepositVo.setOpenid(userDepositOrder.getOpenid());
                userIntegralBalanceDepositVo.setStatus(1);
                userIntegralBalanceDepositVo.setOrderNumber(out_trade_no);
                userIntegralBalanceDepositVo.setSum(new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100),2));
                userIntegralBalanceDepositVo.setCreateTime(DateUtils.getNowDate());
                userDepositDetailMapper.insertUserDepositDetail(userIntegralBalanceDepositVo);
            }else {
                //修改押金订单
                OrderDepositListVO orderDepositListVO = new OrderDepositListVO();
                orderDepositListVO.setOrderNumber(out_trade_no);
                orderDepositListVO.setDepositStatus("0");
                orderDepositListVO.setStatus("1");
                userDepositOrderMapper.updateDepositStatus(orderDepositListVO);
            }

            log.info("订单号：{}",out_trade_no);
            //响应接口
            hashMap.put("code", "SUCCESS");
            hashMap.put("message", "成功");
            return hashMap;
        }else {
            hashMap.put("code", "FAIL");
            hashMap.put("message", "失败");
            return hashMap;
        }
    }

    @Transactional
    @Override
    public AjaxResult rechargeRefund(RechargeVO rechargeVO) {

        return null;
    }


    public static String getAllRequestParam2(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str;
        StringBuilder wholeStr = new StringBuilder();
        while((str = br.readLine()) != null){
            wholeStr.append(str);
        }
        return wholeStr.toString();
    }




    /**
     * 解析响应数据
     *
     * @param response 发送请求成功后，返回的数据
     * @return 微信返回的参数
     */
    private static HashMap<String, String> resolverResponse(CloseableHttpResponse response) {
        try {
            // 1.获取请求码
            int statusCode = response.getStatusLine().getStatusCode();
            // 2.获取返回值 String 格式
            final String bodyAsString = EntityUtils.toString(response.getEntity());
            Gson gson = new Gson();
            if (statusCode == 200) {
                // 3.如果请求成功则解析成Map对象返回
                HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
                return resultMap;
            } else {
                if (StringUtils.isNoneBlank(bodyAsString)) {
                    log.error("退款请求失败，提示信息:{}", bodyAsString);
                    // 4.请求码显示失败，则尝试获取提示信息
                    HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
                    throw new ServiceException(resultMap.get("message"));
                }
                log.error("退款请求失败，未查询到原因，提示信息:{}", response);
                // 其他异常，微信也没有返回数据，这就需要具体排查了
                throw new IOException("request failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
