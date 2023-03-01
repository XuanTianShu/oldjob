package com.yuepei.web.agent.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.system.domain.vo.HospitalAgentVo;
import com.yuepei.utils.TokenUtils;
import com.yuepei.web.agent.service.AgentService;
import com.yuepei.web.hospital.service.HospitalDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zzy
 * @date 2023/2/17 15:01
 */
@RestController
@RequestMapping("/agent/user/info")
public class AgentController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AgentService agentService;

    @Autowired
    private HospitalDeviceService hospitalDeviceService;

    /**
     * 查询代理设备信息
     * */
    @GetMapping("/selectAgentInfo")
    private AjaxResult selectAgentInfo(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(agentService.selectAgentInfo(analysis.getUserId()));
    }

    /**
     * 编辑代理设备信息
     * */
    @PostMapping("/updateDeviceDetails")
    private AjaxResult updateDeviceDetails(@RequestBody DeviceDetailsVo deviceDetailsVo){
        hospitalDeviceService.updateDeviceDetails(deviceDetailsVo);
        return AjaxResult.success();
    }

    /**
     * 查询设备详情
     * */
    @GetMapping("/selectDeviceDetailsByDeviceNumber")
    private AjaxResult selectDeviceDetailsByDeviceNumber(@RequestParam("deviceNumber")String deviceNumber){
        return AjaxResult.success(agentService.selectDeviceDetailsByDeviceNumber(deviceNumber));
    }

    /**代理端医院管理*/
    @GetMapping("/selectHospitalAdministration")
    private AjaxResult selectHospitalAdministration(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(agentService.selectHospitalAdministration(analysis.getUserId()));
    }

    /**代理端添加医院*/
    @PostMapping("/addHospitalByAgent")
    private AjaxResult addHospitalByAgent(@RequestBody HospitalAgentVo hospitalAgentVo,
                                          HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(agentService.insertHospitalByAgent(hospitalAgentVo,user.getUserName()));
    }

    /**代理端租借订单*/
    @GetMapping("/selectLeaseOrder")
    private AjaxResult selectLeaseOrder(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(agentService.selectLeaseOrder(analysis.getUserId()));
    }

    /**开通子账户*/
    @PostMapping("/insertAgentAccount")
    private AjaxResult insertAgentAccount(@RequestBody SysUser sysUser,
                                          HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(agentService.insertAgentAccount(sysUser,analysis.getUserId()));
    }

    /**故障设备列表*/
    /*@GetMapping("/selectDeviceFaultList")
    private AjaxResult selectDeviceFaultList(HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(agentService.selectDeviceFaultList(analysis.getUserId()));
    }*/
}
