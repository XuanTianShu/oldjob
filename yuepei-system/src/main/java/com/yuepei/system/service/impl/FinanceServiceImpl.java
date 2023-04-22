package com.yuepei.system.service.impl;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.uuid.UUID;
import com.yuepei.system.domain.Bank;
import com.yuepei.system.domain.Withdrawal;
import com.yuepei.system.domain.vo.RevenueStatisticsDetailsVo;
import com.yuepei.system.domain.vo.RevenueStatisticsVo;
import com.yuepei.system.mapper.FinanceServiceMapper;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public List<RevenueStatisticsDetailsVo> investorIncomeStatistics() {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = financeServiceMapper.selectRevenueStatistics();
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsDetailsVo> agentIncomeStatistics() {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = financeServiceMapper.selectAgentIncomeStatistics();
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsDetailsVo> hospitalIncomeStatistics() {
        List<RevenueStatisticsDetailsVo> revenueStatisticsVoList = financeServiceMapper.selectHospitalIncomeStatistics();
        return revenueStatisticsVoList;
    }

    @Override
    public List<RevenueStatisticsVo> businessStatistics(Long type) {
        List<RevenueStatisticsVo> revenueStatisticsVoList = new ArrayList<>();
        if (type==1){
            //按年统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByYear();
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==2){
            //按月统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByMonth();
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==3){
            //按天统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByDay();
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==4){
            //按投资商统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByInvestor();
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==5){
            //按代理统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByAgent();
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==6){
            //按代理商医院统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByHospital();
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }else if (type==7){
            //按平台医院统计
            List<RevenueStatisticsVo> revenueStatisticsVos = financeServiceMapper.selectStatisticsByPlatformHospital();
            revenueStatisticsVoList.addAll(revenueStatisticsVos);
        }
        return revenueStatisticsVoList;
    }

    @Override
    public String withdrawalApplication(Withdrawal withdrawal) {
        SysUser sysUser = sysUserMapper.selectUserById(withdrawal.getUserId());
        Bank bank = sysUserMapper.selectBank(withdrawal.getUserId());
        withdrawal.setOrderNumber(UUID.randomUUID().toString().replace("-", ""));
        withdrawal.setRole(sysUser.getUserType());
        withdrawal.setRoleName(sysUser.getNickName());
        withdrawal.setWithdrawalInformation("开户人："+bank.getAccountHolder()+" 银行名称："+bank.getBankName()+" 银行卡号："+bank.getBankNumber());
        withdrawal.setReceived(1L);
        withdrawal.setApplyTime(new Date());
        financeServiceMapper.insertWithdrawalApplication(withdrawal);
        return "申请成功";
    }
}
