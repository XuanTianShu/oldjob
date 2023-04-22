package com.yuepei.web.controller.finance;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.Withdrawal;
import com.yuepei.system.service.FinanceService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 财务管理controller
 * @author zzy
 * @date 2023/4/20 18:00
 */
@RestController
@RequestMapping("/system/finance")
public class FinanceController {
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private FinanceService financeService;

    /**投资人收益*/
    @GetMapping("/investorIncomeStatistics")
    public AjaxResult investorIncomeStatistics(){
        return AjaxResult.success(financeService.investorIncomeStatistics());
    }

    /**代理商收益*/
    @GetMapping("/agentIncomeStatistics")
    public AjaxResult agentIncomeStatistics(){
        return AjaxResult.success(financeService.agentIncomeStatistics());
    }

    /**医院收益*/
    @GetMapping("/hospitalIncomeStatistics")
    public AjaxResult hospitalIncomeStatistics(){
        return AjaxResult.success(financeService.hospitalIncomeStatistics());
    }

    /**营业统计*/
    @GetMapping("/businessStatistics")
    public AjaxResult businessStatistics(@RequestParam("type")Long type){
        return AjaxResult.success(financeService.businessStatistics(type));
    }

    /**提现申请*/
    @GetMapping("/withdrawalApplication")
    public AjaxResult withdrawalApplication(@RequestBody Withdrawal withdrawal){
        return AjaxResult.success(financeService.withdrawalApplication(withdrawal));
    }
}
