package com.yuepei.service.impl;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.redis.RedisCache;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.utils.UsrDemo;
import com.yuepei.service.CallBackService;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.ItemVO;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.mapper.*;
import com.yuepei.system.utils.RedisServer;
import com.yuepei.utils.RequestUtils;
import com.yuepei.utils.WXCallBackUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.security.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
 * @create ：2023/1/5 16:31
 **/
@Slf4j
@Service
public class CallBackServiceImpl implements CallBackService {

    @Value("${wechat.mchKey}")
    private String mchKey;

    @Autowired
    private WXCallBackUtils wxCallBackUtils;

    @Autowired
    private RequestUtils requestUtils;

    @Autowired
    private UserPayOrderMapper userPayOrderMapper;

    @Autowired
    private PayTypeMapper payTypeMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserIntegralOrderMapper userIntegralOrderMapper;

    @Autowired
    private UserBalanceDetailMapper userBalanceDetailMapper;

    @Autowired
    private UserDepositOrderMapper userDepositOrderMapper;

    @Autowired
    private UserDepositDetailMapper userDepositDetailMapper;

    @Autowired
    private  UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private UserDiscountMapper userDiscountMapper;

    @Autowired
    private OrderProportionMapper orderProportionMapper;

    @Autowired
    private RedisServer redisServer;

    @Value("${coupon.order}")
    private String orderPrefix;

    @Value("${coupon.valid}")
    private String orderValid;

    @Value("${telecom.secret}")
    private String secret;

    @Value("${telecom.application}")
    private String application;

    @Value("${telecom.domain}")
    private String domain;

    @Autowired
    private DeviceMapper deviceMapper;

    private HashMap<String, String> hashMap = new HashMap<>();


    @Transactional
    @Override
    public HashMap<String, String> payCallBack(HttpServletRequest request) throws GeneralSecurityException {
        HashMap params = requestUtils.requestParams(request);

        log.info("充值回调");
        Map resource = (Map) params.get("resource");

        Object stringBuffer = params.get("stringBuffer");
        String associated_data = (String) resource.get("associated_data");
        String nonce = (String) resource.get("nonce");
        String ciphertext = (String) resource.get("ciphertext");

        boolean isTrue = wxCallBackUtils.parseWXCallBack(request, stringBuffer.toString());

        UserPayOrder userPayOrder = new UserPayOrder();
        UserIntegralBalanceDepositVo userBalanceDetail = new UserIntegralBalanceDepositVo();

        JSONObject parseObject;
        if (isTrue) {
            AesUtil aesUtil = new AesUtil(mchKey.getBytes());
            parseObject = JSONObject.parseObject(
                    aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext)
            );
            Object payer = parseObject.get("payer");
            JSONObject jsonObject = JSONObject.parseObject(payer.toString());
            Object openid = jsonObject.get("openid");
            if ("SUCCESS".equals(parseObject.get("trade_state"))) {
                Object success_time = parseObject.get("success_time");
                LocalDateTime parse = LocalDateTime.parse(success_time.toString(), DateTimeFormatter.ISO_DATE_TIME);
                String out_trade_no = (String) parseObject.get("out_trade_no");
                Date time = Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
                userPayOrder.setEndTime(time);
                Object amount = parseObject.get("amount");
                JSONObject jsonObject1 = JSONObject.parseObject(amount.toString());
                Object price = jsonObject1.get("payer_total");


                log.info("更新余额");
                SysUser user1 = userMapper.selectUserByOpenid(openid.toString());
                BigDecimal balance = user1.getBalance();
                BigDecimal divide = new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100),2);
                user1.setBalance(balance.add(divide));
                userMapper.updateUser(user1);
                log.info("更新成功");

                //用户充值订单
                userPayOrder.setOpenid(openid.toString());
                userPayOrder.setStatus(1);
                userPayOrder.setOutTradeNo(out_trade_no);
                userPayOrderMapper.updateUserPayOrder(userPayOrder);


                //用户余额明细
                userBalanceDetail.setOpenid(openid.toString());
//                BigDecimal divide1 = new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100), 2);
//                userBalanceDetail.setSum(new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100),2));
                userBalanceDetail.setSum(divide);
                userBalanceDetail.setStatus(0);
                userBalanceDetail.setCreateTime(DateUtils.getNowDate());
                userBalanceDetailMapper.insertUserBalanceDetail(userBalanceDetail);

                List<PayType> payTypes = payTypeMapper.selectPayTypeAll();

