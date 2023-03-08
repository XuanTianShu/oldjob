package com.yuepei.web.agent.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.system.domain.vo.HospitalAgentVo;
import com.yuepei.system.service.HospitalDeviceService;
import com.yuepei.utils.TokenUtils;
import com.yuepei.web.agent.service.AgentService;
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
    private AjaxResult selectAgentInfo(@RequestParam(value = "userId")Long userId,
                                       @RequestParam(value = "utilizationRate",required = false,defaultValue = "")Long utilizationRate){
        return AjaxResult.success(agentService.selectAgentInfo(userId,utilizationRate));
    }

    /**
     * 编辑代理设备信息
     * */
    @GetMapping("/updateDeviceDetails")
    private AjaxResult updateDeviceDetails(@RequestParam(value = "floorId",required = false,defaultValue = "")Long floorId,
                                           @RequestParam(value = "departmentId",required = false,defaultValue = "")Long departmentId,
                                           @RequestParam(value = "roomId",required = false,defaultValue = "")Long roomId,
                                           @RequestParam(value = "bedId",required = false,defaultValue = "")Long bedId,
                                           @RequestParam(value = "deviceNumber",required = false,defaultValue = "")String deviceNumber
    ){
        hospitalDeviceService.updateDeviceDetails(floorId,departmentId,roomId,bedId,deviceNumber);
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
    private AjaxResult selectHospitalAdministration(@RequestParam(value = "userId")Long userId,
                                                    @RequestParam(value = "hospitalId",required = false,defaultValue = "")Long hospitalId,
                                                    @RequestParam(value = "utilizationRate",required = false,defaultValue = "")Long utilizationRate){
        return AjaxResult.success(agentService.selectHospitalAdministration(userId,hospitalId,utilizationRate));
    }

    /**查询可分配分成比例*/
    @GetMapping("/selectProportion/{userId}")
    private AjaxResult selectProportion(@PathVariable(value = "userId")Long userId){
        return AjaxResult.success(agentService.selectProportion(userId));
    }

    /**代理端添加医院*/
    @PostMapping("/addHospitalByAgent")
    private AjaxResult addHospitalByAgent(@RequestBody HospitalAgentVo hospitalAgentVo,
                                          @RequestParam(value = "userId")Long userId){
        return AjaxResult.success(agentService.insertHospitalByAgent(hospitalAgentVo,userId));
    }

    /**代理端租借订单*/
    @GetMapping("/selectLeaseOrder")
    private AjaxResult selectLeaseOrder(@RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                        @RequestParam(value = "deviceTypeName",required = false,defaultValue = "") String deviceTypeNamue,
                                        @RequestParam(value = "nameOrNumber",required = false,defaultValue = "") String nameOrNumber,
                                        HttpServletRequest request){
        SysUser analysis = tokenUtils.analysis(request);
        return AjaxResult.success(agentService.selectLeaseOrder(analysis.getUserId(),deviceDepartment,deviceTypeNamue,nameOrNumber));
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
