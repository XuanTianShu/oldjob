package com.yuepei.system.utils;

import com.alibaba.fastjson2.JSON;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.mapper.UserCouponMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis监听类
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Value("${coupon.order}")
    private String orderPrefix;

    @Value("${coupon.prefix}")
    private String userCouponPrefix;

    @Value("${coupon.coin}")
    private String JYBPrefix;

    @Value("${coupon.valid}")
    private String orderValid;

    @Autowired
    private RedisServer redisServer;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * JSON组件
     */
//    @Resource
//    private JsonComponent jsonComponent;
//    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
//        super(listenerContainer);
//    }

    /**
     * 针对 redis 数据失效事件，进行数据处理
     */
    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 获取过期的key
        String key = message.toString();
        if (key != null) {
            if (key.startsWith(orderPrefix)) {
                log.info("开始修改订单状态");
                int index = key.indexOf(orderPrefix);
                //订单编号
                String orderNumber = key.substring(index + orderPrefix.length());
                int indexOf = orderNumber.indexOf("_");
                String substring = orderNumber.substring(0, indexOf);
                log.info("订单号：{}",substring);
                String substring1 = orderNumber.substring(indexOf+1);
                log.info("套餐类型：{}",substring1);
                UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectUserLeaseOrderByOpenId(substring);
                if (userLeaseOrder != null) {
                    //使用套餐
                    String rule = userLeaseOrder.getDeviceRule();
                    JSONArray objects = JSON.parseArray(rule);
                    Map<String, Object> map = new HashMap<>();
                    Map<String, Object> map1 = new HashMap<>();
                    for (int i = 0; i < objects.size(); i++) {
                        JSONObject jsonObject = JSON.parseObject(objects.get(i).toString());
                        int timeStatus = Integer.parseInt(jsonObject.get("time").toString());
                        if (timeStatus == 0) {
                            map = (Map<String, Object>) JSON.parseObject(objects.get(i).toString());
                        } else {
                            map1 = (Map<String, Object>) JSON.parseObject(objects.get(i).toString());
                        }
                    }
                    String m = "HH:mm:ss";
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
                    simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    String endTime = (String) map1.get("endTime");
                    String startTime = (String) map1.get("startTime");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    if (substring1.equals("1")){
                        //TODO 固定套餐
                        log.info("固定套餐结束");
                        log.info("开始转计时套餐");
                        BigDecimal bigDecimal = new BigDecimal(map1.get("price").toString());
                        //TODO 固定套餐到自动转为计时套餐

                        Date parse = simpleDateFormat1.parse(endTime);
                        Date parse2 = simpleDateFormat1.parse(startTime);

                        String format = new SimpleDateFormat("HH:mm:ss").format(parse2);
                        Date parse3 = simpleDateFormat1.parse(format);
                        log.info("开始时间：{}",parse3);
                        String currentTime = new SimpleDateFormat("HH:mm:ss").format(parse);
                        Date parse1 = simpleDateFormat1.parse(currentTime);
                        log.info("结束时间：{}",parse1);

                        Date nowTime = new SimpleDateFormat(m).parse(simpleDateFormat1.format(new Date()));

                        if (nowTime.getTime() > parse3.getTime()){
                            log.info("跨天");
                            SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.SECOND,0); //这是将当天的【秒】设置为0
                            calendar.set(Calendar.MINUTE,0); //这是将当天的【分】设置为0
                            calendar.set(Calendar.HOUR_OF_DAY,0); //这是将当天的【时】设置为0
                            calendar.setTime(parse3);
                            String ymd = sdfYMD.format(calendar.getTime()); //2021-02-24 00:00:00
                            log.info("指定的时间:{}",ymd);
                            Long tommowStamp = calendar.getTimeInMillis() + 86400000; //86400000 一天的毫秒值
                            String sj = sdfYMD.format(new Date(tommowStamp));
                            Date parse4 = simpleDateFormat.parse(sj);
                            log.info("指定开始时间:{}",parse3);
                            log.info("指定第二天的时间:{}",parse4);
                            log.info("结束判断固定套餐是否跨天");
                            log.info("开始判断当前时间是否在固定套餐时间之内");
                            long l = parse4.getTime() - nowTime.getTime();
                            log.info("{}==={}",parse4,nowTime);
                            log.info("{}==={}",parse4.getTime(),nowTime.getTime());
                            long valid = l / 1000;
                            BigDecimal fixedPrice1 = userLeaseOrder.getFixedPrice();
                            userLeaseOrder.setFixedPrice(fixedPrice1.add(bigDecimal));
                            userLeaseOrder.setTimeTimestamp(String.valueOf(valid));
                            redisServer.setCacheObject(orderPrefix+substring+"_0",substring,new Long(valid).intValue(),TimeUnit.SECONDS);
                        }else {
                            log.info("不跨天");
                            long l = parse3.getTime() - nowTime.getTime();
                            long valid = l / 1000;
                            BigDecimal fixedPrice1 = userLeaseOrder.getFixedPrice();
                            userLeaseOrder.setFixedPrice(fixedPrice1.add(bigDecimal));
                            userLeaseOrder.setTimeTimestamp(String.valueOf(valid));
                            redisServer.setCacheObject(orderPrefix+substring+"_0",substring,new Long(valid).intValue(),TimeUnit.SECONDS);
                        }

                    }else if (substring1.equals("0")){
                        //TODO 计时套餐
                        log.info("计时套餐结束");
                        log.info("开始转固定套餐");
                        Calendar instance = Calendar.getInstance();
                        instance.setTime(userLeaseOrder.getLeaseTime());
                        int i = instance.get(Calendar.HOUR_OF_DAY);
                        int i1 = instance.get(Calendar.MINUTE);
                        int i2 = instance.get(Calendar.SECOND);
                        String s1 = null;
                        if (i1 <= 9) {
                            s1 = i + ":" + "0" + i1 + ":" + i2;
                        } else {
                            s1 = i + ":" + i1 + ":" + i2;
                        }
                        Date parse2 = simpleDateFormat.parse(s1);
                        Date parse3 = simpleDateFormat.parse(startTime);
                        Date parse5 = simpleDateFormat.parse(endTime);
                        long l = parse3.getTime() - parse2.getTime();
                        log.info("{}======{}",parse3,parse2);

                        int time = 0;

                        long nd = 1000 * 24 * 60 * 60;
                        long nh = 1000 * 60 * 60;
                        long nm = 1000 * 60;

                        // 计算差多少天
                        long day = l / nd;
                        // 计算差多少小时
                        long hour = l % nd / nh;
                        // 计算差多少分钟
                        long min = l % nd % nh / nm;

                        if (day != 0) {
                            time += day * 24;
                        }
                        if (hour != 0) {
                            time += hour;
                        }
                        if (min > 0) {
                            time = time + 1;
                        }
                        BigDecimal price = new BigDecimal(map.get("price").toString());
                        log.info("计时套餐的价格:{}",price);
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        BigDecimal multiply = price.multiply(new BigDecimal(time));
                        log.info("{}",new BigDecimal(time));
                        BigDecimal bigDecimal = new BigDecimal(decimalFormat.format(multiply));
                        BigDecimal add = bigDecimal.add(userLeaseOrder.getTimePrice());
                        log.info("计算后的计时套餐的价格:{}",add);
                        userLeaseOrder.setTimePrice(add);

                        String format1 = new SimpleDateFormat("HH:mm:ss").format(parse5);
                        Date parse6 = simpleDateFormat1.parse(format1);

                        String format = new SimpleDateFormat("HH:mm:ss").format(parse3);
                        Date parse = simpleDateFormat1.parse(format);

                        String currentTime = new SimpleDateFormat("HH:mm:ss").format(parse2);
                        Date parse1 = simpleDateFormat1.parse(currentTime);
                        log.info("开始判断固定套餐是否跨天");
                        log.info("{}---{}----{}",parse1,parse,parse6);
                        log.info("{}---{}----{}",parse1.getTime(),parse.getTime(),parse6.getTime());
                        if (parse.getTime() > parse6.getTime()){
                            log.info("跨天");
                            SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.SECOND,0); //这是将当天的【秒】设置为0
                            calendar.set(Calendar.MINUTE,0); //这是将当天的【分】设置为0
                            calendar.set(Calendar.HOUR_OF_DAY,0); //这是将当天的【时】设置为0
                            calendar.setTime(parse);
                            String ymd = sdfYMD.format(calendar.getTime()); //2021-02-24 00:00:00
                            log.info("指定的时间:{}",ymd);
                            Long tommowStamp = calendar.getTimeInMillis() + 86400000; //86400000 一天的毫秒值
                            String sj = sdfYMD.format(new Date(tommowStamp));
                            Date parse4 = simpleDateFormat.parse(sj);
                            log.info("指定开始时间:{}",parse1);
                            log.info("指定第二天的时间:{}",parse4);
                            log.info("结束判断固定套餐是否跨天");
                            log.info("开始判断当前时间是否在固定套餐时间之内");
                            long l1 = parse4.getTime() - parse.getTime();
                            log.info("{}",l1);
                            long valid = l1 / 1000;
                            log.info("valid:{}",valid);
                            userLeaseOrder.setFixedTimestamp(String.valueOf(valid));
                            //TODO 进入固定套餐
                            log.info("进入固定套餐");
                            //TODO 当前时间和固定套餐结束时间计算多少秒钟
                            redisServer.setCacheObject(orderPrefix+substring+"_1",orderNumber,new Long(valid).intValue(), TimeUnit.SECONDS);
                        }else {
                            log.info("不跨天");
                            long l1 = parse6.getTime() - parse.getTime();
                            log.info("{}",l1);
                            long valid = l1 / 1000;
                            log.info("valid:{}",valid);
                            log.info("结束判断当前时间是否在固定套餐时间之内");
                            //TODO 进入固定套餐
                            log.info("进入固定套餐");
                            //TODO 当前时间和固定套餐结束时间计算多少秒钟
                            redisServer.setCacheObject(orderPrefix+substring+"_1",orderNumber,new Long(valid).intValue(),TimeUnit.SECONDS);
                        }
                        log.info("固定转成功");
                    }
                    log.info("修改订单信息");
                    userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder);
                }
                log.info("结束修改订单状态");
            } else if (key.startsWith(userCouponPrefix)) {
                log.info("开始修改优惠券状态");
                int index = key.indexOf(userCouponPrefix);
                String userCoupon = key.substring(index + userCouponPrefix.length());
                int indexOf = userCoupon.indexOf("_");
                String substring = userCoupon.substring(indexOf + 1);
                log.info("用户编号:{}",substring);
                userCouponMapper.batchUpdateUserCoupon(substring);
                log.info("过期值：{}",userCoupon);
                log.info("结束修改优惠券状态");
            } else if (key.startsWith(JYBPrefix)){
                log.info("开始修改兑换券状态");
                int index = key.indexOf(JYBPrefix);
                String JYB = key.substring(index + JYBPrefix.length());
                int indexOf = JYB.indexOf('_'); //获取下划线的索引位置
                String result = JYB.substring(indexOf+1); //截取下划线之后的字符串
                log.info("用户编号:{}",result);
                userCouponMapper.batchUpdateUserCoupon(result);
                log.info("JYB:{}",JYB);
                log.info("过期值：{}",JYB);
                log.info("结束修改兑换券状态");
            } else if (key.startsWith(orderValid)){
                log.info("开始删除无效订单");
                int index = key.indexOf(orderValid);
                String orderNumber = key.substring(index + orderValid.length());
                log.info("订单号：{}",orderNumber);
                userLeaseOrderMapper.deleteUserLeaseOrderByOrderNumber(orderNumber);
                log.info("结束删除无效订单");
            }
        }
    }

}

