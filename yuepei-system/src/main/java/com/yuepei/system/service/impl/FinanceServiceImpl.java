package com.yuepei.system.service.impl;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.Bank;
import com.yuepei.system.domain.Withdrawal;
import com.yuepei.system.domain.vo.RevenueStatisticsDetailsVo;
import com.yuepei.system.domain.vo.RevenueStatisticsVo;
import com.yuepei.system.mapper.FinanceServiceMapper;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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

    @Override
    public List<RevenueStatisticsDetailsVo> investorIncomeStatistics(Long investorId, String startTime, String endTime) {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = new ArrayList<>();
        List<RevenueStatisticsDetailsVo> revenueStatisticsVos = financeServiceMapper.selectRevenueStatistics();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = new Date();
        Date end = new Date();
        try {
            start = format.parse(startTime);
            end = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (investorId!=null){
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getUserId().equals(investorId)).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!startTime.equals("")){
            Date finalStart = start;
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() >= finalStart.getTime()).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!endTime.equals("")){
            Date finalEnd = end;
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() <= finalEnd.getTime()).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        revenueStatisticsVoList.addAll(revenueStatisticsVos);
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsDetailsVo> agentIncomeStatistics(Long agentId, String startTime, String endTime) {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = new ArrayList<>();
        List<RevenueStatisticsDetailsVo> revenueStatisticsVos = financeServiceMapper.selectAgentIncomeStatistics();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = new Date();
        Date end = new Date();
        try {
            start = format.parse(startTime);
            end = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (agentId!=null){
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getUserId().equals(agentId)).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!startTime.equals("")){
            Date finalStart = start;
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() >= finalStart.getTime()).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!endTime.equals("")){
            Date finalEnd = end;
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() <= finalEnd.getTime()).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        revenueStatisticsVoList.addAll(revenueStatisticsVos);
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsDetailsVo> hospitalIncomeStatistics(Long hospitalId, String startTime, String endTime) {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = new ArrayList<>();
        List<RevenueStatisticsDetailsVo> revenueStatisticsVos = financeServiceMapper.selectHospitalIncomeStatistics();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = new Date();
        Date end = new Date();
        try {
            start = format.parse(startTime);
            end = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (hospitalId!=null){
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getUserId().equals(hospitalId)).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!startTime.equals("")){
            Date finalStart = start;
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() >= finalStart.getTime()).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!endTime.equals("")){
            Date finalEnd = end;
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() <= finalEnd.getTime()).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        revenueStatisticsVoList.addAll(revenueStatisticsVos);
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsDetailsVo> deviceStatistics(String deviceNumber, String startTime, String endTime) {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = new ArrayList<>();
        List<RevenueStatisticsDetailsVo> revenueStatisticsVos = financeServiceMapper.selectDeviceStatistics();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = new Date();
        Date end = new Date();
        try {
            start = format.parse(startTime);
            end = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!deviceNumber.equals("")){
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getDeviceNumber().equals(deviceNumber)).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!startTime.equals("")){
            Date finalStart = start;
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() >= finalStart.getTime()).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
        }
        if (!endTime.equals("")){
            Date finalEnd = end;
            List<RevenueStatisticsDetailsVo> collect = revenueStatisticsVos.stream().filter(map -> map.getCreateTime().getTime() <= finalEnd.getTime()).collect(Collectors.toList());
            revenueStatisticsVos.clear();
            revenueStatisticsVos.addAll(collect);
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
        financeServiceMapper.insertWithdrawalApplication(withdrawal);
        return "申请成功";
    }

    @Override
    public List<Withdrawal> withdrawalStatistics(Long userId, Long status, String startApplyTime, String endApplyTime, String startHandleTime, String endHandleTime) {
        List<Withdrawal> withdrawalList = financeServiceMapper.selectWithdrawalStatistics(userId,status,startApplyTime,endApplyTime,startHandleTime,endHandleTime);
        return withdrawalList;
    }
}
