package com.yuepei.investor.service.Impl;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.domain.vo.InvestorHospitalVO;
import com.yuepei.investor.domain.vo.InvestorOrderVO;
import com.yuepei.investor.domain.vo.InvestorRevenueVO;
import com.yuepei.investor.mapper.AppletInvestorMapper;
import com.yuepei.investor.service.AppletInvestorService;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.IndexVo;
import com.yuepei.system.domain.vo.OrderVo;
import com.yuepei.system.domain.vo.TotalVo;
import com.yuepei.system.domain.vo.UserLeaseOrderVo;
import com.yuepei.system.mapper.DeviceTypeMapper;
import com.yuepei.system.mapper.HospitalDeviceMapper;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppletInvestorServiceImpl implements AppletInvestorService {

    @Autowired
    private AppletInvestorMapper appletInvestorMapper;

    @Autowired
    private HospitalDeviceMapper hospitalDeviceMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    /**
     * 首页
     * @param user
     * @param investorHospitalVO
     * @return
     */
    @Override
    public Map<String, Object> selectHome(SysUser user, InvestorHospitalVO investorHospitalVO) {
        Map<String, Object> map = new HashMap<>();
        //医院
        List<InvestorHospitalVO> hospitalList = appletInvestorMapper.selectHospitalByInvestorId(user.getUserId());
        //设备数量
        int deviceCount = appletInvestorMapper.selectDeviceCountByInvestorId(user.getUserId(),investorHospitalVO.getHospitalId());
        //设备总收益
        BigDecimal deviceEarning = appletInvestorMapper.selectDeviceEarningsByInvestorId(user.getUserId(),investorHospitalVO.getHospitalId());
        map.put("hospitalList",hospitalList);
        map.put("deviceCount",deviceCount);
        map.put("deviceEarning",deviceEarning);
        return map;
    }

    /**
     * 昨日营收
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> yesterdayRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.yesterdayRevenue(user.getUserId(),hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.yesterdayRevenueList(user.getUserId(),hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    /**
     * 今日营收
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> todayRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.todayRevenue(user.getUserId(),hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.todayRevenueList(user.getUserId(),hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    /**
     * 本月营收
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> monthRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.monthRevenue(user.getUserId(),hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.monthRevenueList(user.getUserId(),hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    /**
     * 累计营收
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> accumulativeRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.accumulativeRevenue(user.getUserId(),hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.accumulativeRevenueList(user.getUserId(),hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    @Override
    public IndexVo indexPage(Long userId) {
        IndexVo indexVo = new IndexVo();
        List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(userId);
        List<UserLeaseOrderVo> userLeaseOrderVos = new ArrayList<>();
        deviceList.stream().forEach(map->{
            List<UserLeaseOrder> userLeaseOrder = hospitalDeviceMapper.selectLeaseOrderByDeviceNumber(map.getDeviceNumber());
            userLeaseOrder.stream().forEach(i->{
                UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
                BeanUtils.copyProperties(i,userLeaseOrderVo);
                userLeaseOrderVo.setNetAmount(i.getNetAmount());
                userLeaseOrderVos.add(userLeaseOrderVo);
            });
        });
        BigDecimal decimal = userLeaseOrderVos.stream().map(UserLeaseOrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<DeviceType> deviceTypes = new ArrayList<>();
        deviceList.stream().forEach(map->{
            DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map.getDeviceTypeId());
            deviceTypes.add(deviceType);
        });
        indexVo.setDeviceAmount(decimal);
        indexVo.setSum(deviceList.size());
        return indexVo;
    }

    @Override
    public TotalVo selectRevenueStatistics(Long userId, int statistics) {
        SysUser user = sysUserMapper.selectUserById(userId);
        List<String> deviceNumberList = new ArrayList<>();
        List<Long> hospitalIds = new ArrayList<>();
        List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(user.getUserId());
        deviceList.stream().forEach(map->{
            deviceNumberList.add(map.getDeviceNumber());
            hospitalIds.add(map.getHospitalId());
        });
        List<Long> collect = hospitalIds.stream().distinct().collect(Collectors.toList());
        TotalVo totalVo = new TotalVo();
        List<UserLeaseOrder> userLeaseOrderList = new ArrayList<>();
        if (deviceNumberList.size()!=0){
            List<OrderVo> orderVos = new ArrayList<>();
            if (statistics == 1) {
                collect.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectRevenueStatistics(deviceNumberList, map);
                    userLeaseOrderList.addAll(userLeaseOrders);
                });
                Date dNow = new Date();   //当前时间
                Date dBefore = new Date();
                Calendar calendar = Calendar.getInstance(); //得到日历
                calendar.setTime(dNow);//把当前时间赋给日历
                calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
                dBefore = calendar.getTime();   //得到前一天的时间
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
                String defaultStartDate = sdf.format(dBefore);    //格式化前一天
                String now = sdf.format(dNow);
                List<UserLeaseOrder> userLeaseOrders = new ArrayList<>();
                try {
                    //使用SimpleDateFormat的parse()方法生成Date
                    Date date = sdf.parse(defaultStartDate);
                    Date parse = sdf.parse(now);
                    List<UserLeaseOrder> userLeaseOrder = userLeaseOrderList.stream()
                            .filter(s->s.getLeaseTime().getTime()<parse.getTime())
                            .filter(s->s.getLeaseTime().getTime()>date.getTime())
                            .collect(Collectors.toList());
                    userLeaseOrders.addAll(userLeaseOrder);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userLeaseOrders.stream().forEach(map->{
                    OrderVo orderVo = new OrderVo();
                    orderVo.setOrderNumber(map.getOrderNumber());
                    BigDecimal decimal = map.getNetAmount();
                    orderVo.setNetAmount(decimal);
                    orderVo.setDividendRatio(map.getHospitalProportion());
                    orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getHospitalProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                    orderVos.add(orderVo);
                });
                totalVo.setEffectiveOrder(orderVos.size());
                BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                totalVo.setOrderAmount(orderAmount);
                totalVo.setDividendAmount(dividendAmount);
                totalVo.setOrderVos(orderVos);
            } else if (statistics == 2) {
                collect.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectRevenueStatistics(deviceNumberList, map);
                    userLeaseOrderList.addAll(userLeaseOrders);
                });
                Date dNow = new Date();   //当前时间
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
                String format = sdf.format(dNow);
                List<UserLeaseOrder> userLeaseOrders = new ArrayList<>();
                try {
                    //使用SimpleDateFormat的parse()方法生成Date
                    Date date = sdf.parse(format);
                    List<UserLeaseOrder> userLeaseOrder = userLeaseOrderList.stream()
                            .filter(s->s.getLeaseTime().getTime()>=date.getTime())
                            .collect(Collectors.toList());
                    userLeaseOrders.addAll(userLeaseOrder);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userLeaseOrders.stream().forEach(map->{
                    OrderVo orderVo = new OrderVo();
                    orderVo.setOrderNumber(map.getOrderNumber());
                    BigDecimal decimal = map.getNetAmount();
                    orderVo.setNetAmount(decimal);
                    orderVo.setDividendRatio(map.getHospitalProportion());
                    orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getHospitalProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                    orderVos.add(orderVo);
                });
                totalVo.setEffectiveOrder(orderVos.size());
                BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                totalVo.setOrderAmount(orderAmount);
                totalVo.setDividendAmount(dividendAmount);
                totalVo.setOrderVos(orderVos);
            } else if (statistics == 3) {
                collect.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectRevenueStatistics(deviceNumberList, map);
                    userLeaseOrderList.addAll(userLeaseOrders);
                });
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // 获取前月的第一天
                Calendar cale = Calendar.getInstance();
                cale.add(Calendar.MONTH, 0);
                cale.set(Calendar.DAY_OF_MONTH, 1);
                String firstDay = format.format(cale.getTime());
                // 获取前月的最后一天
                cale = Calendar.getInstance();
                cale.add(Calendar.MONTH, 1);
                cale.set(Calendar.DAY_OF_MONTH, 0);
                String lastDay = format.format(cale.getTime());
                List<UserLeaseOrder> userLeaseOrders = new ArrayList<>();
                try {
                    //使用SimpleDateFormat的parse()方法生成Date
                    Date first = format.parse(firstDay);
                    Date last = format.parse(lastDay);
                    List<UserLeaseOrder> userLeaseOrder = userLeaseOrderList.stream()
                            .filter(s->s.getLeaseTime().getTime()>=first.getTime())
                            .filter(s->s.getLeaseTime().getTime()<=last.getTime())
                            .collect(Collectors.toList());
                    userLeaseOrders.addAll(userLeaseOrder);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userLeaseOrders.stream().forEach(map->{
                    OrderVo orderVo = new OrderVo();
                    orderVo.setOrderNumber(map.getOrderNumber());
                    BigDecimal decimal = map.getNetAmount();
                    orderVo.setNetAmount(decimal);
                    orderVo.setDividendRatio(map.getHospitalProportion());
                    orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getHospitalProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                    orderVos.add(orderVo);
                });
                totalVo.setEffectiveOrder(orderVos.size());
                BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                totalVo.setOrderAmount(orderAmount);
                totalVo.setDividendAmount(dividendAmount);
                totalVo.setOrderVos(orderVos);
            } else {
                collect.stream().forEach(map->{
                    List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectRevenueStatistics(deviceNumberList, map);
                    userLeaseOrderList.addAll(userLeaseOrders);
                });
                userLeaseOrderList.stream().forEach(map->{
                    OrderVo orderVo = new OrderVo();
                    orderVo.setOrderNumber(map.getOrderNumber());
                    BigDecimal decimal = map.getNetAmount();
                    orderVo.setNetAmount(decimal);
                    orderVo.setDividendRatio(map.getHospitalProportion());
                    orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getHospitalProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
                    orderVos.add(orderVo);
                });
                totalVo.setEffectiveOrder(orderVos.size());
                BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                totalVo.setOrderAmount(orderAmount);
                totalVo.setDividendAmount(dividendAmount);
                totalVo.setOrderVos(orderVos);
            }
        }
        return totalVo;
    }

    @Override
    public List<DeviceType> selectDeviceType(Long userId) {
        List<DeviceType> deviceTypes = new ArrayList<>();
        List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(userId);
        deviceList.stream().forEach(map->{
            DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map.getDeviceTypeId());
            deviceTypes.add(deviceType);
        });
        List<DeviceType> collect = deviceTypes.stream().distinct().collect(Collectors.toList());
        return collect;
    }
}
