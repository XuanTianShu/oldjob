package com.yuepei.service.impl;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.redis.RedisCache;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.service.CallBackService;
import com.yuepei.system.domain.*;
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
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
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

            //支付时间
            Object success_time = parseObject.get("success_time");
            System.out.println(success_time+"微信回调时间");
            LocalDateTime parse = LocalDateTime.parse(success_time.toString(), DateTimeFormatter.ISO_DATE_TIME);
            Date time = Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
            System.out.println(time+"转换后的时间");
            //订单号
            String out_trade_no = (String) parseObject.get("out_trade_no");
            if ("SUCCESS".equals(parseObject.get("trade_state"))) {
                Map<String, Object> cacheMap = redisCache.getCacheMap(out_trade_no);
                Long couponPrice = (Long) cacheMap.get("couponPrice");
                //记录优惠券金额
                userLeaseOrder.setCouponPrice(couponPrice);
                //实付金额
                userLeaseOrder.setNetAmount(new BigDecimal(price));
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
    public AjaxResult balancePrepaymentOrder(String openid, long couponId, UserLeaseOrder userLeaseOrder) {

        //根据 openId 查用户余额
        SysUser user = userMapper.selectUserByOpenid(openid);

        //根据优惠券 id 查优惠券金额
        UserCoupon userCoupon = userCouponMapper.selectUserCouponById(couponId);
        BigDecimal bigDecimal = new BigDecimal(userCoupon.getDiscountAmount());
        BigDecimal subtract = userLeaseOrder.getPrice().subtract(bigDecimal);
        Long price  =  subtract.longValue();
        if(user.getBalance() < userCoupon.getDiscountAmount()){
            return AjaxResult.error("余额不足");
        }else {
            user.setBalance(user.getBalance() - price);
            userMapper.updateUser(user);
            //记录优惠券金额
            userLeaseOrder.setCouponPrice((long) (userCoupon.getDiscountAmount()));
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
                if (status == 0){
                    System.out.println("借床");
                    if (substring.equals("D")) {
                        //子锁号
                        String substring1 = timestamp.substring(3, 4);
                        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectOrderByDeviceNumberAndChoose(deviceNumber, substring1);
                        //订单变成有效订单
                        userLeaseOrder.setIsValid(1L);
                        //TODO 计算套餐
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
                        if (before){
                            long l = startTime.getTime() - parse.getTime();
                            long nd = 1000 * 24 * 60 * 60;
                            long nh = 1000 * 60 * 60;
                            long nm = 1000 * 60;
                            long ns = 1000;
                            long min = l % nd % nh / nm;
                            System.out.println(min+"------"+l);
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
//                        userLeaseOrderMapper.updateUserLeaseOrder(userLeaseOrder);
                    }
                    System.out.println("借床成功");
                }else if (status == 1){
                    if (temperature == 0){
                        System.out.println("异常还床");
                    }else if (temperature == 1){
                        if (substring.equals("D")){
                            //子锁号
                            String substring1 = timestamp.substring(3, 4);
                            UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectOrderByDeviceNumberAndChoose(deviceNumber,substring1);
                            userLeaseOrder.setRestoreTime(new Date());
                            userLeaseOrder.setStatus("1");
                            //TODO 计算订单金额
                            int time = 0;
                            long l = userLeaseOrder.getLeaseTime().getTime() - new Date().getTime();
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

                            long valid = l / ns;
                            if (valid > 60){
                                //收费
                                long keyExpire = redisServer.getKeyExpire(orderPrefix + userLeaseOrder.getOrderNumber());
                                System.out.println(keyExpire+"过期时间");
                            }else {
                                //免费
//                                userLeaseOrder.setPrice(new BigDecimal(0));
                            }
//                            userLeaseOrderMapper.updateUserLeaseOrder(userLeaseOrder);
                        }
                        System.out.println("正常还床");
                    }
                }
            }else if (serviceType.equals("Battery")){
                //电量上报
                Integer integer = Integer.parseInt(map1.get("batteryLevel").toString());
                log.info("电量:==============={}",integer);
            }
//            log.info("map:============={}", map);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("NB数据上报执行事务回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("NB数据上报执行回滚成功");
        }
        return AjaxResult.error();
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
