package com.yuepei.web.controller.finance;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.system.domain.Withdrawal;
import com.yuepei.system.domain.vo.RevenueStatisticsDetailsVo;
import com.yuepei.system.domain.vo.RevenueStatisticsVo;
import com.yuepei.system.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public TableDataInfo investorIncomeStatistics(@RequestParam(value = "investorId",required = false)Long investorId,
                                                  @RequestParam(value = "startTime",required = false,defaultValue = "") String startTime,
                                                  @RequestParam(value = "endTime",required = false,defaultValue = "") String endTime){
        startPage();
        List<RevenueStatisticsDetailsVo> revenueStatisticsDetailsVos = financeService.investorIncomeStatistics(investorId,startTime,endTime);
        return getDataTable(revenueStatisticsDetailsVos);
    }

    /**代理商收益*/
    @GetMapping("/agentIncomeStatistics")
    public TableDataInfo agentIncomeStatistics(@RequestParam(value = "agentId",required = false)Long agentId,
                                               @RequestParam(value = "startTime",required = false,defaultValue = "") String startTime,
                                               @RequestParam(value = "endTime",required = false,defaultValue = "") String endTime){
        startPage();
        List<RevenueStatisticsDetailsVo> revenueStatisticsDetailsVos = financeService.agentIncomeStatistics(agentId,startTime,endTime);
        return getDataTable(revenueStatisticsDetailsVos);
    }

    /**医院收益*/
    @GetMapping("/hospitalIncomeStatistics")
    public TableDataInfo hospitalIncomeStatistics(@RequestParam(value = "hospitalId",required = false)Long hospitalId,
                                                  @RequestParam(value = "startTime",required = false,defaultValue = "") String startTime,
                                                  @RequestParam(value = "endTime",required = false,defaultValue = "") String endTime){
        startPage();
        List<RevenueStatisticsDetailsVo> revenueStatisticsDetailsVos = financeService.hospitalIncomeStatistics(hospitalId,startTime,endTime);
        return getDataTable(revenueStatisticsDetailsVos);
    }

    /**设备收益*/
    @GetMapping("/deviceStatistics")
    public TableDataInfo deviceStatistics(@RequestParam(value = "nickName",required = false,defaultValue = "")String deviceNumber,
                                          @RequestParam(value = "startTime",required = false,defaultValue = "") String startTime,
                                          @RequestParam(value = "endTime",required = false,defaultValue = "") String endTime){
        startPage();
        List<RevenueStatisticsDetailsVo> revenueStatisticsDetailsVos = financeService.deviceStatistics(deviceNumber,startTime,endTime);
        return getDataTable(revenueStatisticsDetailsVos);
    }

    /**营业统计*/
    @GetMapping("/businessStatistics")
    public TableDataInfo businessStatistics(@RequestParam("type")Long type,
                                            @RequestParam(value = "payType",required = false)String payType,
                                            @RequestParam(value = "agentId",required = false)String agentId,
                                            @RequestParam(value = "hospitalId",required = false)String hospitalId,
                                            @RequestParam(value = "deviceNumber",required = false)String deviceNumber,
                                            @RequestParam(value = "startTime",required = false)String startTime,
                                            @RequestParam(value = "endTime",required = false)String endTime){
        startPage();
        List<RevenueStatisticsVo> revenueStatisticsVos = financeService.businessStatistics(type,payType,agentId,hospitalId,deviceNumber,startTime,endTime);
        return getDataTable(revenueStatisticsVos);
    }

    /**银行卡提现申请*/
    @GetMapping("/withdrawalApplication")
    public AjaxResult withdrawalApplication(@RequestBody Withdrawal withdrawal){
        return AjaxResult.success(financeService.withdrawalApplication(withdrawal));
    }

    /**提现统计*/
    @GetMapping("withdrawalStatistics")
    public TableDataInfo withdrawalStatistics(@RequestParam(value = "userId",required = false)Long userId,
                                              @RequestParam(value = "status",required = false)Long status,
                                              @RequestParam(value = "startApplyTime",required = false)String startApplyTime,
                                              @RequestParam(value = "endApplyTime",required = false)String endApplyTime,
                                              @RequestParam(value = "startHandleTime",required = false)String startHandleTime,
                                              @RequestParam(value = "endHandleTime",required = false)String endHandleTime){
        startPage();
        List<Withdrawal> withdrawalList = financeService.withdrawalStatistics(userId,status,startApplyTime,endApplyTime,startHandleTime,endHandleTime);
        return getDataTable(withdrawalList);
    }
}
