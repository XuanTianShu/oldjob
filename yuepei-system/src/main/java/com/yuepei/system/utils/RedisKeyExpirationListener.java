package com.yuepei.system.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.mapper.DeviceMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * redis监听类
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {


//    @Value("${mqtt.devicePre}")
//    private String devicePre;

    @Value("${coupon.order}")
    private String orderPrefix;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

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
        int index = key.indexOf(orderPrefix);
        if (index != -1){
            //订单编号
            String orderNumber = key.substring(index + orderPrefix.length());
            UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectUserLeaseOrderByOpenId(orderNumber);
            if (userLeaseOrder != null){
                //使用套餐
                String rule = userLeaseOrder.getDeviceRule();
                JSONArray objects = JSON.parseArray(rule);
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> map1 = new HashMap<>();
                for (int i = 0; i < objects.size(); i++) {
                    JSONObject jsonObject = JSON.parseObject(objects.get(i).toString());
                    int timeStatus = Integer.parseInt(jsonObject.get("time").toString());
                    if (timeStatus == 0){
                        map = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                    }else {
                        map1 = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
                    }
                }

                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                Calendar instance = Calendar.getInstance();
                instance.setTime(userLeaseOrder.getLeaseTime());
                int i = instance.get(Calendar.HOUR_OF_DAY);
                int i1 = instance.get(Calendar.MINUTE);
                int i2 = instance.get(Calendar.SECOND);
                String s1 = null;
                if (i1 <= 9){
                    s1 = i + ":" +"0"+ i1 + ":" + i2;
                }else {
                    s1 = i + ":" + i1 + ":" + i2;
                }
                //下单时间
//                Date leaseTime = userLeaseOrder.getLeaseTime();
                //固定套餐开始时间
//                Date startTime = simpleDateFormat.parse(map1.get("startTime").toString());
                String startTime = (String) map1.get("startTime");
                Date parse2 = simpleDateFormat.parse(s1);
                Date parse3 = simpleDateFormat.parse(startTime);
                long l = parse3.getTime() - parse2.getTime();
                System.out.println(parse3+"====="+parse2);

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

                if (day != 0){
                    time += day * 24;
                }
                if (hour != 0){
                    time += hour;
                }
                if (min > 0){
                    time += time+1;
                }
                BigDecimal price = new BigDecimal(map1.get("price").toString());
                System.out.println(price+"计时套餐的价格");
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                BigDecimal multiply = price.multiply(new BigDecimal(time));
                System.out.println(new BigDecimal(time)+"======");
                System.out.println(multiply+"计算后的计时套餐的价格");
                userLeaseOrder.setTimePrice(new BigDecimal(decimalFormat.format(multiply)));
                userLeaseOrderMapper.updateUserLeaseOrder(userLeaseOrder);
                System.out.println("修改成功");
            }
        }
//        int index = key.indexOf(devicePre);
//        if (index != -1) {
            //截取设备编号
//            String deviceNumber = key.substring(index + devicePre.length());
            //获取设备信息
//            Device deviceByNumber = deviceMapper.deviceSelect(deviceNumber);
//            System.out.println(deviceNumber);
            //防止设备被删除，导致空指针
//            if (deviceByNumber != null) {
                //设置设备状态为离线
//                deviceByNumber.setStatus(1);
//                deviceMapper.updateStateDevice(deviceByNumber);
//            }
//        }
    }

}

