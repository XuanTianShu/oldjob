package com.yuepei.maintenance.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.maintenance.domain.vo.HomeVO;
import com.yuepei.maintenance.domain.vo.MalfunctionVO;
import com.yuepei.maintenance.domain.vo.StockVO;
import com.yuepei.maintenance.service.AppletMaintenanceService;
import com.yuepei.system.domain.SysUserFeedback;
import com.yuepei.utils.TokenUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序运维/补货端
 */
@RestController
@RequestMapping("/wechat/user/maintenance/home")
public class MaintenanceHomeController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AppletMaintenanceService appletMaintenanceService;

    /**
     * 首页
     * @param request
     * @return
     */
    @GetMapping("/list")
    public AjaxResult list(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        Map<String,Object> map = new HashMap<>();
        List<HomeVO> list = appletMaintenanceService.selectAppletMaintenanceList(user.getUserId());
        int deviceCount = appletMaintenanceService.selectAppletMaintenanceDeviceCount(user.getUserId());
        int stockCount = appletMaintenanceService.selectAppletMaintenanceStockCount(user.getUserId());
        map.put("HomeList",list);
        map.put("deviceCount",deviceCount);
        map.put("stockCount",stockCount);
        return AjaxResult.success(map);
    }

    /**
     * 设备缺货
     * @param request
     * @param deviceNumber 设备编号或设备地址
     * @return
     */
    @GetMapping("/stock")
    public AjaxResult stock(HttpServletRequest request,String deviceNumber){
        SysUser user = tokenUtils.analysis(request);
        Map<String,Object> map = new HashMap<>();
        List<StockVO> list = appletMaintenanceService.selectAppletMaintenanceStockList(user.getUserId(),deviceNumber);
        int stockCount = appletMaintenanceService.selectAppletMaintenanceStockCountByDeviceNumber(user.getUserId(),deviceNumber);
        map.put("list",list);
        map.put("stockCount",stockCount);
        return AjaxResult.success(map);
    }

    /**
     * 设备故障
     * @param request
     * @param deviceNumber 设备编号或设备地址
     * @return
     */
    @GetMapping("/malfunction")
    public AjaxResult malfunction(HttpServletRequest request,String deviceNumber){
        SysUser user = tokenUtils.analysis(request);
        Map<String,Object> map = new HashMap<>();
        List<MalfunctionVO> list = appletMaintenanceService.selectAppletMaintenanceMalfunctionList(user.getUserId(),deviceNumber);
        int malfunctionCount = appletMaintenanceService.selectAppletMaintenanceMalfunctionCount(user.getUserId(),deviceNumber);
        map.put("list",list);
        map.put("malfunctionCount",malfunctionCount);
        return AjaxResult.success(map);
    }


    /**
     * 填写维修记录
     * @param sysUserFeedback
     * @param request
     * @return
     */
    @PostMapping("/insertMaintenanceRecord")
    public AjaxResult insertMaintenanceRecord(SysUserFeedback sysUserFeedback,HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        sysUserFeedback.setFeedbackUserId(user.getUserId());
        return AjaxResult.success(appletMaintenanceService.insertMaintenanceRecord(sysUserFeedback));
    }

//    @GetMapping("/")
}
