package com.yuepei.system.service.impl;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.Bank;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.Withdrawal;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.FinanceServiceMapper;
import com.yuepei.system.mapper.HospitalDeviceMapper;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zzy
 * @date 2023/4/21 10:00
 */
@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private FinanceServiceMapper financeServiceMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private HospitalDeviceMapper hospitalDeviceMapper;

    @Override
    public List<RevenueStatisticsDetailsVo> investorIncomeStatistics(Long investorId, String startTime, String endTime) {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = new ArrayList<>();
        List<RevenueStatisticsDetailsVo> revenueStatisticsVos = financeServiceMapper.selectRevenueStatistics();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (investorId!=null){
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getUserId().equals(investorId)).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!startTime.equals("")){
            try {
                Date start = format.parse(startTime);
                List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() >= start.getTime()).collect(Collectors.toList());
                revenueStatisticsVos.clear();
                revenueStatisticsVos.addAll(collect);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!endTime.equals("")){
            try {
                Date end = format.parse(endTime);
                List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() <= end.getTime()).collect(Collectors.toList());
                revenueStatisticsVos.clear();
                revenueStatisticsVos.addAll(collect);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        revenueStatisticsVoList.addAll(revenueStatisticsVos);
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsDetailsVo> agentIncomeStatistics(Long agentId, String startTime, String endTime) {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = new ArrayList<>();
        List<RevenueStatisticsDetailsVo> revenueStatisticsVos = financeServiceMapper.selectAgentIncomeStatistics();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (agentId!=null){
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getUserId().equals(agentId)).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!startTime.equals("")){
            try {
                Date start = format.parse(startTime);
                List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() >= start.getTime()).collect(Collectors.toList());
                revenueStatisticsVos.clear();
                revenueStatisticsVos.addAll(collect);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!endTime.equals("")){
            try {
                Date end = format.parse(endTime);
                List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() <= end.getTime()).collect(Collectors.toList());
                revenueStatisticsVos.clear();
                revenueStatisticsVos.addAll(collect);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        revenueStatisticsVoList.addAll(revenueStatisticsVos);
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsDetailsVo> hospitalIncomeStatistics(Long hospitalId, String startTime, String endTime) {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = new ArrayList<>();
        List<RevenueStatisticsDetailsVo> revenueStatisticsVos = financeServiceMapper.selectHospitalIncomeStatistics();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (hospitalId!=null){
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getUserId().equals(hospitalId)).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!startTime.equals("")){
            try {
                Date start = format.parse(startTime);
                List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() >= start.getTime()).collect(Collectors.toList());
                revenueStatisticsVos.clear();
                revenueStatisticsVos.addAll(collect);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!endTime.equals("")){
            try {
                Date end = format.parse(endTime);
                List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() <= end.getTime()).collect(Collectors.toList());
                revenueStatisticsVos.clear();
                revenueStatisticsVos.addAll(collect);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        revenueStatisticsVoList.addAll(revenueStatisticsVos);
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsDetailsVo> deviceStatistics(String deviceNumber, String startTime, String endTime) {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = new ArrayList<>();
        List<RevenueStatisticsDetailsVo> revenueStatisticsVos = financeServiceMapper.selectDeviceStatistics();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (!deviceNumber.equals("")){
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getDeviceNumber().equals(deviceNumber)).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!startTime.equals("")){
            try {
                Date start = format.parse(startTime);
                List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() >= start.getTime()).collect(Collectors.toList());
                revenueStatisticsVos.clear();
                revenueStatisticsVos.addAll(collect);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!endTime.equals("")){
            try {
                Date end = format.parse(endTime);
                List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() <= end.getTime()).collect(Collectors.toList());
                revenueStatisticsVos.clear();
                revenueStatisticsVos.addAll(collect);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        revenueStatisticsVoList.addAll(revenueStatisticsVos);
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsVo> businessStatistics(Long type, String payType, String agentId, String hospitalId, String deviceNumber, String startTime, String endTime) {
        List<RevenueStatisticsVo> revenueStatisticsVoList = new ArrayList<>();
        if (type==1){
            //按年统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByYear(payType,agentId,hospitalId,deviceNumber,startTime,endTime);
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==2){
            //按月统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByMonth(payType,agentId,hospitalId,deviceNumber,startTime,endTime);
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==3){
            //按天统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByDay(payType,agentId,hospitalId,deviceNumber,startTime,endTime);
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==4){
            //按投资商统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByInvestor(payType,agentId,hospitalId,deviceNumber,startTime,endTime);
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==5){
            //按代理统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByAgent(payType,agentId,hospitalId,deviceNumber,startTime,endTime);
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==6){
            //按代理商医院统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByHospital(payType,agentId,hospitalId,deviceNumber,startTime,endTime);
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==7){
            //按平台医院统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByPlatformHospital(payType,agentId,hospitalId,deviceNumber,startTime,endTime);
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }
        return revenueStatisticsVoList;
    }

    @Override
    public String withdrawalApplication(Withdrawal withdrawal) {
        SysUser sysUser = sysUserMapper.selectUserById(withdrawal.getUserId());
        Bank bank = sysUserMapper.selectBank(withdrawal.getUserId());
        String uuid = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        String orderNumber = uuid.substring(uuid.length() - 16);
        withdrawal.setOrderNumber(orderNumber);
        withdrawal.setRole(sysUser.getUserType());
        withdrawal.setRoleName(sysUser.getNickName());
        withdrawal.setWithdrawalInformation("开户人："+bank.getAccountHolder()+" 名称："+bank.getBankName()+" 银行卡号："+bank.getBankNumber());
        withdrawal.setReceived(1L);
        withdrawal.setApplyTime(new Date());
        withdrawal.setBalance(sysUser.getBalance().subtract(withdrawal.getAmount()));
        financeServiceMapper.insertWithdrawalApplication(withdrawal);
        sysUser.setBalance(sysUser.getBalance().subtract(withdrawal.getAmount()));
        sysUserMapper.updateUser(sysUser);
        return "申请成功";
    }

    @Override
    public String byRejecting(String orderNumber, int type) {
        Withdrawal withdrawal = financeServiceMapper.selectWithdrawalByOrderNumber(orderNumber);
        if (type==1){
            withdrawal.setStatus(3L);
            withdrawal.setHandleTime(new Date());
            financeServiceMapper.updateWithdrawal(withdrawal);
            return "已打款";
        }else {
            SysUser sysUser = sysUserMapper.selectUserById(withdrawal.getUserId());
            sysUser.setBalance(sysUser.getBalance().add(withdrawal.getBalance()));
            sysUserMapper.updateUser(sysUser);
            withdrawal.setStatus(2L);
            withdrawal.setHandleTime(new Date());
            financeServiceMapper.updateWithdrawal(withdrawal);
            return "未通过";
        }
    }

    @Override
    public List<Withdrawal> roleWithdrawalStatistics(String role, Long userId, Long status, String startApplyTime, String endApplyTime, String startHandleTime, String endHandleTime) {
        List<Withdrawal> withdrawalList = new ArrayList<>();
        List<Withdrawal> withdrawals = financeServiceMapper.selectRoleWithdrawalStatistics(userId,status,startApplyTime,endApplyTime,startHandleTime,endHandleTime);
        withdrawalList.addAll(withdrawals);
        if (!role.equals("")){
            List<Withdrawal> collect = withdrawalList.stream().filter(map -> map.getRole().equals(role)).collect(Collectors.toList());
            withdrawalList.clear();
            withdrawalList.addAll(collect);
        }
        return withdrawalList;
    }

    @Override
    public WithdrawalVo withdrawalStatistics(String orderNumber) {
        WithdrawalVo withdrawalVo = new WithdrawalVo();
        List<Withdrawal> withdrawalList = new ArrayList<>();
        List<Withdrawal> withdrawals = financeServiceMapper.selectWithdrawalStatistics();
        withdrawalList.addAll(withdrawals);
        if (!orderNumber.equals("")){
            List<Withdrawal> collect = withdrawalList.stream().filter(map -> map.getOrderNumber().equals(orderNumber)).collect(Collectors.toList());
            withdrawalList.clear();
            withdrawalList.addAll(collect);
        }
        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        dBefore = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前一天
        String now = sdf.format(dNow);
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            Date date = sdf.parse(defaultStartDate);
            Date parse = sdf.parse(now);
            List<Withdrawal> collect = withdrawalList.stream()
                    .filter(s -> s.getHandleTime()!=null)
                    .filter(s -> s.getHandleTime().getTime() < parse.getTime())
                    .filter(s -> s.getHandleTime().getTime() > date.getTime())
                    .collect(Collectors.toList());
            //昨天提现金额
            withdrawalVo.setYesterday(collect.stream().map(Withdrawal::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = sdf.format(dNow);
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            Date date = sdf.parse(format);
            List<Withdrawal> collect = withdrawalList.stream()
                    .filter(s -> s.getHandleTime()!=null)
                    .filter(s -> s.getHandleTime().getTime() >= date.getTime())
                    .collect(Collectors.toList());
            //今天提现金额
            withdrawalVo.setToday(collect.stream().map(Withdrawal::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 获取前月的第一天
        Calendar cale1 = Calendar.getInstance();
        cale1.add(Calendar.MONTH, 0);
        cale1.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = sdf.format(cale1.getTime());
        // 获取前月的最后一天
        cale1 = Calendar.getInstance();
        cale1.add(Calendar.MONTH, 1);
        cale1.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = sdf.format(cale1.getTime());
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            Date first = sdf.parse(firstDay);
            Date last = sdf.parse(lastDay);
            List<Withdrawal> collect = withdrawalList.stream()
                    .filter(s -> s.getHandleTime()!=null)
                    .filter(s -> s.getHandleTime().getTime() >= first.getTime())
                    .filter(s -> s.getHandleTime().getTime() <= last.getTime())
                    .collect(Collectors.toList());
            //本月提现金额
            withdrawalVo.setMonth(collect.stream().map(Withdrawal::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //累计提现金额
        withdrawalVo.setAccumulate(withdrawalList.stream()
                .filter(s -> s.getHandleTime()!=null)
                .map(Withdrawal::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        withdrawalVo.setWithdrawalList(withdrawalList);
        return withdrawalVo;
    }

    @Override
    public List<SelectListVo> selectInvestor() {
        List<SelectListVo> list = new ArrayList<>();
        List<SysUser> sysUsers = sysUserMapper.selectUserByUserType("03");
        sysUsers.stream().forEach(map->{
            SelectListVo selectListVo = new SelectListVo();
            selectListVo.setKey(map.getUserId());
            selectListVo.setValue(map.getNickName());
            list.add(selectListVo);
        });
        List<SelectListVo> collect = list.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<SelectListVo> selectAgent() {
        List<SelectListVo> list = new ArrayList<>();
        List<SysUser> sysUsers = sysUserMapper.selectUserByUserType("05");
        sysUsers.stream().forEach(map->{
            SelectListVo selectListVo = new SelectListVo();
            selectListVo.setKey(map.getUserId());
            selectListVo.setValue(map.getNickName());
            list.add(selectListVo);
        });
        List<SelectListVo> collect = list.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<SelectListVo> selectHospital() {
        List<SelectListVo> list = new ArrayList<>();
        List<Hospital> hospitalList = hospitalDeviceMapper.selectHospitalList();
        hospitalList.stream().forEach(map->{
            SelectListVo selectListVo = new SelectListVo();
            selectListVo.setKey(map.getHospitalId());
            selectListVo.setValue(map.getHospitalName());
            list.add(selectListVo);
        });
        List<SelectListVo> collect = list.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<String> selectDevice() {
        List<String> deviceNumber = new ArrayList<>();
        List<Device> deviceList = hospitalDeviceMapper.selectDeviceList();
        deviceList.stream().forEach(map->{
            deviceNumber.add(map.getDeviceNumber());
        });
        List<String> collect = deviceNumber.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<Withdrawal> selectWithdrawalList() {
        List<Withdrawal> withdrawals = financeServiceMapper.selectWithdrawalStatistics();
        return withdrawals;
    }
}
