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
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
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
    private RedisServer redisServer;

    @Value("${coupon.order}")
    private String orderPrefix;

    @Autowired
    private DeviceMapper deviceMapper;

    private HashMap<String, String> hashMap = new HashMap<>();


    @Transactional
    @Override
    public HashMap<String, String> payCallBack(HttpServletRequest request) throws GeneralSecurityException {
        HashMap params = requestUtils.requestParams(request);

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
                //用户充值订单
                userPayOrder.setOpenid(openid.toString());
                userPayOrder.setStatus(1);
                userPayOrder.setOutTradeNo(out_trade_no);
                userPayOrderMapper.updateUserPayOrder(userPayOrder);

                Object amount = parseObject.get("amount");
                JSONObject jsonObject1 = JSONObject.parseObject(amount.toString());
                Object price = jsonObject1.get("payer_total");
                //用户余额明细
                userBalanceDetail.setOpenid(openid.toString());
                BigDecimal divide1 = new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100), 2);
//                userBalanceDetail.setSum(new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100),2));
                userBalanceDetail.setSum(new BigDecimal(price.toString()).divide(new BigDecimal(100)));
                userBalanceDetail.setStatus(0);
                userBalanceDetail.setCreateTime(DateUtils.getNowDate());
                userBalanceDetailMapper.insertUserBalanceDetail(userBalanceDetail);

                List<PayType> payTypes = payTypeMapper.selectPayTypeAll();

                for (int i = 0; i < payTypes.size(); i++) {
                    //如果充值金额 等于 系统设置的对应的金额 则赠送 积分
                    if( price == payTypes.get(i).getPaySum()){
                        SysUser user = new SysUser();
                        user.setIntegral(payTypes.get(i).getIntegral());
//                        user.setBalance(Long.parseLong(String.valueOf(new BigDecimal(price.toString()).divide(BigDecimal.valueOf(100),2))));
                        user.setBalance(Long.parseLong(price.toString()));
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
            String out_trade_no = (String) parseObject.get("out_trade_no");
            if ("SUCCESS".equals(parseObject.get("trade_state"))) {
                System.out.println("-----------------------0-------------------------");
                //支付成功  修改订单状态
                userDepositOrder.setCreateTime(time);
                userDepositOrder.setOrderNumber(out_trade_no);
                userDepositOrder.setStatus(1);
                userDepositOrderMapper.updateUserDepositOrder(userDepositOrder);
                //新增 押金详细
                UserIntegralBalanceDepositVo userIntegralBalanceDepositVo = new UserIntegralBalanceDepositVo();
                Object amount = parseObject.get("amount");
                JSONObject jsonObject1 = JSONObject.parseObject(amount.toString());
                Object price = jsonObject1.get("payer_total");
                System.out.println("-----------------------1-------------------------");
                //记录用户押金详细
                userIntegralBalanceDepositVo.setOpenid(openid.toString());
//                userIntegralBalanceDepositVo.setSum(new BigDecimal(price.toString()));
                userIntegralBalanceDepositVo.setSum(new BigDecimal(price.toString()).divide(new BigDecimal(100)));
                userIntegralBalanceDepositVo.setStatus(0);
                userIntegralBalanceDepositVo.setCreateTime(new Date());
                userDepositDetailMapper.insertUserDepositDetail(userIntegralBalanceDepositVo);
                System.out.println("-----------------------------2--------------------------------");
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
                Long couponPrice = (Long) cacheMap.get("couponPrice");
                //记录优惠券金额
                userLeaseOrder.setCouponPrice(couponPrice);
                //实付金额
                userLeaseOrder.setNetAmount(new BigDecimal(price).multiply(new BigDecimal(100)));
                //付款时间
                userLeaseOrder.setCreateTime(time);
                //支付流水号
                userLeaseOrder.setOrderNumber(out_trade_no);
                //支付方式
                userLeaseOrder.setPayType("1");
                //修改状态
                userLeaseOrder.setStatus("2");
                //支付成功押金订单为0
                userLeaseOrder.setDepositNumber("0");
                //修改用户租赁信息
                userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder);
                if(cacheMap.get("couponId")!=null ||cacheMap.get("couponId")!= ""){
                    //扣除 优惠券
                    UserDiscount userDiscount = new UserDiscount();
                    userDiscount.setStatus(1L);
                    userDiscount.setUserId((Long) cacheMap.get("couponId"));
                    userDiscountMapper.updateUserDiscount(userDiscount);

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

        //根据 openId 查用户余额
        SysUser user = userMapper.selectUserByOpenid(openid);
        Long price = 0L;
        UserDiscount userDiscount = null;
        if (couponId != null){
            //根据优惠券 id 查优惠券金额
            userDiscount = userDiscountMapper.selectUserCouponById(couponId);
            //记录优惠券金额
            userLeaseOrder.setCouponPrice(userDiscount.getPrice().longValue());
            BigDecimal subtract = userLeaseOrder.getPrice().subtract(userDiscount.getPrice());
            price = subtract.longValue();
            if(user.getBalance() < price) {
                return AjaxResult.error("余额不足");
            }
        }
        user.setBalance(user.getBalance() - price);
        userMapper.updateUser(user);

        //实付金额
        userLeaseOrder.setNetAmount(new BigDecimal(price));
        //付款时间
        userLeaseOrder.setCreateTime(DateUtils.getNowDate());
        //订单号
        userLeaseOrder.setOrderNumber(userLeaseOrder.getOrderNumber());
        //修改状态
        userLeaseOrder.setStatus("2");
        //修改用户租赁信息
        return AjaxResult.success(userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder));
    }

    @Override
    public AjaxResult bluetoothCallback(HttpServletRequest request) {
        try {
            String reader = getAllRequestParam2(request);
            log.info("数据上报:============={}", reader);
//            map = JSONObject.parseObject(reader, Map.class);
            JSONObject jsonObject = JSON.parseObject(reader);
            Map<String,Object> map = (Map<String, Object>) jsonObject.get("service");
            String serviceType = (String) map.get("serviceType");
            Map<String,Object> map1 = (Map<String, Object>) map.get("data");
            //锁状态上报
            if (serviceType.equals("VehicleDetectorBasic")){
                int status = Integer.parseInt(map1.get("status").toString());
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
                /**
                 * 长度为 16 位字符，eg：”timestamp”:
                 * "D002061049050051"
                 * 第 1 位为’D’；第 2~4 位为卡槽号/锁号，002 表示 2 号锁
                 * 头；第 5~16 位表示插销 ID/共享物品的 ID，为 12 位字符，
                 * 其中第 16 位表示共享品种类
                 */
                String timestamp = (String) map1.get("timestamp");
                String substring = timestamp.substring(0, 1);

                //子锁号
                String substring1 = timestamp.substring(3, 4);
                String substring2 = timestamp.substring(4, 16);
                log.info("子锁柜号：{}",substring1);
                log.info("子锁唯一标识：{}",substring2);


                if (status == 0){
                    System.out.println("借床");
                    if (substring.equals("D")) {
                        if (temperature == 2 || temperature == 3){
                            UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectOrderByDeviceNumberAndChoose(substring2);
                            if (userLeaseOrder != null){
                                //TODO 计算套餐
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

                                Device device = deviceMapper.selectDeviceByDeviceNumber(deviceNumber);
                                String rows = device.getRows();

                                int count = 0;
                                Gson gson = new Gson();
                                List<ItemVO> itemList = gson.fromJson(rows, new TypeToken<List<ItemVO>>(){}.getType());
                                if (itemList.size() != 0){
                                    for (int i = 0; i < itemList.size(); i++) {
                                        int index = itemList.get(i).getIndex();
                                        if (Integer.parseInt(substring1) == index){
                                            itemList.get(i).setStatus(2);
                                            count=count+1;
                                        }else if (itemList.get(i).getStatus() != 0){
                                            count=count+1;
                                        }
                                    }
                                }
                                if (count == itemList.size()){
                                    deviceMapper.updateDeviceByDeviceNumber(gson.toJson(itemList),deviceNumber,1);
                                }else {
                                    deviceMapper.updateDeviceByDeviceNumber(gson.toJson(itemList),deviceNumber,0);
                                }
                            }
                            System.out.println("借床成功");
                        }else if (temperature == 4){
                            log.info("借床异常");
                        }
                    }
                }else if (status == 1){
                    if (temperature == 0){
                        System.out.println("异常还床");
                    }else if (temperature == 1){
                        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectOrderByDeviceNumber(substring2);
                        if (userLeaseOrder != null){
                            //TODO 计算套餐
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
                                log.info("userLeaseOrder.getTimePrice():{}",userLeaseOrder.getTimePrice());
                                log.info("userLeaseOrder.getFixedPrice():{}",userLeaseOrder.getFixedPrice());
                                log.info("日志：{}",userLeaseOrder.getTimePrice().add(userLeaseOrder.getFixedPrice(), MathContext.DECIMAL32));
                                userLeaseOrder.setPrice(userLeaseOrder.getTimePrice().add(userLeaseOrder.getFixedPrice(), MathContext.DECIMAL32));
                                log.info("修改的id：{}",userLeaseOrder.getId());
                                userLeaseOrderMapper.updateUserLeaseOrder(userLeaseOrder);
                                redisServer.deleteObject(orderPrefix+userLeaseOrder.getOrderNumber());
                            }
                            System.out.println("正常还床");
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
            }else if (serviceType.equals("Battery")){
                //电量上报
                Integer integer = Integer.parseInt(map1.get("batteryLevel").toString());
                log.info("电量:==============={}",integer);
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
        try {
            String reader = getAllRequestParam2(request);
            log.info("PH70数据上报:============={}", reader);
            JSONObject object = JSON.parseObject(reader);
            Map<String,Object> map = (Map<String,Object>)object.get("payload");
            String apPdata = String.valueOf(map.get("APPdata"));
            log.info("PH70数据上报16进制64加密：{}",apPdata);
            byte[] bytes = Base64.getDecoder().decode(apPdata);
            String hexBinary = DatatypeConverter.printHexBinary(bytes);
            log.info("PH70数据上报16进制64解密：{}",hexBinary);
            //7E 0102 0006 0864515065974661 001A 086451506597 38 7E
            /**
             * 7E//:头
             * 0102//:命令ID
             * 0006//:属性
             * 0864515065974661//:IMEI
             * 001A//流水号
             * 086451506597//:消息体
             * 38//:校验
             * 7E//:尾
             */
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AjaxResult XG70NBTCallback(HttpServletRequest request) {
        try {
            String reader = getAllRequestParam2(request);
            log.info("XG70NBT数据上报:============={}", reader);
        }catch (Exception e){
            e.printStackTrace();
        }
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
