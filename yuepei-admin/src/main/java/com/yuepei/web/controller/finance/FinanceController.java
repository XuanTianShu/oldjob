package com.yuepei.web.controller.finance;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.system.domain.Withdrawal;
import com.yuepei.system.domain.vo.RevenueStatisticsDetailsVo;
import com.yuepei.system.domain.vo.RevenueStatisticsVo;
import com.yuepei.system.service.FinanceService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 财务管理controller
 * @author zzy
 * @date 2023/4/20 18:00
 */
@RestController
@RequestMapping("/system/finance")
public class FinanceController extends BaseController {
    @Autowired
    private FinanceService financeService;

    /**投资人收益*/
    @GetMapping("/investorIncomeStatistics")
    public TableDataInfo investorIncomeStatistics(@RequestParam(value = "nickName",required = false,defaultValue = "")String nickName,
                                                  @RequestParam(value = "startTime",required = false,defaultValue = "")String startTime,
                                                  @RequestParam(value = "endTime",required = false,defaultValue = "")String endTime){
        startPage();
        List<RevenueStatisticsDetailsVo> revenueStatisticsDetailsVos = financeService.investorIncomeStatistics();
        return getDataTable(revenueStatisticsDetailsVos);
    }

    /**代理商收益*/
    @GetMapping("/agentIncomeStatistics")
    public TableDataInfo agentIncomeStatistics(){
        startPage();
        List<RevenueStatisticsDetailsVo> revenueStatisticsDetailsVos = financeService.agentIncomeStatistics();
        return getDataTable(revenueStatisticsDetailsVos);
    }

    /**医院收益*/
    @GetMapping("/hospitalIncomeStatistics")
    public TableDataInfo hospitalIncomeStatistics(){
        startPage();
        List<RevenueStatisticsDetailsVo> revenueStatisticsDetailsVos = financeService.hospitalIncomeStatistics();
        return getDataTable(revenueStatisticsDetailsVos);
    }

    /**营业统计*/
    @GetMapping("/businessStatistics")
    public TableDataInfo businessStatistics(@RequestParam("type")Long type){
        startPage();
        List<RevenueStatisticsVo> revenueStatisticsVos = financeService.businessStatistics(type);
        return getDataTable(revenueStatisticsVos);
    }

    /**银行卡提现申请*/
    @GetMapping("/withdrawalApplication")
    public AjaxResult withdrawalApplication(@RequestBody Withdrawal withdrawal){
        return AjaxResult.success(financeService.withdrawalApplication(withdrawal));
    }
}
