package com.yuepei.investor.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.investor.domain.vo.InvestorDeviceManageVo;
import com.yuepei.investor.domain.vo.InvestorHospitalVO;
import com.yuepei.investor.domain.vo.InvestorOrderVO;
import com.yuepei.investor.domain.vo.InvestorRevenueVO;
import com.yuepei.investor.mapper.AppletInvestorMapper;
import com.yuepei.investor.service.AppletInvestorService;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.*;
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

    @Autowired
    private SysUserFeedbackMapper sysUserFeedbackMapper;

    /**
     * 首页
     *
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
        int deviceCount = appletInvestorMapper.selectDeviceCountByInvestorId(user.getUserId(), investorHospitalVO.getHospitalId());
        //设备总收益
        BigDecimal deviceEarning = appletInvestorMapper.selectDeviceEarningsByInvestorId(user.getUserId(), investorHospitalVO.getHospitalId());
        map.put("hospitalList", hospitalList);
        map.put("deviceCount", deviceCount);
        map.put("deviceEarning", deviceEarning);
        return map;
    }

    /**
     * 昨日营收
     *
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> yesterdayRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.yesterdayRevenue(user.getUserId(), hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.yesterdayRevenueList(user.getUserId(), hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    /**
     * 今日营收
     *
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> todayRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.todayRevenue(user.getUserId(), hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.todayRevenueList(user.getUserId(), hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    /**
     * 本月营收
     *
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> monthRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.monthRevenue(user.getUserId(), hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.monthRevenueList(user.getUserId(), hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    /**
     * 累计营收
     *
     * @param user
     * @param hospitalId
     * @return
     */
    @Override
    public Map<String, Object> accumulativeRevenue(SysUser user, Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        InvestorRevenueVO investorRevenueVO = appletInvestorMapper.accumulativeRevenue(user.getUserId(), hospitalId);
        List<InvestorOrderVO> investorOrderVOList = appletInvestorMapper.accumulativeRevenueList(user.getUserId(), hospitalId);
        investorRevenueVO.setInvestorOrderVOList(investorOrderVOList);
        map.put("revenueVO", investorRevenueVO);
        return map;
    }

    @Override
    public List<HospitalVO> selectHospitalName(Long userId) {
        List<HospitalVO> hospitalVOS = new ArrayList<>();
        List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(userId);
        deviceList.stream().forEach(map -> {
            HospitalVO hospitalVO = new HospitalVO();
            if (map.getHospitalId() != 0) {
                Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
                hospitalVO.setHospitalId(map.getHospitalId());
                hospitalVO.setHospitalName(hospital.getHospitalName());
                hospitalVOS.add(hospitalVO);
            }
        });
        List<HospitalVO> collect = hospitalVOS.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public IndexVo indexPage(Long userId, Long hospitalId) {
        IndexVo indexVo = new IndexVo();
        List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(userId);
        if (hospitalId != null) {
            List<Device> collect = deviceList.stream().filter(map -> map.getHospitalId().equals(hospitalId)).collect(Collectors.toList());
            deviceList.clear();
            deviceList.addAll(collect);
        }
        List<UserLeaseOrderVo> userLeaseOrderVos = new ArrayList<>();
        List<String> deviceNumbers = new ArrayList<>();
        deviceList.stream().forEach(map -> {
            deviceNumbers.add(map.getDeviceNumber());
        });
        if (deviceNumbers.size() != 0) {
            List<UserLeaseOrder> userLeaseOrder = appletInvestorMapper.selectUserLeaseOrderByDevices(deviceNumbers, String.valueOf(userId));
            userLeaseOrder.stream().forEach(i -> {
                UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
                BeanUtils.copyProperties(i, userLeaseOrderVo);
                userLeaseOrderVos.add(userLeaseOrderVo);
            });
        }
        BigDecimal decimal = userLeaseOrderVos.stream().map(UserLeaseOrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        indexVo.setDeviceAmount(decimal);
        indexVo.setSum(deviceList.size());
        return indexVo;
    }

    @Override
    public TotalVo selectRevenueStatistics(Long userId, int statistics) {
        TotalVo totalVo = new TotalVo();
        List<OrderVo> orderVos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (statistics == 1) {
//            List<UserLeaseOrder> userLeaseOrderList = userLeaseOrderMapper.selectUserLeaseOrderByInvestorId(String.valueOf(userId));
            List<OrderProportionDetailVo> orders = appletInvestorMapper.selectOrderProtionDetail(userId);
            Date dNow = new Date();   //当前时间
            Date dBefore = new Date();
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(dNow);//把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
            dBefore = calendar.getTime();   //得到前一天的时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            String defaultStartDate = sdf.format(dBefore);    //格式化前一天
            String now = sdf.format(dNow);
            List<OrderProportionDetailVo> userLeaseOrders = new ArrayList<>();
            try {
                //使用SimpleDateFormat的parse()方法生成Date
                Date date = sdf.parse(defaultStartDate);
                Date parse = sdf.parse(now);
                List<OrderProportionDetailVo> userLeaseOrder = orders.stream()
                        .filter(s -> s.getLeaseTime().getTime() < parse.getTime())
                        .filter(s -> s.getLeaseTime().getTime() > date.getTime())
                        .collect(Collectors.toList());
                userLeaseOrders.addAll(userLeaseOrder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userLeaseOrders.stream().forEach(map -> {
                Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(map.getHospitalId()));
                OrderVo orderVo = new OrderVo();
                orderVo.setLeaseTime(dateFormat.format(map.getLeaseTime()));
                orderVo.setHospitalName(hospital.getHospitalName());
                orderVo.setOrderNumber(map.getOrderNumber());
                BigDecimal decimal = map.getNetAmount();
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(Long.valueOf(map.getProportion()));
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getProportion())).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else if (statistics == 2) {
            List<OrderProportionDetailVo> orders = appletInvestorMapper.selectOrderProtionDetail(userId);
            Date dNow = new Date();   //当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            String format = sdf.format(dNow);
            List<OrderProportionDetailVo> userLeaseOrders = new ArrayList<>();
            try {
                //使用SimpleDateFormat的parse()方法生成Date
                Date date = sdf.parse(format);
                List<OrderProportionDetailVo> userLeaseOrder = orders.stream()
                        .filter(s -> s.getLeaseTime().getTime() >= date.getTime())
                        .collect(Collectors.toList());
                userLeaseOrders.addAll(userLeaseOrder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userLeaseOrders.stream().forEach(map -> {
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderNumber(map.getOrderNumber());
                BigDecimal decimal = map.getNetAmount();
                orderVo.setLeaseTime(dateFormat.format(map.getLeaseTime()));
                Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(map.getHospitalId()));
                orderVo.setHospitalName(hospital.getHospitalName());
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(Long.valueOf(map.getProportion()));
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getProportion())).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else if (statistics == 3) {
            List<OrderProportionDetailVo> orders = appletInvestorMapper.selectOrderProtionDetail(userId);
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
            List<OrderProportionDetailVo> userLeaseOrders = new ArrayList<>();
            try {
                //使用SimpleDateFormat的parse()方法生成Date
                Date first = format.parse(firstDay);
                Date last = format.parse(lastDay);
                List<OrderProportionDetailVo> userLeaseOrder = orders.stream()
                        .filter(s -> s.getLeaseTime().getTime() >= first.getTime())
                        .filter(s -> s.getLeaseTime().getTime() <= last.getTime())
                        .collect(Collectors.toList());
                userLeaseOrders.addAll(userLeaseOrder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userLeaseOrders.stream().forEach(map -> {
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderNumber(map.getOrderNumber());
                BigDecimal decimal = map.getNetAmount();
                orderVo.setLeaseTime(dateFormat.format(map.getLeaseTime()));
                Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(map.getHospitalId()));
                orderVo.setHospitalName(hospital.getHospitalName());
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(Long.valueOf(map.getProportion()));
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getProportion())).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        } else {
            List<OrderProportionDetailVo> orders = appletInvestorMapper.selectOrderProtionDetail(userId);
            orders.stream().forEach(map -> {
                OrderVo orderVo = new OrderVo();
                orderVo.setOrderNumber(map.getOrderNumber());
                BigDecimal decimal = map.getNetAmount();
                orderVo.setLeaseTime(dateFormat.format(map.getLeaseTime()));
                Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(map.getHospitalId()));
                orderVo.setHospitalName(hospital.getHospitalName());
                orderVo.setNetAmount(decimal);
                orderVo.setDividendRatio(Long.valueOf(map.getProportion()));
                orderVo.setIncomeAmount(decimal.multiply(new BigDecimal(map.getProportion())).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                orderVos.add(orderVo);
            });
            totalVo.setEffectiveOrder(orderVos.size());
            BigDecimal orderAmount = orderVos.stream().map(OrderVo::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dividendAmount = orderVos.stream().map(OrderVo::getIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalVo.setOrderAmount(orderAmount);
            totalVo.setDividendAmount(dividendAmount);
            totalVo.setOrderVos(orderVos);
        }
        return totalVo;
    }

    @Override
    public List<List<String>> selectDepartment(Long userId) {
        List<List<String>> lists = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(sysUser.getUserId());
        List<String> deviceDepartment = new ArrayList<>();
        List<String> deviceType = new ArrayList<>();
        deviceList.stream().forEach(map -> {
            String device_full_address = map.getDeviceFullAddress();
            if (!device_full_address.equals("0")) {
                String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                DeviceType typeName = hospitalDeviceMapper.selectDeviceByTypeName(map.getDeviceTypeId());
                deviceDepartment.add(Department.getHospitalName());
                deviceType.add(typeName.getDeviceTypeName());
            }
        });
        List<String> collect = deviceDepartment.stream().distinct().collect(Collectors.toList());
        List<String> collect1 = deviceType.stream().distinct().collect(Collectors.toList());
        lists.add(collect);
        lists.add(collect1);
        return lists;
    }

    @Override
    public List<DeviceType> selectDeviceType(Long userId) {
        List<DeviceType> deviceTypes = new ArrayList<>();
        List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(userId);
        deviceList.stream().forEach(map -> {
            DeviceType deviceType = deviceTypeMapper.selectDeviceTypeByDeviceTypeId(map.getDeviceTypeId());
            deviceTypes.add(deviceType);
        });
        List<DeviceType> collect = deviceTypes.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<HospitalVO> selectDeviceTypeByHospital(Long userId, Long deviceTypeId) {
        List<HospitalVO> hospitalVOS = new ArrayList<>();
        if (deviceTypeId == null) {
            List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(userId);
            List<Long> hospitalIds = new ArrayList<>();
            deviceList.stream().forEach(map -> {
                hospitalIds.add(map.getHospitalId());
            });
            List<Long> hospitalIdList = hospitalIds.stream().distinct().collect(Collectors.toList());
            List<Hospital> hospitals = hospitalDeviceMapper.selectHospitalByHospitalIds(hospitalIdList);
            hospitals.stream().forEach(map -> {
                HospitalVO hospitalVO = new HospitalVO();
                BeanUtils.copyProperties(map, hospitalVO);
                hospitalVO.setDepartmentList(selectDeviceTypeByDepartment(map.getHospitalId()));
                hospitalVOS.add(hospitalVO);
            });
            return hospitalVOS;
        } else {
            List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(userId);
            List<Device> collect = deviceList.stream().filter(map -> map.getDeviceTypeId() == deviceTypeId).collect(Collectors.toList());
            List<Long> hospitalIds = new ArrayList<>();
            collect.stream().forEach(map -> {
                hospitalIds.add(map.getHospitalId());
            });
            List<Long> hospitalIdList = hospitalIds.stream().distinct().collect(Collectors.toList());
            List<Hospital> hospitals = hospitalDeviceMapper.selectHospitalByHospitalIds(hospitalIdList);
            hospitals.stream().forEach(map -> {
                HospitalVO hospitalVO = new HospitalVO();
                BeanUtils.copyProperties(map, hospitalVO);
                hospitalVO.setDepartmentList(selectDeviceTypeByDepartment(map.getHospitalId()));
                hospitalVOS.add(hospitalVO);
            });
            return hospitalVOS;
        }
    }

    public List<String> selectDeviceTypeByDepartment(Long hospitalId) {
        List<String> departments = new ArrayList<>();
        List<Hospital> hospitals = hospitalDeviceMapper.selectHospitalByParentId(hospitalId);
        List<Hospital> hospitalList = new ArrayList<>();
        hospitals.stream().forEach(map -> {
            List<Hospital> hospital = hospitalDeviceMapper.selectHospitalByParentId(map.getHospitalId());
            hospitalList.addAll(hospital);
        });
        hospitalList.stream().forEach(map -> {
            departments.add(map.getHospitalName());
        });
        List<String> collect = departments.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public InvestorDeviceManageVo investorDeviceManage(Long userId, Long hospitalId, String departmentName, Long utilizationRate, Long deviceTypeId) {
        InvestorDeviceManageVo deviceManageVo = new InvestorDeviceManageVo();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<Device> device = hospitalDeviceMapper.selectInvestorId(userId);
        List<DeviceDetailsVo> deviceDetailsVos = new ArrayList<>();
        device.stream().forEach(map -> {
            DeviceDetailsVo detailsVo = new DeviceDetailsVo();
            String device_full_address = map.getDeviceFullAddress();
            if (!map.getDeviceFullAddress().equals("0")) {
                String[] array = JSON.parseArray(device_full_address).toArray(new String[0]);
                Hospital Department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                detailsVo.setDeviceDepartment(Department.getHospitalId());
                detailsVo.setDeviceDepartmentName(Department.getHospitalName());
            }
            Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(map.getHospitalId());
            if (hospital != null) {
                detailsVo.setHospitalId(hospital.getHospitalId());
                detailsVo.setHospitalName(hospital.getHospitalName());
            }
            detailsVo.setDeviceNumber(map.getDeviceNumber());
            detailsVo.setAgentId(map.getUserId());
            detailsVo.setAgentName(sysUser.getNickName());
            detailsVo.setStatus(map.getStatus());
            detailsVo.setDeviceTypeId(map.getDeviceTypeId());
            deviceDetailsVos.add(detailsVo);
        });
        if (deviceTypeId != null) {
            List<DeviceDetailsVo> collect = deviceDetailsVos.stream()
                    .filter(map -> map.getDeviceTypeId() != null)
                    .filter(map -> map.getDeviceTypeId().equals(deviceTypeId)).collect(Collectors.toList());
            deviceDetailsVos.clear();
            deviceDetailsVos.addAll(collect);

        }
        if (hospitalId != null) {
            List<DeviceDetailsVo> collect = deviceDetailsVos.stream()
                    .filter(map -> map.getHospitalId() != null)
                    .filter(map -> map.getHospitalId().equals(hospitalId)).collect(Collectors.toList());
            deviceDetailsVos.clear();
            deviceDetailsVos.addAll(collect);

        }
        if (!departmentName.equals("")) {
            List<DeviceDetailsVo> collect = deviceDetailsVos.stream()
                    .filter(map -> map.getDeviceDepartmentName() != null)
                    .filter(map -> map.getDeviceDepartmentName().equals(departmentName)).collect(Collectors.toList());
            deviceDetailsVos.clear();
            deviceDetailsVos.addAll(collect);
        }
        List<String> deviceNumbers = new ArrayList<>();
        deviceDetailsVos.stream().forEach(map -> {
            deviceNumbers.add(map.getDeviceNumber());
        });
        List<BigDecimal> decimals = new ArrayList<>();
        if (deviceNumbers.size() != 0) {
            List<UserLeaseOrder> userLeaseOrderList = appletInvestorMapper.selectUserLeaseOrderByDevices(deviceNumbers, String.valueOf(userId));
            userLeaseOrderList.stream().forEach(map -> {
                decimals.add(map.getNetAmount());
            });
        }
        BigDecimal decimal = BigDecimal.ZERO;
        for (BigDecimal price : decimals) {
            decimal = decimal.add(price);
        }
//        deviceManageVo.setDeviceAmount(decimal.multiply(new BigDecimal(sysUser.getProportion())).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
        deviceManageVo.setDeviceAmount(decimal);
        deviceManageVo.setDeviceSum(deviceDetailsVos.size());
        deviceManageVo.setUtilizationRate(0L);
        deviceManageVo.setDeviceDetails(deviceDetailsVos);
        return deviceManageVo;
    }

    @Override
    public PersonalCenterVo investorPersonalCenter(Long userId) {
        PersonalCenterVo personalCenterVo = new PersonalCenterVo();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        personalCenterVo.setAmount(new BigDecimal(String.valueOf(sysUser.getBalance())));
        personalCenterVo.setUserName(sysUser.getUserName());
        personalCenterVo.setAvatar(sysUser.getAvatar());
        return personalCenterVo;
    }

    @Override
    public SysUser investorPersonalData(Long userId) {
        return sysUserMapper.selectUserById(userId);
    }

    @Override
    public Long selectProportion(Long userId) {
        List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
        Long proportion = 50L;
        if (sysUsers.size() == 0) {
            return proportion;
        } else {
            for (SysUser sysUser : sysUsers) {
                proportion = proportion - sysUser.getProportion();
            }
            return proportion;
        }
    }

    @Override
    public List<SubAccountManageVo> investorSubAccount(Long userId) {
        List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
        if (sysUsers.isEmpty()) {
            return null;
        }
        List<SubAccountManageVo> subAccountManageVoList = new ArrayList<>();
        sysUsers.stream().forEach(map -> {
            SubAccountManageVo subAccountManageVo = new SubAccountManageVo();
            List<Device> deviceList = hospitalDeviceMapper.selectInvestorId(map.getUserId());
            List<Hospital> hospitals = new ArrayList<>();
            deviceList.stream().forEach(i -> {
                Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(i.getHospitalId());
                hospitals.add(hospital);
            });
            List<Hospital> collect = hospitals.stream().distinct().collect(Collectors.toList());
            subAccountManageVo.setProportion(map.getProportion());
            subAccountManageVo.setInvestorName(map.getNickName());
            subAccountManageVo.setHospitalSum(collect.size());
            subAccountManageVo.setDeviceSum(deviceList.size());
            subAccountManageVoList.add(subAccountManageVo);
        });
        return subAccountManageVoList;
    }

    @Override
    public int investorUploadsFile(FeedbackInfoVo feedbackInfoVo) {
        SysUser sysUser = sysUserMapper.selectUserById(feedbackInfoVo.getUserId());
        SysUserFeedback feedback = new SysUserFeedback();
        feedback.setUserId(sysUser.getUserId());
        feedback.setUserName(sysUser.getUserName());
        feedback.setPhoneNumber(sysUser.getPhoneNumber());
        String urls = JSON.toJSONString(feedbackInfoVo.getFeedbackUrl());
        List<List<String>> lists = JSON.parseObject(urls, new TypeReference<List<List<String>>>() {
        });
        String string = JSON.toJSONString(lists);
        feedback.setFeedbackUrl(string);
        feedback.setFeedbackTime(new Date());
        feedback.setFeedbackType("3");
        feedback.setStatus(0);
        return sysUserFeedbackMapper.insertSysUserFeedback(feedback);
    }

    @Override
    public List<FeedbackInfoVo> investorUploadsFileList(Long userId) {
        List<FeedbackInfoVo> infoVoList = new ArrayList<>();
        List<SysUserFeedback> userFeedbackList = sysUserFeedbackMapper.selectSysUserFeedbackByUserId(userId);
        userFeedbackList.stream().forEach(map -> {
            FeedbackInfoVo feedbackInfoVo = new FeedbackInfoVo();
            BeanUtils.copyProperties(map, feedbackInfoVo);
            List<List<String>> lists = JSON.parseObject(map.getFeedbackUrl(), new TypeReference<List<List<String>>>() {
            });
            List<String> arrayList = new ArrayList<>();
            for (List<String> list : lists) {
                for (String s : list) {
                    arrayList.add(s);
                }
            }
            feedbackInfoVo.setFeedbackUrl(arrayList);
            infoVoList.add(feedbackInfoVo);
        });
        return infoVoList;
    }

    @Override
    public FeedbackInfoVo investorUploadsFileListDetails(Long feedbackId) {
        SysUserFeedback sysUserFeedback = sysUserFeedbackMapper.selectSysUserFeedbackById(feedbackId);
        FeedbackInfoVo feedbackInfoVo = new FeedbackInfoVo();
        BeanUtils.copyProperties(sysUserFeedback, feedbackInfoVo);
        List<List<String>> lists = JSON.parseObject(sysUserFeedback.getFeedbackUrl(), new TypeReference<List<List<String>>>() {
        });
        List<String> arrayList = new ArrayList<>();
        for (List<String> list : lists) {
            for (String s : list) {
                arrayList.add(s);
            }
        }
        feedbackInfoVo.setFeedbackUrl(arrayList);
        return feedbackInfoVo;
    }

    public List<UserLeaseOrderVo> selectLeaseOrderList(Long userId, String deviceDepartment, String deviceTypeName, String nameOrNumber) {
        List<UserLeaseOrderVo> userLeaseOrderVoList = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<UserLeaseOrder> leaseOrders = new ArrayList<>();
        if (!nameOrNumber.equals("")) {
            List<UserLeaseOrder> userLeaseOrders = appletInvestorMapper.selectUserLeaseOrderByOrderNumber(nameOrNumber, String.valueOf(sysUser.getUserId()));
            leaseOrders.addAll(userLeaseOrders);
        } else {
            List<UserLeaseOrder> userLeaseOrders = userLeaseOrderMapper.selectUserLeaseOrderByInvestor(String.valueOf(sysUser.getUserId()));
            leaseOrders.addAll(userLeaseOrders);
        }
        List<UserLeaseOrderVo> userLeaseOrderList = leaseOrders.stream().map(a -> {
            UserLeaseOrderVo b = new UserLeaseOrderVo();
            BeanUtils.copyProperties(a, b);
            return b;
        }).collect(Collectors.toList());
        if (leaseOrders == null) {
            return userLeaseOrderList;
        }
        userLeaseOrderList.stream().forEach(map -> {
            Device device = hospitalDeviceMapper.selectDeviceByTypeNumber(map.getDeviceNumber());
            String deviceFullAddress = device.getDeviceFullAddress();
            if (!deviceFullAddress.equals("0")) {
                String[] array = JSON.parseArray(deviceFullAddress).toArray(new String[0]);
                Hospital department = hospitalDeviceMapper.selectHospitalByHospitalName(Long.valueOf(array[1]));
                map.setDepartment(department.getHospitalName());
            }
        });
        if (!deviceDepartment.equals("")) {
            List<UserLeaseOrderVo> collect = userLeaseOrderList.stream().filter(map -> map.getDepartment().equals(deviceDepartment)).collect(Collectors.toList());
            userLeaseOrderList.clear();
            userLeaseOrderList.addAll(collect);
        }
        if (!deviceTypeName.equals("")) {
            List<UserLeaseOrderVo> collect = userLeaseOrderList.stream().filter(map -> map.getDeviceType().equals(deviceTypeName)).collect(Collectors.toList());
            userLeaseOrderList.clear();
            userLeaseOrderList.addAll(collect);
        }
        userLeaseOrderVoList.addAll(userLeaseOrderList);
        List<UserLeaseOrderVo> collect = userLeaseOrderVoList.stream().filter(map -> !map.getStatus().equals("3")).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<UserLeaseOrderVo> investorLeaseOrder(Long userId, String status, String deviceDepartment, String deviceTypeName, String nameOrNumber) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<UserLeaseOrderVo> userLeaseOrderVoList = new ArrayList<>();
        if (sysUser.getParentId() != 0) {
            List<UserLeaseOrderVo> userLeaseOrderVos = selectLeaseOrderList(userId, deviceDepartment, deviceTypeName, nameOrNumber);
            userLeaseOrderVoList.addAll(userLeaseOrderVos);
        } else {
            List<SysUser> sysUsers = sysUserMapper.selectUserByParentId(userId);
            sysUsers.add(sysUser);
            sysUsers.stream().forEach(map -> {
                List<UserLeaseOrderVo> userLeaseOrderVos = selectLeaseOrderList(map.getUserId(), deviceDepartment, deviceTypeName, nameOrNumber);
                if (userLeaseOrderVos.size() != 0) {
                    userLeaseOrderVoList.addAll(userLeaseOrderVos);
                }
            });
        }
        if (status.equals("")) {
            List<UserLeaseOrderVo> collect = userLeaseOrderVoList.stream().sorted(Comparator.comparing(UserLeaseOrderVo::getLeaseTime).reversed()).collect(Collectors.toList());
            return collect;
        } else {
            List<UserLeaseOrderVo> collect = userLeaseOrderVoList.stream()
                    .filter(map -> map.getStatus().equals(status))
                    .sorted(Comparator.comparing(UserLeaseOrderVo::getLeaseTime).reversed()).collect(Collectors.toList());
            return collect;
        }
    }

    @Override
    public UserLeaseOrderVo investorLeaseOrderDetails(String orderNumber, Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        UserLeaseOrder userLeaseOrder = userLeaseOrderMapper.selectLeaseOrderDetails(orderNumber);
        Device device = hospitalDeviceMapper.selectDeviceByTypeNumber(userLeaseOrder.getDeviceNumber());
        //获取医院信息
        Hospital hospital = hospitalDeviceMapper.selectHospitalByHospitalName(device.getHospitalId());
        //获取代理商信息
        UserLeaseOrderVo userLeaseOrderVo = new UserLeaseOrderVo();
        BeanUtils.copyProperties(userLeaseOrder, userLeaseOrderVo);
        userLeaseOrderVo.setUserName(sysUser.getUserName());
        userLeaseOrderVo.setAgentName(sysUser.getNickName());
        if (hospital != null) {
            userLeaseOrderVo.setHospitalName(hospital.getHospitalName());
        }
        if (userLeaseOrder.getStatus().equals("0")) {
            Long date = new Date().getTime();
            Long lease = userLeaseOrderVo.getLeaseTime().getTime();
            //已使用时长、使用时长
            Long time = new Date(date - lease).getTime();
            Long day = time / 1000 / 60 / 60 / 24;
            Long hour = time / 1000 / 60 / 60 % 24;
            Long minute = time / 1000 / 60 % 60;
            Long second = time / 1000 % 60;
            if (day == 0) {
                if (hour == 0) {
                    if (minute == 0) {
                        userLeaseOrderVo.setPlayTime(second + "秒");
                    } else {
                        userLeaseOrderVo.setPlayTime(minute + "分钟" + second + "秒");
                    }
                } else {
                    userLeaseOrderVo.setPlayTime(hour + "小时" + minute + "分钟" + second + "秒");
                }
            } else {
                userLeaseOrderVo.setPlayTime(day + "天" + hour + "小时" + minute + "分钟" + second + "秒");
            }
            JSONArray deviceRule = JSON.parseArray(userLeaseOrder.getDeviceRule());
            for (int i = 0; i < deviceRule.size(); i++) {
                JSONObject jsonObject = deviceRule.getJSONObject(i);
                Integer time1 = (Integer) jsonObject.get("time");
                String startTime = (String) jsonObject.get("startTime");
                String endTime = (String) jsonObject.get("endTime");
                if (time1 == 0) {
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setContent(bigDecimal + device.getContent());
                } else if (time1 == 1) {
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setEstimateAmount("(" + startTime + "~" + endTime + ")  " + bigDecimal);
                }
            }
            userLeaseOrderVo.setDepositNum(new BigDecimal(userLeaseOrder.getDeposit()));
            userLeaseOrderVo.setLeaseTime(userLeaseOrder.getLeaseTime());
            userLeaseOrderVo.setLeaseAddress(userLeaseOrder.getLeaseAddress());
            userLeaseOrderVo.setOrderNumber(userLeaseOrder.getOrderNumber());
        } else if (userLeaseOrder.getStatus().equals("1")) {
            Long time = Long.valueOf(userLeaseOrderVo.getPlayTime());
            Long day = time / 1000 / 60 / 60 / 24;
            Long hour = time / 1000 / 60 / 60 % 24;
            Long minute = time / 1000 / 60 % 60;
            Long second = time / 1000 % 60;
            if (day == 0) {
                if (hour == 0) {
                    if (minute == 0) {
                        userLeaseOrderVo.setPlayTime(second + "秒");
                    } else {
                        userLeaseOrderVo.setPlayTime(minute + "分钟" + second + "秒");
                    }
                } else {
                    userLeaseOrderVo.setPlayTime(hour + "小时" + minute + "分钟" + second + "秒");
                }
            } else {
                userLeaseOrderVo.setPlayTime(day + "天" + hour + "小时" + minute + "分钟" + second + "秒");
            }
            userLeaseOrderVo.setPrice(userLeaseOrder.getPrice());
            JSONArray deviceRule = JSON.parseArray(userLeaseOrder.getDeviceRule());
            for (int i = 0; i < deviceRule.size(); i++) {
                JSONObject jsonObject = deviceRule.getJSONObject(i);
                Integer time1 = (Integer) jsonObject.get("time");
                if (time1 == 0) {
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setContent(bigDecimal + device.getContent());
                } else {
                    String start = String.valueOf(jsonObject.get("startTime"));
                    String end = String.valueOf(jsonObject.get("endTime"));
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setEstimateAmount("(" + start + "~" + end + ")  " + bigDecimal);
                }
            }
            userLeaseOrderVo.setDepositNum(new BigDecimal(userLeaseOrder.getDeposit()));
            userLeaseOrderVo.setCouponPrice(userLeaseOrder.getCouponPrice());
            userLeaseOrderVo.setLeaseTime(userLeaseOrder.getLeaseTime());
            userLeaseOrderVo.setLeaseAddress(userLeaseOrder.getLeaseAddress());
            userLeaseOrderVo.setRestoreTime(userLeaseOrder.getRestoreTime());
            userLeaseOrderVo.setRestoreAddress(userLeaseOrder.getRestoreAddress());
            userLeaseOrderVo.setOrderNumber(userLeaseOrder.getOrderNumber());
            userLeaseOrderVo.setStatus(userLeaseOrder.getStatus());
            userLeaseOrderVo.setNetAmount(userLeaseOrder.getNetAmount());
        } else {
            Long time = Long.valueOf(userLeaseOrderVo.getPlayTime());
            Long day = time / 1000 / 60 / 60 / 24;
            Long hour = time / 1000 / 60 / 60 % 24;
            Long minute = time / 1000 / 60 % 60;
            Long second = time / 1000 % 60;
            if (day == 0) {
                if (hour == 0) {
                    if (minute == 0) {
                        userLeaseOrderVo.setPlayTime(second + "秒");
                    } else {
                        userLeaseOrderVo.setPlayTime(minute + "分钟" + second + "秒");
                    }
                } else {
                    userLeaseOrderVo.setPlayTime(hour + "小时" + minute + "分钟" + second + "秒");
                }
            } else {
                userLeaseOrderVo.setPlayTime(day + "天" + hour + "小时" + minute + "分钟" + second + "秒");
            }
            userLeaseOrderVo.setPrice(userLeaseOrder.getPrice());
            JSONArray deviceRule = JSON.parseArray(userLeaseOrder.getDeviceRule());
            for (int i = 0; i < deviceRule.size(); i++) {
                JSONObject jsonObject = deviceRule.getJSONObject(i);
                Integer time1 = (Integer) jsonObject.get("time");
                if (time1 == 0) {
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setContent(bigDecimal + device.getContent());
                } else {
                    String start = String.valueOf(jsonObject.get("startTime"));
                    String end = String.valueOf(jsonObject.get("endTime"));
                    Object price = jsonObject.get("price");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(price));
                    userLeaseOrderVo.setEstimateAmount("(" + start + "~" + end + ")  " + bigDecimal);
                }
            }
            userLeaseOrderVo.setDepositNum(new BigDecimal(userLeaseOrder.getDeposit()));
            userLeaseOrderVo.setCouponPrice(userLeaseOrder.getCouponPrice());
            userLeaseOrderVo.setLeaseTime(userLeaseOrder.getLeaseTime());
            userLeaseOrderVo.setLeaseAddress(userLeaseOrder.getLeaseAddress());
            userLeaseOrderVo.setRestoreTime(userLeaseOrder.getRestoreTime());
            userLeaseOrderVo.setRestoreAddress(userLeaseOrder.getRestoreAddress());
            userLeaseOrderVo.setOrderNumber(userLeaseOrder.getOrderNumber());
            userLeaseOrderVo.setPayType(userLeaseOrder.getPayType());
            userLeaseOrderVo.setStatus(userLeaseOrder.getStatus());
            userLeaseOrderVo.setNetAmount(userLeaseOrder.getNetAmount());
        }
        return userLeaseOrderVo;
    }

    @Override
    public String bindingBank(Long userId, Bank bank) {
        bank.setUserId(userId);
        if (checkBankCard(bank.getBankNumber())){
            appletInvestorMapper.bindingBank(bank);
            return "银行卡绑定成功";
        }else {
            return "请重新输入银行卡号";
        }
    }

    /**
    校验过程：
    1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
    2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
    3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
    */
    public boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    public char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}
