package com.yuepei.service.impl;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuepei.common.annotation.DataScope;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.Item;
import com.yuepei.service.MyLeaseOrderService;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.*;
import com.yuepei.system.utils.RedisServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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
 * @create ：2023/1/9 18:34
 **/
@Slf4j
@Service
public class MyLeaseOrderServiceImpl implements MyLeaseOrderService {
    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private RedisServer redisServer;

    @Autowired
    private DeviceInvestorMapper deviceInvestorMapper;

    @Autowired
    private DeviceHospitalMapper deviceHospitalMapper;

    @Autowired
    private DeviceAgentMapper deviceAgentMapper;

    @Autowired
    private OrderProportionMapper orderProportionMapper;

    @Value("${coupon.valid}")
    private String orderValid;

    @Value("${coupon.order}")
    private String orderPrefix;

    @Override
    public List<UserLeaseOrder> userLeaseOrder(String openid, Integer status) {
        return userLeaseOrderMapper.userLeaseOrder(openid,status);
    }

    @Transactional
    @Override
    public AjaxResult insertUserLeaseOrder(String openid, String rows, UserLeaseOrder userLeaseOrder) {

        if (userLeaseOrder.getChoose() == 0){
            UserLeaseOrder selectLeaseOrderByDeviceNumber = userLeaseOrderMapper.selectLeaseOrderByDeviceNumber(userLeaseOrder.getDeviceNumber());
            if (selectLeaseOrderByDeviceNumber != null){
                return AjaxResult.error("该设备已被租赁！");
            }
        }else {
            int i = userLeaseOrderMapper.selectUSerLeaseOrderByChild(userLeaseOrder);
            if (i > 0){
                return AjaxResult.error("该设备已被租赁！");
            }
        }

        log.info(userLeaseOrder.getDeviceNumber());
        try {
            //修改该设备状态
//            Device deviceNumber = deviceMapper.selectDeviceByDeviceNumber(userLeaseOrder.getDeviceNumber());
//            if (deviceNumber.getElectricEarly() > userLeaseOrder.getElectric()){
//                deviceNumber.setElectric(userLeaseOrder.getElectric());
//                deviceNumber.setStatus(2L);
//                deviceMapper.updateDevice(deviceNumber);
//                System.out.println("出现故障无法借床!");
//                return AjaxResult.error("出现故障无法借床！");
//            }
            //TODO 生成维修记录通知维修人员
            List<String> list = userLeaseOrderMapper.selectUserDepositList(openid, userLeaseOrder.getDeviceNumber());
            List<String> uSerLeaseOrderDeposit = userLeaseOrderMapper.selectUSerLeaseOrderDeposit(openid,userLeaseOrder.getDeviceNumber());
            if (list.size() != 0){
                //创建订单号
                if(userLeaseOrder.getOrderNumber() == null ){

                    if (uSerLeaseOrderDeposit.size() != 0){
                        for (String s : uSerLeaseOrderDeposit) {
                            for (int k = list.size() - 1; k >= 0; k--) {
                                if (s.equals(list.get(k))) {
                                    list.remove(k);
                                }
                            }
                        }
                    }
                    //创建订单
                    //String orderNumber = UUID.randomUUID().toString().replace("-", "");
                    System.out.println("借床");
                    String uuid = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
                    String orderNumber = uuid.substring(uuid.length() - 16);
                    //添加投资人订单分成明细
                    //投资人子账户
                    List<OrderProportionDetail> orderProportionDetailList = deviceInvestorMapper.selectInvestorListByDeviceNumber(userLeaseOrder.getDeviceNumber());
                    if (orderProportionDetailList.size() > 0){
                        List<DeviceInvestorAccount> deviceInvestorAccountList = orderProportionMapper.selectInvestorAccount(orderProportionDetailList);
                        if (deviceInvestorAccountList.size() > 0){
                            orderProportionMapper.insertAgentAccountProportion(orderNumber,deviceInvestorAccountList);
                        }
                        userLeaseOrderMapper.insertOrderProportionDeatail(orderNumber,orderProportionDetailList);
                    }


                    //添加医院分成比例
                    DeviceHospital deviceHospital = deviceHospitalMapper.selectHospitalListByDeviceNumber(userLeaseOrder.getDeviceNumber());
                    if (deviceHospital != null){
                        if (deviceHospital.getType() != null){
                            if (deviceHospital.getType().equals("0")){
                                userLeaseOrder.setHospitalProportion(Long.parseLong(deviceHospital.getProportion()));
                            }else {
                                userLeaseOrder.setAgentHospitalProportion(deviceHospital.getProportion());
                                orderProportionMapper.insertAgentHospitalProportion(orderNumber,deviceHospital.getHospitalId(),deviceHospital.getUserId(),deviceHospital.getProportion());
                            }
                            userLeaseOrder.setHospitalId(deviceHospital.getHospitalId());
                        }
                    }else {
                        userLeaseOrder.setHospitalProportion(0L);
                        userLeaseOrder.setAgentHospitalProportion("0");
                    }


                    //添加代理商分成比例
                    //代理商子账户
                    DeviceAgent deviceAgent = deviceAgentMapper.selectAgentListByDeviceNumber(userLeaseOrder.getDeviceNumber());
                    if (deviceAgent != null){
                        userLeaseOrder.setAgentId(deviceAgent.getAgentId());
                        userLeaseOrder.setAgentProportion(Long.parseLong(deviceAgent.getProportion()));
                        List<DeviceAgent> deviceAgentList = deviceAgentMapper.selectAgentAccountList(userLeaseOrder.getDeviceNumber());
                        if (deviceAgentList.size() > 0){
                            orderProportionMapper.insertAgentProportion(orderNumber,deviceAgentList);
                        }
                    }else {
                        userLeaseOrder.setAgentProportion(0L);
                    }



                    //计算订单代理商、投资人、医院分成比例
                    TotalProportionVO totalProportionVO = deviceMapper.selectDeviceProportionDetail(userLeaseOrder.getDeviceNumber());

                    //记录订单分成比例详情
                    Device device = deviceMapper.selectDeviceByDeviceNumber(userLeaseOrder.getDeviceNumber());
                    DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(device.getDeviceTypeId());
                    AgentAndHospitalNameVO agentAndHospitalNameVO = userLeaseOrderMapper.selectUserNameAndHospitalName(userLeaseOrder.getDeviceNumber());
                    userLeaseOrder.setDeviceType(deviceType.getDeviceTypeName());
                    userLeaseOrder.setLeaseTime(new Date());

                    log.info("医院：{}",userLeaseOrder.getHospitalProportion());
                    log.info("{}",totalProportionVO.getDiProportion());
                    log.info("代理商：{}",userLeaseOrder.getAgentProportion());

                    int proportionCount = 100-(Integer.parseInt(String.valueOf(userLeaseOrder.getHospitalProportion()))
                            + totalProportionVO.getDiProportion() + Integer.parseInt(String.valueOf(userLeaseOrder.getAgentProportion())));

                    userLeaseOrder.setPlatformProportion(Long.parseLong(String.valueOf(proportionCount)));
//                    userLeaseOrder.setHospitalId(String.valueOf(device.getHospitalId()));
//                    userLeaseOrder.setAgentId(String.valueOf(device.getUserId()));
                    userLeaseOrder.setInvestorId(device.getInvestorId());
//                    userLeaseOrder.setAgentProportion(Long.parseLong(String.valueOf(totalProportionVO.getSuProportion())));
//                    userLeaseOrder.setHospitalProportion(Long.parseLong(String.valueOf(totalProportionVO.getHProportion())));
//                    userLeaseOrder.setInvestorProportion(Long.parseLong(String.valueOf(totalProportionVO.getDiProportion())));
                    userLeaseOrder.setOrderNumber(orderNumber);
                    userLeaseOrder.setOpenid(openid);
                    userLeaseOrder.setInvestorProportion(totalProportionVO.getDiProportion().longValue());
                    userLeaseOrder.setUserName(agentAndHospitalNameVO.getUserName());
                    userLeaseOrder.setHospitalName(agentAndHospitalNameVO.getHospitalName());
                    userLeaseOrder.setDeviceNumber(userLeaseOrder.getDeviceNumber());
                    userLeaseOrder.setDepositNumber(list.get(0));
                    userLeaseOrder.setDeviceRule(userLeaseOrder.getRule());
                    userLeaseOrder.setDeposit(new BigDecimal(String.valueOf(deviceType.getDeviceTypeDeposit())).longValue());

//                    redisServer.setCacheObject(orderValid+orderNumber,userLeaseOrder,300,TimeUnit.SECONDS);
                    System.out.println(userLeaseOrder.getRule()+"--前端传的--");

                    log.info("rows:{}",rows);
                    if (!rows.equals("1")){
                        ObjectMapper objectMapper = new ObjectMapper();
                        List<Item> itemList;
                        itemList = objectMapper.readValue(rows, new TypeReference<List<Item>>() {
                        });
                        for (int i = Objects.requireNonNull(itemList).size() - 1; i >= 0; i--) {
                            //0可用1故障2租赁中
                            if (itemList.get(i).getStatus() != 0){
                                itemList.remove(i);
                            }
                        }
                        if (itemList.size() != 0){
                            System.out.println(0+"====");
                            //修改 格子柜状态
                            deviceMapper.updateDeviceByDeviceNumber(rows,userLeaseOrder.getDeviceNumber(),0);
                            System.out.println("修改成功");
                        }else {
                            System.out.println(1+"=====");
                            //修改 格子柜状态
                            deviceMapper.updateDeviceByDeviceNumber(rows,userLeaseOrder.getDeviceNumber(),1);
                            System.out.println("修改成功");
                        }
                        userLeaseOrder.setIsValid(1L);
                    }else {
                        System.out.println("修改单个锁");
                        Device device1 = new Device();
                        device1.setDeviceNumber(userLeaseOrder.getDeviceNumber());
                        device1.setStatus(1L);
                        deviceMapper.updateDeviceStatus(device1);
                        userLeaseOrder.setIsValid(1L);
                    }
                    log.info("参数：{}",userLeaseOrder.getIsValid());

                    userLeaseOrderMapper.insertUserLeaseOrder(userLeaseOrder);

                    //将订单信息存放到redis
                    System.out.println("新增内容");
                    UserLeaseOrder userLeaseOrder1 = userLeaseOrderMapper.selectUserLeaseOrderByOpenId(orderNumber);
                    System.out.println("订单编号："+orderNumber);
                    String rule = userLeaseOrder.getRule();
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
                    System.out.println("开始判断");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    String format = simpleDateFormat.format(userLeaseOrder1.getLeaseTime());

                    Date parse = simpleDateFormat.parse(format);
                    Date startTime = simpleDateFormat.parse(map1.get("startTime").toString());
                    Date endTime = simpleDateFormat.parse(map1.get("endTime").toString());
                    boolean before = parse.before(startTime);
                    System.out.println(parse+"下单时间");
                    System.out.println(startTime+"固定套餐开始时间");
                    System.out.println(before+"结果");
                    if (before){
                        long l = startTime.getTime() - parse.getTime();
                        long nd = 1000 * 24 * 60 * 60;
                        long nh = 1000 * 60 * 60;
                        long nm = 1000 * 60;
                        long ns = 1000;
                        long sec = l % nd % nh % nm / ns;
                        long valid = l / ns;
                        log.info("sec:{}",sec);
                        log.info("valid:{}",valid);
                        redisServer.setCacheObject(orderPrefix+userLeaseOrder1.getOrderNumber()+"_0",userLeaseOrder1,
                                new Long(valid).intValue(), TimeUnit.SECONDS);
                        System.out.println("存储到redis1");
                    }else {

                        redisServer.setCacheObject(orderPrefix+userLeaseOrder1.getOrderNumber()+"_1",userLeaseOrder1);
                        System.out.println("存储到redis2");
                    }
//                    redisServer.setCacheObject(orderPrefix+userLeaseOrder1.getOrderNumber(),userLeaseOrder1);
                    log.info("借床ok");



                    log.info("执行完");
//                    return AjaxResult.success();
                }
//                else {
                    //计算使用时长
//                    UserLeaseOrder userLease = userLeaseOrderMapper.selectLeaseOrderDetails(userLeaseOrder.getOrderNumber());
//                    long time = userLease.getLeaseTime().getTime();
//                    Date date = new Date();
//                    long l = date.getTime() - time;
//                    userLeaseOrder.setPlayTime(String.valueOf(l));

                    // 计算计时套餐费用和固定套餐费用
//                    String deviceRule = userLease.getDeviceRule();
//                    JSONArray objects = JSON.parseArray(deviceRule);
//                    Map<String, Object> map = new HashMap<>();
//                    Map<String, Object> map1 = new HashMap<>();
//                    for (int i = 0; i < objects.size(); i++) {
//                        JSONObject jsonObject = JSON.parseObject(objects.get(i).toString());
//                        int timeStatus = Integer.parseInt(jsonObject.get("time").toString());
//                        if (timeStatus == 0){
//                             map = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
//                        }else {
//                             map1 = (Map<String,Object>)JSON.parseObject(objects.get(i).toString());
//                        }
//                    }
//                    System.out.println(userLease.getLeaseTime());
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//                    String format = simpleDateFormat.format(userLease.getLeaseTime());
//                    String startTime = (String) map1.get("startTime");




//                    System.out.println("还床");
                    //修改订单
//                    userLeaseOrder.setStatus("1");
//                    userLeaseOrder.setDeviceNumber(userLeaseOrder.getDeviceNumber());
//                    userLeaseOrder.setRestoreTime(new Date());
//                    userLeaseOrderMapper.updateUserLeaseOrderByOrderNumber(userLeaseOrder);
//                    System.out.println("还床ok");

//                    if (rows != null){
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        List<Item> itemList;
//                        itemList = objectMapper.readValue(rows, new com.fasterxml.jackson.core.type.TypeReference<List<Item>>() {
//                        });
//                        for (int i = Objects.requireNonNull(itemList).size() - 1; i >= 0; i--) {
//                            if (itemList.get(i).getStatus() != 0){
//                                itemList.remove(i);
//                            }
//                        }
//                        if (itemList.size() != 0){
//                            System.out.println(0+"====");
                            //修改 格子柜状态
//                            deviceMapper.updateDeviceByDeviceNumber(rows,userLeaseOrder.getDeviceNumber(),0);
//                            System.out.println("修改成功");
//                        }else {
//                            System.out.println(1+"=====");
                            //修改 格子柜状态
//                            deviceMapper.updateDeviceByDeviceNumber(rows,userLeaseOrder.getDeviceNumber(),1);
//                            System.out.println("修改成功");
//                        }
//                    }else {
//                        System.out.println(userLeaseOrder.getDeviceNumber()+"--------"+"修改单个锁");
//                        Device device = new Device();
//                        device.setDeviceNumber(userLeaseOrder.getDeviceNumber());
//                        device.setStatus(1L);
//                        deviceMapper.updateDevice(device);
//                    }

                    return AjaxResult.success();
//                }
            }else {
                return AjaxResult.error("未缴纳押金");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("执行事务回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("执行回滚成功");
            return AjaxResult.error();
        }
    }

    /**
     * 查询租赁订单
     * @param leaseOrderVO
     * @return
     */
    @Override
    @DataScope(deptAlias = "lo",userAlias = "lo")
    public List<UserLeaseOrder> leaseOrderList(LeaseOrderVO leaseOrderVO) {
        return userLeaseOrderMapper.leaseOrderList(leaseOrderVO);
    }

    @Override
    @DataScope(userAlias = "user",deptAlias = "user")
    public OrderSumAndMoneyVO selectDayOrder(LeaseOrderVO leaseOrderVO) {
        return userLeaseOrderMapper.selectDayOrder(leaseOrderVO);
    }

    @Override
    public ConditionOrderVO selectConditionOrder(LeaseOrderVO leaseOrderVO) {
        return userLeaseOrderMapper.selectConditionOrder(leaseOrderVO);
    }

}
