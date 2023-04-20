package com.yuepei;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class YuePeiApplication
{
    public static void main(String[] args) throws ParseException {
        String rule = "[{\"time\":0,\"price\":1,\"startTime\":\"00:45:00\",\"endTime\":\"01:00:00\"}," +
                "{\"time\":1,\"price\":2,\"startTime\":\"10:00:00\",\"endTime\":\"15:00:00\"}]";
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
        String string = map1.get("startTime").toString();
        String string1 = map1.get("endTime").toString();
        System.out.println(string+"==="+string1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Date parse2 = simpleDateFormat1.parse(string);
        Date parse3 = simpleDateFormat1.parse(string1);

        String currentTime = new SimpleDateFormat("HH:mm:ss").format(parse2);
        Date parse1 = simpleDateFormat1.parse(currentTime);
        String format = new SimpleDateFormat("HH:mm:ss").format(parse3);
        Date parse = simpleDateFormat1.parse(format);
        System.out.println("开始时间"+parse1);
        System.out.println("结束时间"+parse);
        int time = 0;
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        if (parse1.getTime() > parse.getTime()) {
            SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.SECOND,0); //这是将当天的【秒】设置为0
            calendar.set(Calendar.MINUTE,0); //这是将当天的【分】设置为0
            calendar.set(Calendar.HOUR_OF_DAY,0); //这是将当天的【时】设置为0
            calendar.setTime(parse);
            String ymd = sdfYMD.format(calendar.getTime()); //2021-02-24 00:00:00
            System.out.println(ymd+"指定的时间");
            Long tommowStamp = calendar.getTimeInMillis() + 86400000; //86400000 一天的毫秒值
            String sj = sdfYMD.format(new Date(tommowStamp));
            Date parse4 = simpleDateFormat.parse(sj);
            System.out.println(parse1+"开始时间");
            System.out.println(parse4+"指定第二天的时间");
            System.out.println(parse4.getTime() + "----" + parse1.getTime());
            long l = parse4.getTime() - parse1.getTime();
            System.out.println(l+"多少毫秒");
            // 计算差多少天
            long day =l / nd;
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
                time += 1;
            }
            System.out.println("time:"+time);
            System.out.println("超过当日");
        }else {
            long l = parse.getTime() - parse1.getTime();
            System.out.println(l);
            // 计算差多少天
            long day =l / nd;
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
                time += 1;
            }
            System.out.println("time:"+time);
            System.out.println("未超过");
        }


        String m = "HH:mm:ss";
        //Calendar calendar = Calendar.getInstance();
        String format1 = simpleDateFormat1.format(new Date());
        Date parse4 = simpleDateFormat1.parse(format1);
        System.out.println(parse4);
        //获取当前时间
        //Date nowTime = new SimpleDateFormat(format).parse(Tools.getTimeString(calendar, format));
        Date nowTime = new SimpleDateFormat(m).parse(simpleDateFormat1.format(new Date()));
        //范围开始时间
        Date startTime = new SimpleDateFormat(m).parse(format1);
        //范围结束时间
        Date endTime = new SimpleDateFormat(m).parse(simpleDateFormat1.format(parse));
        boolean in = DateUtil.isIn(nowTime, startTime, endTime);
        System.out.println(in);
        SpringApplication.run(YuePeiApplication.class, args);
    }
}