                for (int i = 0; i < payTypes.size(); i++) {
                    //如果充值金额 等于 系统设置的对应的金额 则赠送 积分
                    if( divide.longValue() == payTypes.get(i).getPaySum()){
                        SysUser user = new SysUser();
                        user.setIntegral(payTypes.get(i).getIntegral());
//                        user.setBalance(Long.parseLong(String.valueOf(new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100),2))));
//                        user.setBalance(new BigDecimal(price.toString()));
                        user.setOpenid(openid.toString());
                        if(userMapper.updateUserIntegralByOpenid(user)>0){
                            //赠送积分成功  记录 赠送积分明细
                            UserIntegralBalanceDepositVo userIntegralDetail = new UserIntegralBalanceDepositVo();
                            userIntegralDetail.setOpenid(openid.toString());
                            userIntegralDetail.setSum(new BigDecimal(payTypes.get(i).getIntegral()));
                            userIntegralDetail.setStatus(0);
                            userIntegralDetail.setCreateTime(DateUtils.getNowDate());
                            userIntegralOrderMapper.insertUserIntegralOrder(userIntegralDetail);
                        }
                    }
                }
                //响应接口
                hashMap.put("code", "SUCCESS");
                hashMap.put("message", "成功");
                return hashMap;
            }else {
                String out_trade_no = (String) parseObject.get("out_trade_no");
                userPayOrder.setOpenid(openid.toString());
                userPayOrder.setStatus(2);
                userPayOrder.setOutTradeNo(out_trade_no);
                userPayOrder.setErrMsg("trade_state_desc");
                userPayOrderMapper.updateUserPayOrder(userPayOrder);
                hashMap.put("code", "FAIL");
                hashMap.put("message", "失败");
                return hashMap;
            }
        }else {
            hashMap.put("code", "FAIL");
            hashMap.put("message", "失败");
            return hashMap;
        }
    }

    @Transactional
    @Override
    public HashMap<String, String> depositCallBack(HttpServletRequest request) throws GeneralSecurityException {
        HashMap params = requestUtils.requestParams(request);
        Map resource = (Map) params.get("resource");
        Object stringBuffer = params.get("stringBuffer");
        String associated_data = (String) resource.get("associated_data");
        String nonce = (String) resource.get("nonce");
        String ciphertext = (String) resource.get("ciphertext");

        UserDepositOrder userDepositOrder = new UserDepositOrder();
        boolean isTrue = wxCallBackUtils.parseWXCallBack(request, stringBuffer.toString());
        JSONObject parseObject;
        if (isTrue) {
            AesUtil aesUtil = new AesUtil(mchKey.getBytes());
            parseObject = JSONObject.parseObject(
                    aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext)
            );
            Object payer = parseObject.get("payer");
            JSONObject jsonObject = JSONObject.parseObject(payer.toString());
            Object openid = jsonObject.get("openid");
            //支付时间
            Object success_time = parseObject.get("success_time");
            LocalDateTime parse = LocalDateTime.parse(success_time.toString(), DateTimeFormatter.ISO_DATE_TIME);
            Date time = Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
            //订单号
            log.info("parseObject:{}",parseObject.toString());
            String out_trade_no = (String) parseObject.get("out_trade_no");
            String transaction_id = (String) parseObject.get("transaction_id");
            if ("SUCCESS".equals(parseObject.get("trade_state"))) {
                //支付成功  修改订单状态
                userDepositOrder.setCreateTime(time);
                userDepositOrder.setOrderNumber(out_trade_no);
                userDepositOrder.setStatus(1);
                userDepositOrder.setTransactionId(transaction_id);
                userDepositOrderMapper.updateUserDepositOrder(userDepositOrder);
                //新增 押金详细
                UserIntegralBalanceDepositVo userIntegralBalanceDepositVo = new UserIntegralBalanceDepositVo();
                Object amount = parseObject.get("amount");
                JSONObject jsonObject1 = JSONObject.parseObject(amount.toString());
                Object price = jsonObject1.get("payer_total");
                //记录用户押金详细
                userIntegralBalanceDepositVo.setOrderNumber(out_trade_no);
                userIntegralBalanceDepositVo.setOpenid(openid.toString());
//                userIntegralBalanceDepositVo.setSum(new BigDecimal(price.toString()));
                userIntegralBalanceDepositVo.setSum(new BigDecimal(price.toString()).divide(new BigDecimal(100)));
                userIntegralBalanceDepositVo.setStatus(0);
                userIntegralBalanceDepositVo.setCreateTime(new Date());
                userDepositDetailMapper.insertUserDepositDetail(userIntegralBalanceDepositVo);
                //响应接口
                hashMap.put("code", "SUCCESS");
                hashMap.put("message", "成功");
                return hashMap;
            }else {
                userDepositOrder.setCreateTime(time);
                userDepositOrder.setOrderNumber(out_trade_no);
                userDepositOrder.setStatus(2);
                userDepositOrderMapper.updateUserDepositOrder(userDepositOrder);
                hashMap.put("code", "FAIL");
                hashMap.put("message", "失败");
                return hashMap;
            }
        }else {
            hashMap.put("code", "FAIL");
            hashMap.put("message", "失败");
            return hashMap;
        }
    }

    @Transactional
    @Override
    public HashMap<String, String> paymentCallBack(HttpServletRequest request) throws GeneralSecurityException {
        System.out.println("订单支付回调成功");
        HashMap params = requestUtils.requestParams(request);
        Map resource = (Map) params.get("resource");
        Object stringBuffer = params.get("stringBuffer");
        String associated_data = (String) resource.get("associated_data");
        String nonce = (String) resource.get("nonce");
        String ciphertext = (String) resource.get("ciphertext");

        //用户租赁信息
        UserLeaseOrder userLeaseOrder = new UserLeaseOrder();

        boolean isTrue = wxCallBackUtils.parseWXCallBack(request, stringBuffer.toString());
        JSONObject parseObject;
        if (isTrue) {
            AesUtil aesUtil = new AesUtil(mchKey.getBytes());
            parseObject = JSONObject.parseObject(
                    aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext)
            );

            Object amount = parseObject.get("amount");
            JSONObject jsonObject1 = JSONObject.parseObject(amount.toString());
            Long price = Long.parseLong(jsonObject1.get("payer_total").toString());
            log.info("实付金额：{}",price);
            BigDecimal divide = new BigDecimal(price).divide(new BigDecimal(100), MathContext.DECIMAL64);
            //支付时间
            Object success_time = parseObject.get("success_time");
            System.out.println(success_time+"微信回调时间");
            LocalDateTime parse = LocalDateTime.parse(success_time.toString(), DateTimeFormatter.ISO_DATE_TIME);
            Date time = Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
            System.out.println(time+"转换后的时间");
            //订单号
            String out_trade_no = (String) parseObject.get("out_trade_no");
            log.info("流水号：{}",out_trade_no);
            if ("SUCCESS".equals(parseObject.get("trade_state"))) {
                Map<String, Object> cacheMap = redisCache.getCacheMap(out_trade_no);
                log.info("测试：{}",cacheMap.toString());
                //实付金额
                userLeaseOrder.setNetAmount(divide);
                //付款时间
                userLeaseOrder.setCreateTime(time);
                //支付流水号
                userLeaseOrder.setOrderNumber(out_trade_no);
                //支付方式
                userLeaseOrder.setPayType("0");
                //修改状态
                userLeaseOrder.setStatus("2");
                //支付成功押金订单为0
                userLeaseOrder.setDepositNumber("0");
                //修改用户租赁信息
                userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder);
                String couponId = (String) cacheMap.get("couponId");
                if(couponId != null){
                    //扣除 优惠券
                    UserDiscount userDiscount = new UserDiscount();
                    userDiscount.setStatus(1L);
                    userDiscount.setUserId(Long.parseLong(String.valueOf(cacheMap.get("couponId"))));
                    userDiscountMapper.updateUserDiscount(userDiscount);
                }

                if (divide.longValue() > 0){
                    log.info("总金额：{}",divide.longValue());
                    //TODO 计算分成
                    UserLeaseOrder userLeaseOrder1 = userLeaseOrderMapper.selectLeaseOrderDetails(userLeaseOrder.getOrderNumber());
                    Long residue = divide.longValue();
                    String agentId = userLeaseOrder1.getAgentId();
                    List<SysUser> list = new ArrayList<>();

                    //TODO 计算代理商分成
                    if (!agentId.equals("0")){
                        Long agentProportion = userLeaseOrder1.getAgentProportion();
                        if (agentProportion != 0){
                            //TODO 计算分成金额
                            BigDecimal netAmount = new BigDecimal(String.valueOf(price)).divide(new BigDecimal(100),MathContext.DECIMAL64);
                            BigDecimal bigDecimal = new BigDecimal(userLeaseOrder1.getAgentProportion()).divide(new BigDecimal(100), MathContext.DECIMAL64);
                            BigDecimal multiply = netAmount.multiply(bigDecimal);
                            //TODO 添加分成金额到账号
                            SysUser user1 = userMapper.selectUserById(Long.parseLong(agentId));
                            if (user1 != null){
                                BigDecimal balance = user1.getBalance();
                                user1.setBalance(balance.add(multiply));
                                list.add(user1);
                                log.info("代理商：{}",user1.getBalance());
                            }

                            residue = new BigDecimal(residue).subtract(multiply).longValue();

                            //TODO 代理商子账户
                            List<OrderProportionDetail> agentAccountProportion = orderProportionMapper.selectAgentAccountProportion(userLeaseOrder.getOrderNumber());
                            if (agentAccountProportion.size() > 0){
                                //TODO 计算子账户分成
                                for (int i = 0; i < agentAccountProportion.size(); i++) {
                                    SysUser user2 = userMapper.selectUserById(agentAccountProportion.get(i).getUserId());
                                    if (user2 != null){
                                        String proportion = agentAccountProportion.get(i).getProportion();
                                        BigDecimal multiply1 = new BigDecimal(proportion).divide(new BigDecimal(100), MathContext.DECIMAL64);
                                        BigDecimal multiply2 = multiply1.multiply(multiply);
                                        user2.setBalance(user2.getBalance().add(multiply2));
                                        log.info("代理商子账户：{}",user2.getBalance());
                                        agentAccountProportion.get(i).setPrice(multiply1);
                                        list.add(user2);
                                    }
                                }
                                //TODO 批量更新收益
                                orderProportionMapper.updateAgentAccountProportion(agentAccountProportion);
                            }

                            //TODO 代理商医院
                            List<OrderProportionDetail> agentHospitalProportion = orderProportionMapper.selectAgentHospitalProportion(userLeaseOrder.getOrderNumber());
                            if (agentHospitalProportion.size() > 0) {
                                for (int i = 0; i < agentHospitalProportion.size(); i++) {
                                    String proportion = agentHospitalProportion.get(i).getProportion();
                                    BigDecimal multiply1 = new BigDecimal(proportion).divide(new BigDecimal(100), MathContext.DECIMAL64).multiply(multiply);
                                    agentHospitalProportion.get(i).setPrice(multiply1);
                                    SysUser user2 = userMapper.selectUserById(agentHospitalProportion.get(i).getUserId());
                                    if (user2 != null){
                                        user2.setBalance(user2.getBalance().add(multiply1));
                                        list.add(user2);
                                        log.info("代理商医院：{}",user2.getBalance());
                                    }

                                }
                                //TODO 批量更新收益
                                orderProportionMapper.updateAgentHospitalProportion(agentHospitalProportion);
                            }
                        }
                    }


                    //TODO 投资人
                    List<OrderProportionDetail> investorProportion = orderProportionMapper.selectInvestorProportion(userLeaseOrder.getOrderNumber());
                    if (investorProportion.size() > 0) {
                        Long investorProportion1 = userLeaseOrder1.getInvestorProportion();
                        BigDecimal netAmount = new BigDecimal(String.valueOf(price.longValue())).divide(new BigDecimal(100),MathContext.DECIMAL64);
                        log.info("总金额：{}",netAmount);
                        BigDecimal multiply = new BigDecimal(investorProportion1).divide(new BigDecimal(100), MathContext.DECIMAL64);
                        log.info("分成比例：{}",multiply);
                        BigDecimal multiply3 = multiply.multiply(netAmount);
                        log.info("分成金额：{}",multiply3);
                        //TODO 投资人子账户
                        List<OrderProportionDetail> investorAccountProportion = orderProportionMapper.selectInvestorAccountProportion(userLeaseOrder.getOrderNumber());
                        if (investorAccountProportion.size() > 0){
                            for (int i = 0; i < investorProportion.size(); i++) {
                                String proportion = investorProportion.get(i).getProportion();
//                                BigDecimal multiply1 = new BigDecimal(proportion).divide(new BigDecimal(100), MathContext.DECIMAL64);
//                                BigDecimal multiply4 = multiply1.multiply(multiply3);
                                residue = new BigDecimal(residue).subtract(multiply3).longValue();
                                for (int k = 0; k < investorAccountProportion.size(); k++) {
                                    if (investorProportion.get(i).getUserId().equals(investorAccountProportion.get(k).getParentId())){
                                        BigDecimal multiply2 = multiply3.multiply(new BigDecimal(investorAccountProportion.get(k).getProportion()).divide(new BigDecimal(100), MathContext.DECIMAL64));
                                        investorAccountProportion.get(k).setPrice(multiply2);
                                        log.info("子账户的分成金额：{}",multiply2);
                                        multiply3 = multiply3.subtract(multiply2);
                                        SysUser user1 = userMapper.selectUserById(investorAccountProportion.get(k).getUserId());
                                        if (user1 != null){
                                            log.info("投资人子账户原来：{}",user1.getBalance());
                                            BigDecimal add = user1.getBalance().add(multiply2);
                                            user1.setBalance(add);
                                            log.info("投资人子账户现在：{}",add);
                                            list.add(user1);
                                        }
                                    }
                                }
                                investorProportion.get(i).setPrice(multiply3);
                                SysUser user1 = userMapper.selectUserById(investorProportion.get(i).getUserId());
                                log.info("原来：{}",user1.getBalance());
                                BigDecimal add = user1.getBalance().add(multiply3);
                                user1.setBalance(add);
                                list.add(user1);
                                log.info("现在投资人：{}",add);
                            }
                            //TODO 批量更新收益
                            orderProportionMapper.updateInvestorProportion(investorProportion);
                            orderProportionMapper.updateInvestorAccountProportion(investorAccountProportion);
                        }else {
                            for (int i = 0; i < investorProportion.size(); i++) {
                                String proportion = investorProportion.get(i).getProportion();
                                BigDecimal multiply1 = new BigDecimal(proportion).divide(new BigDecimal(100), MathContext.DECIMAL64);
                                BigDecimal multiply2 = multiply1.multiply(netAmount);
                                log.info("没有子账号的代理商：{}",multiply2);

                                residue = new BigDecimal(residue).subtract(multiply2).longValue();
                                SysUser user1 = userMapper.selectUserById(investorProportion.get(i).getUserId());
                                if (user1 != null){
                                    user1.setBalance(user1.getBalance().add(multiply1));
                                    list.add(user1);
                                    log.info("投资人：{}",user1.getBalance());
                                }
                            }
                            //TODO 批量更新收益
                            orderProportionMapper.updateInvestorProportion(investorProportion);
                        }
                    }

                    //TODO 计算医院分成
                    String hospitalId = userLeaseOrder1.getHospitalId();
                    Long hospitalProportion = userLeaseOrder1.getHospitalProportion();
                    if (!hospitalId.equals("0")){
                        BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hospitalProportion).divide(new BigDecimal(100), MathContext.DECIMAL64));
                        SysUser sysUser = userMapper.selectHospitalProportion(hospitalId);
                        if (sysUser != null){
                            BigDecimal balance = sysUser.getBalance();
                            sysUser.setBalance(balance.add(multiply));
                            list.add(sysUser);
                            log.info("医院：{}",sysUser.getBalance());
                        }
                    }
                    SysUser sysUser = userMapper.selectAdmin(1L);
                    BigDecimal add = sysUser.getBalance().add(new BigDecimal(residue));
                    if (add.compareTo(BigDecimal.ZERO) == 0 || add.compareTo(BigDecimal.ZERO) > 0){
                        sysUser.setBalance(add);
                        list.add(sysUser);
                        log.info("平台：{}",sysUser.getBalance());
                    }

                    if (list.size() > 0){
                        log.info("批量更新");
                        //TODO 批量更新
                        userMapper.batchUpdate(list);
                    }
                }

                //响应接口
                hashMap.put("code", "SUCCESS");
                hashMap.put("message", "成功");
                return hashMap;
            } else {
                hashMap.put("code", "FAIL");
                hashMap.put("message", "失败");
                return hashMap;
            }
        }else {
            hashMap.put("code", "FAIL");
            hashMap.put("message", "失败");
            return hashMap;
        }
    }

    @Transactional
    @Override
    public AjaxResult balancePrepaymentOrder(String openid, Long couponId, UserLeaseOrder userLeaseOrder) {
        log.info(openid+"----"+couponId+"----"+userLeaseOrder.getPrice()+"-----"+userLeaseOrder.getOrderNumber()+"-----"+userLeaseOrder.getDeviceNumber());
        //根据 openId 查用户余额
        SysUser user = userMapper.selectUserByOpenid(openid);
        BigDecimal price = new BigDecimal(0.00);
        UserDiscount userDiscount = null;
        if (couponId != null){
            //根据优惠券 id 查优惠券金额
            userDiscount = userDiscountMapper.selectUserCouponById(couponId);
            //记录优惠券金额
            userLeaseOrder.setCouponPrice(userDiscount.getPrice().longValue());
//            price = userLeaseOrder.getPrice().subtract(userDiscount.getPrice());
            price = userLeaseOrder.getPrice();
            userLeaseOrder.setPrice(userLeaseOrder.getPrice().add(userDiscount.getPrice()));
            userDiscount.setStatus(1L);
            userDiscountMapper.updateUserDiscount(userDiscount);
//            price = subtract;
        }else {
            price = userLeaseOrder.getPrice();
        }
        if(user.getBalance().compareTo(price) < 0) {
            return AjaxResult.error("余额不足");
        }
        log.info("余额:{}",user.getBalance());

        user.setBalance(user.getBalance().subtract(price));
        userMapper.updateUser(user);
        log.info("实付金额：{}",price);

        //添加余额消费明细
        UserIntegralBalanceDepositVo userIntegralBalanceDepositVo = new UserIntegralBalanceDepositVo();
        userIntegralBalanceDepositVo.setOpenid(openid);
        userIntegralBalanceDepositVo.setSum(price);
        userIntegralBalanceDepositVo.setStatus(1);
        userIntegralBalanceDepositVo.setCreateTime(new Date());
        userBalanceDetailMapper.insertUserBalanceDetail(userIntegralBalanceDepositVo);

        //实付金额
        userLeaseOrder.setNetAmount(price);
        //付款时间
        userLeaseOrder.setCreateTime(DateUtils.getNowDate());
        //订单号
        userLeaseOrder.setOrderNumber(userLeaseOrder.getOrderNumber());
        //修改状态
        userLeaseOrder.setStatus("2");
        //支付方式
        userLeaseOrder.setPayType("2");
        //押金订单
        userLeaseOrder.setDepositNumber("0");
        //修改用户租赁信息
        userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder);

        if (price.longValue() > 0){
            log.info("总金额：{}",price.longValue());
            //TODO 计算分成
            UserLeaseOrder userLeaseOrder1 = userLeaseOrderMapper.selectLeaseOrderDetails(userLeaseOrder.getOrderNumber());
            Long residue = price.longValue();
            String agentId = userLeaseOrder1.getAgentId();
            List<SysUser> list = new ArrayList<>();

            //TODO 计算代理商分成
            if (!agentId.equals("0")){
                Long agentProportion = userLeaseOrder1.getAgentProportion();
                if (agentProportion != 0){
                    //TODO 计算分成金额
                    BigDecimal netAmount = new BigDecimal(String.valueOf(price));
                    BigDecimal bigDecimal = new BigDecimal(userLeaseOrder1.getAgentProportion()).divide(new BigDecimal(100), MathContext.DECIMAL64);
                    log.info("代理商分成：{}",bigDecimal);
                    BigDecimal multiply = netAmount.multiply(bigDecimal);
                    //TODO 添加分成金额到账号
                    SysUser user1 = userMapper.selectUserById(Long.parseLong(agentId));
                    if (user1 != null){
                        BigDecimal balance = user1.getBalance();
                        BigDecimal add = balance.add(multiply);
                        user1.setBalance(add);
                        list.add(user1);
                        log.info("代理商：{}",user1.getBalance());
                    }

                    residue = new BigDecimal(residue).subtract(multiply).longValue();

                    //TODO 代理商子账户
                    List<OrderProportionDetail> agentAccountProportion = orderProportionMapper.selectAgentAccountProportion(userLeaseOrder.getOrderNumber());
                    if (agentAccountProportion.size() > 0){
                        //TODO 计算子账户分成
                        for (int i = 0; i < agentAccountProportion.size(); i++) {
                            SysUser user2 = userMapper.selectUserById(agentAccountProportion.get(i).getUserId());
                            if (user2 != null){
                                String proportion = agentAccountProportion.get(i).getProportion();
                                BigDecimal multiply1 = new BigDecimal(proportion).divide(new BigDecimal(100), MathContext.DECIMAL64);
                                BigDecimal multiply2 = multiply1.multiply(multiply);
                                user2.setBalance(user2.getBalance().add(multiply2));
                                log.info("代理商子账户：{}",user2.getBalance());
                                agentAccountProportion.get(i).setPrice(multiply1);
                                list.add(user2);
                            }
                        }
                        //TODO 批量更新收益
                        orderProportionMapper.updateAgentAccountProportion(agentAccountProportion);
                    }

                    //TODO 代理商医院
                    List<OrderProportionDetail> agentHospitalProportion = orderProportionMapper.selectAgentHospitalProportion(userLeaseOrder.getOrderNumber());
                    if (agentHospitalProportion.size() > 0) {
                        for (int i = 0; i < agentHospitalProportion.size(); i++) {
                            String proportion = agentHospitalProportion.get(i).getProportion();
                            BigDecimal multiply1 = new BigDecimal(proportion).divide(new BigDecimal(100), MathContext.DECIMAL64).multiply(multiply);
                            agentHospitalProportion.get(i).setPrice(multiply1);
                            SysUser user2 = userMapper.selectUserById(agentHospitalProportion.get(i).getUserId());
                            if (user2 != null){
                                user2.setBalance(user2.getBalance().add(multiply1));
                                list.add(user2);
                                log.info("代理商医院：{}",user2.getBalance());
                            }

                        }
                        //TODO 批量更新收益
                        orderProportionMapper.updateAgentHospitalProportion(agentHospitalProportion);
                    }
                }
            }


            //TODO 投资人
            List<OrderProportionDetail> investorProportion = orderProportionMapper.selectInvestorProportion(userLeaseOrder.getOrderNumber());
            if (investorProportion.size() > 0) {
                Long investorProportion1 = userLeaseOrder1.getInvestorProportion();
                BigDecimal netAmount = new BigDecimal(String.valueOf(price.longValue()));
                log.info("总金额：{}",netAmount);
                BigDecimal multiply = new BigDecimal(investorProportion1).divide(new BigDecimal(100), MathContext.DECIMAL64);
                log.info("分成比例：{}",multiply);
                BigDecimal multiply3 = multiply.multiply(netAmount);
                log.info("分成金额：{}",multiply3);
                //TODO 投资人子账户
                List<OrderProportionDetail> investorAccountProportion = orderProportionMapper.selectInvestorAccountProportion(userLeaseOrder.getOrderNumber());
                if (investorAccountProportion.size() > 0){
                    for (int i = 0; i < investorProportion.size(); i++) {
                        String proportion = investorProportion.get(i).getProportion();
                        BigDecimal multiply1 = new BigDecimal(proportion).divide(new BigDecimal(100), MathContext.DECIMAL64);
                        BigDecimal multiply4 = multiply1.multiply(multiply3);
                        log.info("投资人金额：{}",multiply4);
                        residue = new BigDecimal(residue).subtract(multiply4).longValue();
                        for (int k = 0; k < investorAccountProportion.size(); k++) {
                            if (investorProportion.get(i).getUserId().equals(investorAccountProportion.get(k).getParentId())){
                                BigDecimal multiply2 = multiply4.multiply(new BigDecimal(investorAccountProportion.get(k).getProportion()).divide(new BigDecimal(100), MathContext.DECIMAL64));
                                investorAccountProportion.get(k).setPrice(multiply2);
                                multiply4 = multiply4.subtract(multiply2);
                                SysUser user1 = userMapper.selectUserById(investorAccountProportion.get(k).getUserId());
                                if (user1 != null){
                                    user1.setBalance(user1.getBalance().add(multiply2));
                                    list.add(user1);
                                    log.info("投资人子账户：{}",user1.getBalance());
                                }
                            }
                        }
                        investorProportion.get(i).setPrice(multiply4);
                        SysUser user1 = userMapper.selectUserById(investorProportion.get(i).getUserId());
                        log.info("原来：{}",user1.getBalance());
                        BigDecimal add = user1.getBalance().add(multiply4);
                        user1.setBalance(add);
                        list.add(user1);
                        log.info("投资人现在：{}",add);
                    }
                    //TODO 批量更新收益
                    orderProportionMapper.updateInvestorProportion(investorProportion);
                    orderProportionMapper.updateInvestorAccountProportion(investorAccountProportion);
                }else {
                    for (int i = 0; i < investorProportion.size(); i++) {
                        String proportion = investorProportion.get(i).getProportion();
                        BigDecimal multiply1 = new BigDecimal(proportion).divide(new BigDecimal(100), MathContext.DECIMAL64);
                        BigDecimal multiply2 = multiply1.multiply(netAmount);
                        log.info("没有子账号的代理商：{}",multiply2);

                        residue = new BigDecimal(residue).subtract(multiply2).longValue();
                        SysUser user1 = userMapper.selectUserById(investorProportion.get(i).getUserId());
                        if (user1 != null){
                            log.info("原来：{}",user1.getBalance());
                            BigDecimal add = user1.getBalance().add(multiply1);
                            user1.setBalance(add);
                            list.add(user1);
                            log.info("现在投资人：{}",add);
                        }
                    }
                    //TODO 批量更新收益
                    orderProportionMapper.updateInvestorProportion(investorProportion);
                }
            }

            //TODO 计算医院分成
            String hospitalId = userLeaseOrder1.getHospitalId();
            Long hospitalProportion = userLeaseOrder1.getHospitalProportion();
            if (!hospitalId.equals("0")){
                BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                BigDecimal multiply = bigDecimal.multiply(new BigDecimal(hospitalProportion).divide(new BigDecimal(100), MathContext.DECIMAL64));
                SysUser sysUser = userMapper.selectHospitalProportion(hospitalId);
                if (sysUser != null){
                    BigDecimal balance = sysUser.getBalance();
                    sysUser.setBalance(balance.add(multiply));
                    list.add(sysUser);
                    log.info("医院：{}",sysUser.getBalance());
                }
            }
            SysUser sysUser = userMapper.selectAdmin(1L);
            BigDecimal add = sysUser.getBalance().add(new BigDecimal(residue));
            if (add.compareTo(BigDecimal.ZERO) == 0 || add.compareTo(BigDecimal.ZERO) > 0){
                sysUser.setBalance(add);
                list.add(sysUser);
                log.info("平台：{}",sysUser.getBalance());
            }

            if (list.size() > 0){
                log.info("批量更新");
                //TODO 批量更新
                userMapper.batchUpdate(list);
            }
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult bluetoothCallback(HttpServletRequest request) {
        try {
            String reader = getAllRequestParam2(request);
            log.info("数据上报:============={}", reader);
//            map = JSONObject.parseObject(reader, Map.class);
            JSONObject jsonObject = JSON.parseObject(reader);
            String deviceId = (String)jsonObject.get("deviceId");
            Map<String,Object> map = (Map<String, Object>) jsonObject.get("service");
            String serviceType = (String) map.get("serviceType");
            Map<String,Object> map1 = (Map<String, Object>) map.get("data");
            //锁状态上报
            if (serviceType.equals("VehicleDetectorBasic")){
                String status = (String)map1.get("status");
                /**
                 * 当 status==‘0’；temperature 用来指示开锁方式：
                 * NB 开锁：temperature == 2；
                 * 蓝牙开锁：temperature == 3；
                 * 异常（钥匙，自动弹开）开锁：temperature == 4；
                 * 当 status==‘1’；temperature 用来指示还床状态：
                 * 异常还床：temperature == 0；
                 * 正常还床：temperature == 1；
                 */
                int temperature = Integer.parseInt(map1.get("temperature").toString());
                //设备编号
                String deviceNumber = (String) map1.get("NO");
                Device device1 = new Device();
                device1.setDeviceNumber(deviceNumber);
                device1.setTelecomId(deviceId);
                device1.setTime(new Date());
                deviceMapper.updateDeviceStatus(device1);
                /**
                 * 长度为 16 位字符，eg：”timestamp”:
                 * "D002061049050051"
                 * 第 1 位为’D’；第 2~4 位为卡槽号/锁号，002 表示 2 号锁
                 * 头；第 5~16 位表示插销 ID/共享物品的 ID，为 12 位字符，
                 * 其中第 16 位表示共享品种类
                 */
                String timestamp = (String) map1.get("timestamp");
                if (timestamp != null){
                    String substring = timestamp.substring(0, 1);
                    //子锁号
                    String substring1 = timestamp.substring(3, 4);
                    String substring2 = timestamp.substring(4, 16);
                    log.info("子锁柜号：{}",substring1);
                    log.info("子锁唯一标识：{}",substring2);
                    if (status.equals("0")){
                        if (substring.equals("D")) {
                            log.info("借床回调注释");
//                            if (temperature == 2 || temperature == 3){
//                                UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectOrderByDeviceNumberAndChoose(substring2);
//                                if (userLeaseOrder != null){
//                                    //计算套餐
//                                    log.info("订单使用套餐：{}",userLeaseOrder.getDeviceRule());
//                                    String rule = userLeaseOrder.getDeviceRule();
//                                    JSONArray objects = JSON.parseArray(rule);
//                                    Map<String, Object> hashMap = new HashMap<>();
//                                    Map<String, Object> objectMap = new HashMap<>();
//                                    for (int i = 0; i < objects.size(); i++) {
//                                        JSONObject object = JSON.parseObject(objects.get(i).toString());
//                                        int timeStatus = Integer.parseInt(object.get("time").toString());
//                                        if (timeStatus == 0){
//                                            hashMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
//                                        }else {
//                                            objectMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
//                                        }
//                                    }
//                                    System.out.println("开始判断");
//                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
//                                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//                                    String format = simpleDateFormat.format(userLeaseOrder.getLeaseTime());
//
//                                    Date parse = simpleDateFormat.parse(format);
//                                    Date startTime = simpleDateFormat.parse(objectMap.get("startTime").toString());
//                                    BigDecimal price = new BigDecimal(objectMap.get("price").toString());
//                                    System.out.println(price + "固定套餐的价格");
//                                    boolean before = parse.before(startTime);
//                                    System.out.println(parse+"下单时间");
//                                    System.out.println(startTime+"固定套餐时间");
//                                    System.out.println(before+"结果");
//                                    //订单变成有效订单
//                                    userLeaseOrder.setIsValid(1L);
//
//                                    if (before){
//                                        long l = startTime.getTime() - parse.getTime();
//                                        long nd = 1000 * 24 * 60 * 60;
//                                        long nh = 1000 * 60 * 60;
//                                        long nm = 1000 * 60;
//                                        long ns = 1000;
//                                        long sec = l / ns;
//                                        redisServer.setCacheObject(orderPrefix+userLeaseOrder.getOrderNumber(),userLeaseOrder,
//                                                new Long(sec).intValue(), TimeUnit.SECONDS);
//                                    }else {
//                                        redisServer.setCacheObject(orderPrefix+userLeaseOrder.getOrderNumber(),userLeaseOrder);
//                                    }
//                                    //修改订单信息
//                                    userLeaseOrderMapper.updateUserLeaseOrder(userLeaseOrder);
//                                    redisServer.deleteObject(orderValid+userLeaseOrder.getOrderNumber());
//                                    Device device = deviceMapper.selectDeviceByDeviceNumber(deviceNumber);
//                                    String rows = device.getRows();
//                                    //TODO 处理一拖五或单个锁
//                                    int count = 0;
//                                    Gson gson = new Gson();
//                                    List<ItemVO> itemList = gson.fromJson(rows, new TypeToken<List<ItemVO>>(){}.getType());
//                                    if (itemList.size() != 0){
//                                        for (int i = 0; i < itemList.size(); i++) {
//                                            int index = itemList.get(i).getIndex();
//                                            if (Integer.parseInt(substring1) == index){
//                                                itemList.get(i).setStatus(2);
//                                                count=count+1;
//                                            }else if (itemList.get(i).getStatus() != 0){
//                                                count=count+1;
//                                            }
//                                        }
//                                    }
//                                    if (count == itemList.size()){
//                                        deviceMapper.updateDeviceByDeviceNumber(gson.toJson(itemList),deviceNumber,1);
//                                    }else {
//                                        deviceMapper.updateDeviceByDeviceNumber(gson.toJson(itemList),deviceNumber,0);
//                                    }
//                                }
//                                System.out.println("借床成功");
//                            }else if (temperature == 4){
//                                log.info("借床异常");
//                            }
                        }
                    }else if (status.equals("1")){
                        if (temperature == 0){
                            log.info("异常还床");
                        }else if (temperature == 1){
                            log.info("正常还床");
                            UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectOrderByDeviceNumber(substring2);
                            if (userLeaseOrder != null){
                                log.info("订单使用套餐：{}",userLeaseOrder.getDeviceRule());
                                String rule = userLeaseOrder.getDeviceRule();
                                JSONArray objects = JSON.parseArray(rule);
                                Map<String, Object> hashMap = new HashMap<>();
                                Map<String, Object> objectMap = new HashMap<>();
                                for (int i = 0; i < objects.size(); i++) {
                                    JSONObject object = JSON.parseObject(objects.get(i).toString());
                                    int timeStatus = Integer.parseInt(object.get("time").toString());
                                    if (timeStatus == 0){
                                        hashMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                    }else {
                                        objectMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                    }
                                }
                                System.out.println("开始判断");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                                String format = simpleDateFormat.format(userLeaseOrder.getLeaseTime());

                                Date parse = simpleDateFormat.parse(format);
                                Date startTime = simpleDateFormat.parse(objectMap.get("startTime").toString());
                                BigDecimal price = new BigDecimal(objectMap.get("price").toString());
                                System.out.println(price + "固定套餐的价格");
                                boolean before = parse.before(startTime);
                                System.out.println(parse+"下单时间");
                                System.out.println(startTime+"固定套餐时间");
                                System.out.println(before+"结果");
                                if (substring.equals("D")){
                                    userLeaseOrder.setRestoreTime(new Date());
                                    userLeaseOrder.setStatus("1");
                                    int time = 0;
                                    long l = new Date().getTime() - userLeaseOrder.getLeaseTime().getTime();
                                    long nd = 1000 * 24 * 60 * 60;
                                    long nh = 1000 * 60 * 60;
                                    long nm = 1000 * 60;
                                    long ns = 1000;

                                    // 计算差多少天
                                    long day = l / nd;
                                    // 计算差多少小时
                                    long hour = l % nd / nh;
                                    // 计算差多少分钟
                                    long min = l % nd % nh / nm;
                                    if (day != 0){
                                        time += day * 24;
                                    }
                                    if (hour != 0){
                                        time += hour;
                                    }
                                    if (min > 0){
                                        time += time+1;
                                    }
                                    userLeaseOrder.setPlayTime(String.valueOf(l));
                                    log.info("还床使用时长：{}",userLeaseOrder.getPlayTime());
                                    long valid = l / ns;
                                    log.info("还床使用多少秒钟：{}",valid);
                                    if (valid > 600){
                                        //收费
                                        long keyExpire = redisServer.getKeyExpire(orderPrefix + userLeaseOrder.getOrderNumber());
                                        System.out.println(keyExpire+"过期时间");
                                        BigDecimal bigDecimal = new BigDecimal(hashMap.get("price").toString());
                                        if (keyExpire >= 0){
                                            if (before){
                                                userLeaseOrder.setTimePrice(bigDecimal.multiply(new BigDecimal(time)));
                                                log.info("定时费用：{}",userLeaseOrder.getTimePrice());
                                            }
                                        }else {
                                            userLeaseOrder.setFixedPrice(price);
                                            log.info("固定费用：{}",userLeaseOrder.getTimePrice());
                                        }
                                    }else {
                                        log.info("订单未超过10分钟免费");
                                        //免费
                                        redisServer.deleteObject(orderPrefix+userLeaseOrder.getOrderNumber());
                                        userLeaseOrder.setPrice(new BigDecimal(0));
                                        userLeaseOrder.setTimePrice(new BigDecimal(0));
                                        userLeaseOrder.setFixedPrice(new BigDecimal(0));
                                    }
                                    Device device = deviceMapper.selectDeviceByDeviceNumber(deviceNumber);
                                    userLeaseOrder.setRestoreAddress(device.getDeviceAddress());
                                    log.info("userLeaseOrder.getTimePrice():{}",userLeaseOrder.getTimePrice());
                                    log.info("userLeaseOrder.getFixedPrice():{}",userLeaseOrder.getFixedPrice());
                                    log.info("日志：{}",userLeaseOrder.getTimePrice().add(userLeaseOrder.getFixedPrice(), MathContext.DECIMAL32));
                                    userLeaseOrder.setPrice(userLeaseOrder.getTimePrice().add(userLeaseOrder.getFixedPrice(), MathContext.DECIMAL32));
                                    log.info("修改的id：{}",userLeaseOrder.getId());
                                    userLeaseOrderMapper.updateUserLeaseOrder(userLeaseOrder);
                                    redisServer.deleteObject(orderPrefix+userLeaseOrder.getOrderNumber());
                                    String rows = device.getRows();
                                    Gson gson = new Gson();
                                    List<ItemVO> itemList = gson.fromJson(rows, new TypeToken<List<ItemVO>>(){}.getType());
                                    if (itemList.size() != 0){
                                        for (int i = 0; i < itemList.size(); i++) {
                                            int index = itemList.get(i).getIndex();
                                            if (Integer.parseInt(substring1) == index){
                                                itemList.get(i).setStatus(0);
                                            }
                                        }
                                    }
                                    deviceMapper.updateDeviceByDeviceNumber(gson.toJson(itemList),deviceNumber,0);
                                }
                            }else {
                                Device device = deviceMapper.selectDeviceByDeviceNumber(deviceNumber);
                                String rows = device.getRows();
                                Gson gson = new Gson();
                                List<ItemVO> itemList = gson.fromJson(rows, new TypeToken<List<ItemVO>>(){}.getType());
                                if (itemList.size() != 0){
                                    for (int i = 0; i < itemList.size(); i++) {
                                        int index = itemList.get(i).getIndex();
                                        if (Integer.parseInt(substring1) == index){
                                            itemList.get(i).setStatus(0);
                                        }
                                    }
                                }
                                deviceMapper.updateDeviceByDeviceNumber(gson.toJson(itemList),deviceNumber,0);
                            }
                        }
                    }
                }
            }else if (serviceType.equals("Battery")){
                //电量上报
                Integer integer = Integer.parseInt(map1.get("batteryLevel").toString());
                log.info("电量:==============={}",integer);
                //TODO 电量处理
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("NB数据上报执行事务回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("NB数据上报执行回滚成功");
        }
        return AjaxResult.error();
    }

    @Override
    public AjaxResult PH70Callback(HttpServletRequest request) {
        UsrDemo usrDemo = new UsrDemo();
        try {
//            String productId = "16668951";
//            String MasterKey = "73dcc3a337954de78d7a912394dbd52f";
            String reader = getAllRequestParam2(request);
            log.info("PH70数据上报:============={}", reader);
            JSONObject object = JSON.parseObject(reader);
            String productId = (String) object.get("productId");
            Map<String,Object> map = (Map<String,Object>)object.get("payload");
            String apPdata = String.valueOf(map.get("APPdata"));
//            log.info("PH70数据上报16进制64加密：{}",apPdata);
            byte[] bytes = Base64.getDecoder().decode(apPdata);
            String hexBinary = DatatypeConverter.printHexBinary(bytes);
            log.info("PH70数据上报16进制64解密：{}",hexBinary);
            log.info("post开始");
            //头
            String substring = hexBinary.substring(0, 2);
            //命令ID
            String substring1 = hexBinary.substring(2, 6);
            //属性
            String substring2 = hexBinary.substring(6, 10);
            //IMEI
            String substring3 = hexBinary.substring(10, 26);
            //流水号
            String substring4 = hexBinary.substring(26, 30);
            Device deviceNumber = deviceMapper.selectDeviceByDeviceNumber(substring3.substring(1, 16));
            if (deviceNumber != null){
                if (substring1.equals("0102")){
                    log.info("锁鉴权");
                    //消息体
                    String substring5 = hexBinary.substring(30, 42);
                    //检验
                    String substring6 = hexBinary.substring(42, 44);
                    //尾
                    String substring7 = hexBinary.substring(44, 46);
                    //            String result = "7E80010005"+substring3+"0001"+substring4+substring1+"00"+substring6+substring7;
                    String result = substring+"80010005"+substring3+substring4+substring4+substring1+"00"+substring6+substring7;
                    String substring8 = result.substring(2, 40);
                    byte[] bytes1 = DatatypeConverter.parseHexBinary(substring8);
                    // 执行异或运算
                    byte xor = 0;
                    for (byte b : bytes1) {
                        xor ^= b;
                    }
                    // 将结果转换为16进制字符串
                    System.out.println(xor);
                    String string = String.format("%02X", xor);
                    System.out.println(string+"结果");
                    StringBuilder builder = new StringBuilder(result);
                    builder.replace(40,42,string);
                    String string1 = builder.toString();
                    usrDemo.httpPostExample(string1,deviceNumber.getProductId(),deviceNumber.getMasterKey(),deviceNumber.getTelecomId(),secret,application,domain);
                    log.info("post结束");

                    Device device = new Device();
                    device.setDeviceNumber(deviceNumber.getDeviceNumber());
                    if (productId != null){
                        device.setProductId(productId);
                    }
                    device.setTime(new Date());
                    deviceMapper.updateDeviceStatus(device);
                }else if (substring1.equals("0200")){
                    log.info("位置信息汇报");
                    //锁状态
                    String substring16 = hexBinary.substring(104, 112);
                    //校验
                    String substring21 = hexBinary.substring(162, 164);
                    //尾
                    String substring22 = hexBinary.substring(164, 166);
                    //校验
                    String substring23 = hexBinary.substring(2, 162);
                    byte[] bytes1 = DatatypeConverter.parseHexBinary(substring23);
                    // 执行异或运算
                    byte xor = 0;
                    for (byte b : bytes1) {
                        xor ^= b;
                    }
                    String string = String.format("%02X", xor);
                    if (string.equals(substring21)){
                        log.info("位置信息汇报校验通过");
                        String substring25 = substring16.substring(4, 8);
                        int decimalData = Integer.parseInt(substring25, 16);
                        String binaryData = Integer.toBinaryString(decimalData);
                        // 在前面补全二进制字符串
                        while (binaryData.length() < 8) {
                            binaryData = "0" + binaryData;
                        }
                        // 补全到 12 位
                        while (binaryData.length() < 12) {
                            binaryData = "0" + binaryData;
                        }
                        // 将二进制字符串划分为两个 8 位的字节
                        String byte1 = binaryData.substring(0, 8);
                        String byte2 = binaryData.substring(8, 12);
                        String s = byte1 + byte2;
                        //磁铁的状态  1： 开启，0 : 闭合
                        String substring26 = s.substring(11, 12);
                        /**
                         * 0000：表示定时上传，
                         * 0001：表示开锁上传，
                         * 0010：表示关锁上传，
                         * 0011：表示低电上传，
                         * 0100：表示剪断报警，
                         * 0101：表示撞击上传，
                         * 0111：按键唤醒，
                         * 1000：系统重启，
                         * 0110：开锁失败（开锁超时
                         */
                        String substring27 = s.substring(7, 11);
                        String substring31 = s.substring(3, 4);
                        log.info("锁状态：{}",s);
                        log.info("锁状态：{}",substring27);
                        log.info("磁铁的状态：{}",substring26);
                        log.info("锁梁状态:{}",substring31);
                        String substring35 = substring3.substring(1, 16);
                        log.info("IMEI号：{}",substring35);
                        if (substring27.equals("0001")){
//                            UserLeaseOrder userLeaseOrder = new UserLeaseOrder();
//                            userLeaseOrder.setDeviceNumber(substring35);
//                            userLeaseOrder.setIsValid(1L);
//                            log.info("订单信息：{}",userLeaseOrder.getDeviceNumber());
//                            userLeaseOrderMapper.updateUserLeaseOrderByDeviceNumber(userLeaseOrder);

                            UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectLeaseOrderByDeviceNumber(substring35);
                            if (userLeaseOrder != null){
                                //计算套餐
                                log.info("订单使用套餐：{}",userLeaseOrder.getDeviceRule());
                                String rule = userLeaseOrder.getDeviceRule();
                                JSONArray objects = JSON.parseArray(rule);
                                Map<String, Object> hashMap = new HashMap<>();
                                Map<String, Object> objectMap = new HashMap<>();
                                for (int i = 0; i < objects.size(); i++) {
                                    JSONObject object1 = JSON.parseObject(objects.get(i).toString());
                                    int timeStatus = Integer.parseInt(object1.get("time").toString());
                                    if (timeStatus == 0){
                                        hashMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                    }else {
                                        objectMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                    }
                                }
                                System.out.println("开始判断");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                                String format = simpleDateFormat.format(userLeaseOrder.getLeaseTime());

                                Date parse = simpleDateFormat.parse(format);
                                Date startTime = simpleDateFormat.parse(objectMap.get("startTime").toString());
                                BigDecimal price = new BigDecimal(objectMap.get("price").toString());
                                System.out.println(price + "固定套餐的价格");
                                boolean before = parse.before(startTime);
                                System.out.println(parse+"下单时间");
                                System.out.println(startTime+"固定套餐时间");
                                System.out.println(before+"结果");
                                //订单变成有效订单
                                userLeaseOrder.setIsValid(1L);

                                if (before){
                                    long l = startTime.getTime() - parse.getTime();
                                    long nd = 1000 * 24 * 60 * 60;
                                    long nh = 1000 * 60 * 60;
                                    long nm = 1000 * 60;
                                    long ns = 1000;
                                    long sec = l / ns;
                                    System.out.println(sec+"多少秒钟");
                                    redisServer.setCacheObject(orderPrefix+userLeaseOrder.getOrderNumber(),userLeaseOrder,
                                            new Long(sec).intValue(), TimeUnit.SECONDS);
                                    System.out.println("存储到redis1");
                                }else {
                                    redisServer.setCacheObject(orderPrefix+userLeaseOrder.getOrderNumber(),userLeaseOrder);
                                    System.out.println("存储到redis2");
                                }
                                //修改订单信息
                                userLeaseOrderMapper.updateUserLeaseOrderByDeviceNumber(userLeaseOrder);

                                redisServer.deleteObject(orderValid+userLeaseOrder.getOrderNumber());

                                Device device1 = new Device();
                                device1.setDeviceNumber(substring35);
                                device1.setStatus(1L);
                                if (productId != null){
                                    device1.setProductId(productId);
                                }
                                device1.setTime(new Date());
                                deviceMapper.updateDeviceStatus(device1);
                            }
                            log.info("开锁");
                        }else if (substring27.equals("0111")){
                            Device device = new Device();
                            device.setDeviceNumber(deviceNumber.getDeviceNumber());
                            if (productId != null){
                                device.setProductId(productId);
                            }
                            device.setTime(new Date());
                            deviceMapper.updateDeviceStatus(device);
                            //TODO 设备在线
                            log.info("唤醒");
                        }else if (substring27.equals("0010")){
                            UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectLeaseOrderByDeviceNumber(substring35);
                            if (userLeaseOrder != null){
                                //TODO 计算套餐
                                log.info("订单使用套餐：{}",userLeaseOrder.getDeviceRule());
                                String rule = userLeaseOrder.getDeviceRule();
                                JSONArray objects = JSON.parseArray(rule);
                                Map<String, Object> hashMap = new HashMap<>();
                                Map<String, Object> objectMap = new HashMap<>();
                                for (int i = 0; i < objects.size(); i++) {
                                    JSONObject object1 = JSON.parseObject(objects.get(i).toString());
                                    int timeStatus = Integer.parseInt(object1.get("time").toString());
                                    if (timeStatus == 0){
                                        hashMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                    }else {
                                        objectMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                    }
                                }
                                System.out.println("开始判断");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                                String format = simpleDateFormat.format(userLeaseOrder.getLeaseTime());

                                Date parse = simpleDateFormat.parse(format);
                                Date startTime = simpleDateFormat.parse(objectMap.get("startTime").toString());
                                BigDecimal price = new BigDecimal(objectMap.get("price").toString());
                                System.out.println(price + "固定套餐的价格");
                                boolean before = parse.before(startTime);
                                System.out.println(parse+"下单时间");
                                System.out.println(startTime+"固定套餐时间");
                                System.out.println(before+"结果");
                                    userLeaseOrder.setRestoreTime(new Date());
                                    userLeaseOrder.setStatus("1");
                                    //TODO 计算订单金额
                                    int time = 0;
                                    long l = new Date().getTime() - userLeaseOrder.getLeaseTime().getTime();
                                    long nd = 1000 * 24 * 60 * 60;
                                    long nh = 1000 * 60 * 60;
                                    long nm = 1000 * 60;
                                    long ns = 1000;

                                    // 计算差多少天
                                    long day = l / nd;
                                    // 计算差多少小时
                                    long hour = l % nd / nh;
                                    // 计算差多少分钟
                                    long min = l % nd % nh / nm;
                                    if (day != 0){
                                        time += day * 24;
                                    }
                                    if (hour != 0){
                                        time += hour;
                                    }
                                    if (min > 0){
                                        time += time+1;
                                    }
                                    userLeaseOrder.setPlayTime(String.valueOf(l));
                                    long valid = l / ns;
                                    if (valid > 600){
                                        //收费
                                        long keyExpire = redisServer.getKeyExpire(orderPrefix + userLeaseOrder.getOrderNumber());
                                        System.out.println(keyExpire+"过期时间");
                                        BigDecimal bigDecimal = new BigDecimal(hashMap.get("price").toString());
                                        if (keyExpire != -1){
                                            if (before){
                                                userLeaseOrder.setTimePrice(bigDecimal.multiply(new BigDecimal(time)));
                                            }
                                        }else {
                                            userLeaseOrder.setFixedPrice(price);
                                        }
                                    }else {
                                        //免费
                                        redisServer.deleteObject(orderPrefix+userLeaseOrder.getOrderNumber());
                                        userLeaseOrder.setPrice(new BigDecimal(0));
                                        userLeaseOrder.setTimePrice(new BigDecimal(0));
                                        userLeaseOrder.setFixedPrice(new BigDecimal(0));
                                    }
                                    Device device = deviceMapper.selectDeviceByDeviceNumber(substring35);

                                    userLeaseOrder.setRestoreAddress(device.getDeviceAddress());
                                    log.info("userLeaseOrder.getTimePrice():{}",userLeaseOrder.getTimePrice());
                                    log.info("userLeaseOrder.getFixedPrice():{}",userLeaseOrder.getFixedPrice());
                                    log.info("日志：{}",userLeaseOrder.getTimePrice().add(userLeaseOrder.getFixedPrice(), MathContext.DECIMAL32));
                                    userLeaseOrder.setPrice(userLeaseOrder.getTimePrice().add(userLeaseOrder.getFixedPrice(), MathContext.DECIMAL32));
                                    log.info("修改的id：{}",userLeaseOrder.getId());
                                    userLeaseOrderMapper.updateUserLeaseOrderByDeviceNumber(userLeaseOrder);
                                    redisServer.deleteObject(orderPrefix+userLeaseOrder.getOrderNumber());

                                    System.out.println("正常还床");

                                    Device device1 = new Device();
                                    device1.setDeviceNumber(substring35);
                                    device1.setStatus(1L);
                                    if (productId != null){
                                        device1.setProductId(productId);
                                    }
                                    device1.setTime(new Date());
                                    deviceMapper.updateDeviceStatus(device1);
                            }else {
                                Device device1 = new Device();
                                device1.setDeviceNumber(substring35);
                                device1.setStatus(0L);
                                device1.setTime(new Date());
                                deviceMapper.updateDeviceStatus(device1);
                            }
                            log.info("关锁");
                        }
                        String result = substring+"80010005"+substring3+substring4+substring4+substring1+"00"+substring21+substring22;
                        String substring24 = result.substring(2, 40);
                        // 执行异或运算
                        byte xo = 0;
                        for (byte b : bytes1) {
                            xo ^= b;
                        }
                        String format = String.format("%02X", xo);
                        StringBuilder builder = new StringBuilder(result);
                        builder.replace(40,42,format);
                        String string1 = builder.toString();
                        usrDemo.httpPostExample(string1,deviceNumber.getProductId(),deviceNumber.getMasterKey(),deviceNumber.getTelecomId(),secret,application,domain);
                        log.info("post结束");
                    }else {
                        log.info("位置信息汇报校验失败");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("PH70数据上报执行事务回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("Ph70数据上报执行回滚成功");
        }
        return null;
    }

    @Override
    public AjaxResult XG70NBTCallback(HttpServletRequest request) {
        UsrDemo usrDemo = new UsrDemo();
        try {
//            String MasterKey = "95304dbadea04d1f899b992ef34d49d6";
//            String productId = "16666960";
            String reader = getAllRequestParam2(request);
            log.info("XG70NBT数据上报:============={}", reader);
            JSONObject object = JSON.parseObject(reader);
            Map<String,Object> map = (Map<String,Object>)object.get("payload");
            String apPdata = String.valueOf(map.get("APPdata"));
            byte[] bytes = Base64.getDecoder().decode(apPdata);
            String hexBinary = DatatypeConverter.printHexBinary(bytes);
            log.info("XG70NBT数据上报16进制64解密：{}",hexBinary);
            //头
            String substring = hexBinary.substring(0, 2);
            //命令ID
            String substring1 = hexBinary.substring(2, 6);
            //属性
            String substring2 = hexBinary.substring(6, 10);
            //IMEI
            String substring3 = hexBinary.substring(10, 26);
            //流水号
            String substring4 = hexBinary.substring(26, 30);
            Device deviceNumber = deviceMapper.selectDeviceByDeviceNumber(substring3.substring(1, 16));
            if (substring1.equals("0102")){
                log.info("锁鉴权");
                //消息体
                String substring5 = hexBinary.substring(30, 42);
                //检验
                String substring6 = hexBinary.substring(42, 44);
                //尾
                String substring7 = hexBinary.substring(44, 46);
                //            String result = "7E80010005"+substring3+"0001"+substring4+substring1+"00"+substring6+substring7;
                String result = substring+"80010005"+substring3+substring4+substring4+substring1+"00"+substring6+substring7;
                String substring8 = result.substring(2, 40);
                log.info("result:{}",result);
                byte[] bytes1 = DatatypeConverter.parseHexBinary(substring8);
                // 执行异或运算
                byte xor = 0;
                for (byte b : bytes1) {
                    xor ^= b;
                }
                // 将结果转换为16进制字符串
                System.out.println(xor);
                String string = String.format("%02X", xor);
                System.out.println(string+"结果");
                StringBuilder builder = new StringBuilder(result);
                builder.replace(40,42,string);
                String string1 = builder.toString();
                log.info("string1:{}",string1);
                log.info("productId:{}",deviceNumber.getProductId());
                log.info("MasterKey:{}",deviceNumber.getMasterKey());
                log.info("device.getTelecomId():{}",deviceNumber.getTelecomId());
                usrDemo.httpPostExample(string1,deviceNumber.getProductId(),deviceNumber.getMasterKey(),deviceNumber.getTelecomId(),secret,application,domain);
                log.info("post结束");
            }else if (substring1.equals("0200")){
                log.info("位置信息汇报");
                //锁状态
                String substring16 = hexBinary.substring(104, 112);
                //校验
                String substring21 = hexBinary.substring(146, 148);
                //尾
                String substring22 = hexBinary.substring(148, 150);
                //校验
                String substring23 = hexBinary.substring(2, 146);
                byte[] bytes1 = DatatypeConverter.parseHexBinary(substring23);
                // 执行异或运算
                byte xor = 0;
                for (byte b : bytes1) {
                    xor ^= b;
                }
                String string = String.format("%02X", xor);
                if (string.equals(substring21)){
                    log.info("位置信息汇报校验通过");
                    String substring25 = substring16.substring(4, 8);
                    int decimalData = Integer.parseInt(substring25, 16);
                    String binaryData = Integer.toBinaryString(decimalData);
                    // 在前面补全二进制字符串
                    while (binaryData.length() < 8) {
                        binaryData = "0" + binaryData;
                    }
                    // 补全到 12 位
                    while (binaryData.length() < 12) {
                        binaryData = "0" + binaryData;
                    }
                    // 将二进制字符串划分为两个 8 位的字节
                    String byte1 = binaryData.substring(0, 8);
                    String byte2 = binaryData.substring(8, 12);
                    String s = byte1 + byte2;
                    //磁铁的状态  1： 开启，0 : 闭合
                    String substring26 = s.substring(11, 12);

                    /**
                     * 0000：表示定时上传，
                     * 0001：表示开锁上传，
                     * 0010：表示关锁上传，
                     * 0011：表示低电上传，
                     * 0100：表示剪断报警，
                     * 0101：表示撞击上传，
                     * 0111：按键唤醒，
                     * 1000：系统重启，
                     * 0110：开锁失败（开锁超时
                     */
                    String substring27 = s.substring(7, 11);
                    //锁梁状态  1: 关 0：开
                    String substring31 = s.substring(3, 4);
                    log.info("锁状态：{}",s);
                    log.info("锁状态：{}",substring27);
                    log.info("磁铁的状态：{}",substring26);
                    log.info("锁梁状态:{}",substring31);
                    String substring35 = substring3.substring(1, 16);
                    log.info("IMEI号：{}",substring35);
                    if (substring27.equals("0001")){
                        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectLeaseOrderByDeviceNumber(substring35);
                        if (userLeaseOrder != null){
                            //计算套餐
                            log.info("订单使用套餐：{}",userLeaseOrder.getDeviceRule());
                            String rule = userLeaseOrder.getDeviceRule();
                            JSONArray objects = JSON.parseArray(rule);
                            Map<String, Object> hashMap = new HashMap<>();
                            Map<String, Object> objectMap = new HashMap<>();
                            for (int i = 0; i < objects.size(); i++) {
                                JSONObject object1 = JSON.parseObject(objects.get(i).toString());
                                int timeStatus = Integer.parseInt(object1.get("time").toString());
                                if (timeStatus == 0){
                                    hashMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                }else {
                                    objectMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                }
                            }
                            System.out.println("开始判断");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                            String format = simpleDateFormat.format(userLeaseOrder.getLeaseTime());

                            Date parse = simpleDateFormat.parse(format);
                            Date startTime = simpleDateFormat.parse(objectMap.get("startTime").toString());
                            BigDecimal price = new BigDecimal(objectMap.get("price").toString());
                            System.out.println(price + "固定套餐的价格");
                            boolean before = parse.before(startTime);
                            System.out.println(parse+"下单时间");
                            System.out.println(startTime+"固定套餐时间");
                            System.out.println(before+"结果");
                            //订单变成有效订单
                            userLeaseOrder.setIsValid(1L);

                            if (before){
                                long l = startTime.getTime() - parse.getTime();
                                long nd = 1000 * 24 * 60 * 60;
                                long nh = 1000 * 60 * 60;
                                long nm = 1000 * 60;
                                long ns = 1000;
                                long sec = l / ns;
                                System.out.println(sec+"多少秒钟");
                                redisServer.setCacheObject(orderPrefix+userLeaseOrder.getOrderNumber(),userLeaseOrder,
                                        new Long(sec).intValue(), TimeUnit.SECONDS);
                                System.out.println("存储到redis1");
                            }else {
                                redisServer.setCacheObject(orderPrefix+userLeaseOrder.getOrderNumber(),userLeaseOrder);
                                System.out.println("存储到redis2");
                            }
                            //修改订单信息
                            userLeaseOrderMapper.updateUserLeaseOrder(userLeaseOrder);

                            redisServer.deleteObject(orderValid+userLeaseOrder.getOrderNumber());

                            Device device1 = new Device();
                            device1.setDeviceNumber(substring35);
                            device1.setStatus(1L);
                            device1.setTime(new Date());
                            deviceMapper.updateDeviceStatus(device1);
                        }
                        log.info("开锁");
                    }else if (substring27.equals("0111")){
                        //TODO 设备在线
                        log.info("唤醒");
                    }else if (substring27.equals("0010")){
                        //                        UserLeaseOrder userLeaseOrder = new UserLeaseOrder();
//                        userLeaseOrder.setStatus("1");
//                        userLeaseOrder.setDeviceNumber(substring35);
//                        userLeaseOrderMapper.updateUserLeaseOrderByDeviceNumber(userLeaseOrder);
//                        Device device = new Device();
//                        device.setDeviceNumber(substring35);
//                        device.setStatus(0L);
//                        deviceMapper.updateDeviceStatus(device);

                        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectLeaseOrderByDeviceNumber(substring35);
                        if (userLeaseOrder != null){
                            //TODO 计算套餐
                            log.info("订单使用套餐：{}",userLeaseOrder.getDeviceRule());
                            String rule = userLeaseOrder.getDeviceRule();
                            JSONArray objects = JSON.parseArray(rule);
                            Map<String, Object> hashMap = new HashMap<>();
                            Map<String, Object> objectMap = new HashMap<>();
                            for (int i = 0; i < objects.size(); i++) {
                                JSONObject object1 = JSON.parseObject(objects.get(i).toString());
                                int timeStatus = Integer.parseInt(object1.get("time").toString());
                                if (timeStatus == 0){
                                    hashMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                }else {
                                    objectMap = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                                }
                            }
                            System.out.println("开始判断");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                            String format = simpleDateFormat.format(userLeaseOrder.getLeaseTime());

                            Date parse = simpleDateFormat.parse(format);
                            Date startTime = simpleDateFormat.parse(objectMap.get("startTime").toString());
                            BigDecimal price = new BigDecimal(objectMap.get("price").toString());
                            System.out.println(price + "固定套餐的价格");
                            boolean before = parse.before(startTime);
                            System.out.println(parse+"下单时间");
                            System.out.println(startTime+"固定套餐时间");
                            System.out.println(before+"结果");
                            userLeaseOrder.setRestoreTime(new Date());
                            userLeaseOrder.setStatus("1");
                            //TODO 计算订单金额
                            int time = 0;
                            long l = new Date().getTime() - userLeaseOrder.getLeaseTime().getTime();
                            long nd = 1000 * 24 * 60 * 60;
                            long nh = 1000 * 60 * 60;
                            long nm = 1000 * 60;
                            long ns = 1000;

                            // 计算差多少天
                            long day = l / nd;
                            // 计算差多少小时
                            long hour = l % nd / nh;
                            // 计算差多少分钟
                            long min = l % nd % nh / nm;
                            if (day != 0){
                                time += day * 24;
                            }
                            if (hour != 0){
                                time += hour;
                            }
                            if (min > 0){
                                time += time+1;
                            }
                            userLeaseOrder.setPlayTime(String.valueOf(l));
                            long valid = l / ns;
                            if (valid > 600){
                                //收费
                                long keyExpire = redisServer.getKeyExpire(orderPrefix + userLeaseOrder.getOrderNumber());
                                System.out.println(keyExpire+"过期时间");
                                BigDecimal bigDecimal = new BigDecimal(hashMap.get("price").toString());
                                if (keyExpire != -1){
                                    if (before){
                                        userLeaseOrder.setTimePrice(bigDecimal.multiply(new BigDecimal(time)));
                                    }
                                }else {
                                    userLeaseOrder.setFixedPrice(price);
                                }
                            }else {
                                //免费
                                redisServer.deleteObject(orderPrefix+userLeaseOrder.getOrderNumber());
                                userLeaseOrder.setPrice(new BigDecimal(0));
                                userLeaseOrder.setTimePrice(new BigDecimal(0));
                                userLeaseOrder.setFixedPrice(new BigDecimal(0));
                            }
                            Device device = deviceMapper.selectDeviceByDeviceNumber(substring35);

                            userLeaseOrder.setRestoreAddress(device.getDeviceAddress());
                            log.info("userLeaseOrder.getTimePrice():{}",userLeaseOrder.getTimePrice());
                            log.info("userLeaseOrder.getFixedPrice():{}",userLeaseOrder.getFixedPrice());
                            log.info("日志：{}",userLeaseOrder.getTimePrice().add(userLeaseOrder.getFixedPrice(), MathContext.DECIMAL32));
                            userLeaseOrder.setPrice(userLeaseOrder.getTimePrice().add(userLeaseOrder.getFixedPrice(), MathContext.DECIMAL32));
                            log.info("修改的id：{}",userLeaseOrder.getId());
                            userLeaseOrderMapper.updateUserLeaseOrderByDeviceNumber(userLeaseOrder);
                            redisServer.deleteObject(orderPrefix+userLeaseOrder.getOrderNumber());

                            System.out.println("正常还床");
                            Device device1 = new Device();
                            device1.setDeviceNumber(substring35);
                            device1.setStatus(0L);
                            device1.setTime(new Date());
                            deviceMapper.updateDeviceStatus(device1);
                        }else {
                            //空闲
                            Device device1 = new Device();
                            device1.setDeviceNumber(substring35);
                            device1.setStatus(0L);
                            device1.setTime(new Date());
                            deviceMapper.updateDeviceStatus(device1);
                        }
                        log.info("关锁");
                    }else if (substring27.equals("0000")){

                    }
                    String result = substring+"80010005"+substring3+substring4+substring4+substring1+"00"+substring21+substring22;
                    String substring24 = result.substring(2, 40);
                    // 执行异或运算
                    byte xo = 0;
                    for (byte b : bytes1) {
                        xo ^= b;
                    }
                    String format = String.format("%02X", xo);
                    StringBuilder builder = new StringBuilder(result);
                    builder.replace(40,42,format);
                    String string1 = builder.toString();
                    usrDemo.httpPostExample(string1,deviceNumber.getProductId(),deviceNumber.getMasterKey(),deviceNumber.getTelecomId(),secret,application,domain);
                    log.info("post结束");
                }else {
                    log.info("位置信息汇报校验失败");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("XG70NBT数据上报执行事务回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("XG70NBT数据上报执行回滚成功");
        }
        return null;
    }

    @Override
    public AjaxResult PH70InstructCallback(HttpServletRequest request) {
        String reader = null;
        try {
            reader = getAllRequestParam2(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("PH70设备指令响应通知:============={}", reader);
        return null;
    }

    @Override
    public AjaxResult PH70IncidentCallback(HttpServletRequest request) {
        String reader = null;
        try {
            reader = getAllRequestParam2(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("PH70设备事件上报通知:============={}", reader);
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

}
