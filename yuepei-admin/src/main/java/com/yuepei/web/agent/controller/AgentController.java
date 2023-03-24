package com.yuepei.web.agent.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.vo.FeedbackInfoVo;
import com.yuepei.system.domain.vo.HospitalAgentVo;
import com.yuepei.system.domain.vo.SubAccountVo;
import com.yuepei.system.service.HospitalDeviceService;
import com.yuepei.utils.TokenUtils;
import com.yuepei.web.agent.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
     * 代理-设备管理
     * */
    @GetMapping("/selectAgentInfo")
    private AjaxResult selectAgentInfo(@RequestParam(value = "userId")Long userId,
                                       @RequestParam(value = "hospitalId",required = false,defaultValue = "")Long hospitalId,
                                       @RequestParam(value = "utilizationRate",required = false,defaultValue = "")Long utilizationRate){
        return AjaxResult.success(agentService.selectAgentInfo(userId,hospitalId,utilizationRate));
    }

    /**根据医院查询设备详细地址*/
    @GetMapping("/selectDeviceAddressByHospital/{hospitalId}")
    private AjaxResult selectDeviceAddressByHospital(@PathVariable("hospitalId") Long hospitalId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceAddress1(hospitalId));
    }

    /**科室下拉框*/
    @GetMapping("/selectDepartment/{userId}")
    private AjaxResult selectDepartment(@PathVariable("userId") Long userId){
        return AjaxResult.success(agentService.selectDepartment(userId));
    }

    /**
     * 代理端-编辑信息
     * */
    @GetMapping("/updateDeviceDetails")
    private AjaxResult updateDeviceDetails(@RequestParam(value = "floorId",required = false,defaultValue = "")Long floorId,
                                           @RequestParam(value = "departmentId",required = false,defaultValue = "")Long departmentId,
                                           @RequestParam(value = "roomId",required = false,defaultValue = "")Long roomId,
                                           @RequestParam(value = "bedId",required = false,defaultValue = "")Long bedId,
                                           @RequestParam(value = "deviceNumber",required = false,defaultValue = "")String deviceNumber){
        hospitalDeviceService.updateDeviceDetails(floorId,departmentId,roomId,bedId,deviceNumber);
        return AjaxResult.success();
    }

    /**
     * 代理端-陪护床-设备详情
     * */
    @GetMapping("/selectDeviceDetailsByDeviceNumber")
    private AjaxResult selectDeviceDetailsByDeviceNumber(@RequestParam("deviceNumber")String deviceNumber){
        return AjaxResult.success(agentService.selectDeviceDetailsByDeviceNumber(deviceNumber));
    }

    /**代理端-医院管理*/
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

    /**医院下拉框*/
    @GetMapping("/selectHospitalList")
    private AjaxResult selectHospitalList(){
        return AjaxResult.success(agentService.selectHospitalList());
    }

    /**查看该代理商下管理的医院*/
    @GetMapping("/selectAgentHospitalList/{userId}")
    private AjaxResult selectAgentHospitalList(@PathVariable(value = "userId")Long userId){
        return AjaxResult.success(agentService.selectAgentHospitalList(userId));
    }

    /**设备下拉框*/
    @GetMapping("/selectDeviceList/{userId}")
    private AjaxResult selectDeviceList(@PathVariable(value = "userId")Long userId){
        return AjaxResult.success(agentService.selectDeviceList(userId));
    }

    /**代理端-添加医院*/
    @PostMapping("/addHospitalByAgent")
    private AjaxResult addHospitalByAgent(@RequestBody HospitalAgentVo hospitalAgentVo){
        return AjaxResult.success(agentService.insertHospitalByAgent(hospitalAgentVo));
    }

    /**代理端-租借订单*/
    @GetMapping("/selectLeaseOrder")
    private AjaxResult selectLeaseOrder(@RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                        @RequestParam(value = "deviceTypeName",required = false,defaultValue = "") String deviceTypeNamue,
                                        @RequestParam(value = "nameOrNumber",required = false,defaultValue = "") String nameOrNumber,
                                        @RequestParam(value = "userId") Long userId){
        return AjaxResult.success(agentService.selectLeaseOrder(userId,deviceDepartment,deviceTypeNamue,nameOrNumber));
    }

    /**查询当前用户是哪个代理商*/
    @GetMapping("/selectAgentByUser/{userId}")
    private AjaxResult selectAgentByUser(@PathVariable(value = "userId")Long userId){
        return AjaxResult.success(agentService.selectAgentByUser(userId));
    }

    /**代理端-开通子账户*/
    @PostMapping("/insertAgentAccount")
    private AjaxResult insertAgentAccount(@RequestBody SubAccountVo subAccountVo){
        return AjaxResult.success(agentService.insertAgentAccount(subAccountVo));
    }

    /**代理端-子账户管理列表*/
    @GetMapping("/selectSubAccount/{userId}")
    private AjaxResult selectSubAccount(@PathVariable(value = "userId")Long userId){
        return AjaxResult.success(agentService.selectSubAccount(userId));
    }

    /**代理端-设备管理*/
    @GetMapping("/selectAgentByDevice/{userId}")
    private AjaxResult selectAgentByDevice(@PathVariable(value = "userId")Long userId){
        return AjaxResult.success(agentService.selectAgentByDevice(userId));
    }

    /**代理端-设备详情-没有选择医院*/
    @GetMapping("/selectDeviceTypeDetails")
    private AjaxResult selectDeviceTypeDetails(@RequestParam(value = "hospitalId",required = false,defaultValue = "") Long hospitalId,
                                               @RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                               @RequestParam(value = "utilizationRate",required = false,defaultValue = "") Long utilizationRate,
                                               @RequestParam(value = "userId") Long userId,
                                               @RequestParam(value = "deviceTypeId",required = false,defaultValue = "") Long deviceTypeId){
        return AjaxResult.success(agentService.selectDeviceTypeDetails(userId,deviceTypeId,hospitalId,deviceDepartment,utilizationRate));
    }

    /**代理端-营收统计*/
    @GetMapping("/selectAgentRevenueStatistics")
    private AjaxResult selectAgentRevenueStatistics(@RequestParam("statistics")int statistics,
                                                    @RequestParam("userId") Long userId){
        return AjaxResult.success(agentService.selectAgentRevenueStatistics(statistics,userId));
    }

    /**个人资料*/
    @GetMapping("/selectPersonalData/{userId}")
    public AjaxResult selectPersonalData(@PathVariable("userId")Long userId){
        return AjaxResult.success(hospitalDeviceService.selectPersonalData(userId));
    }

    /**代理端-陪护床-订单详情*/
    @GetMapping("/selectLeaseOrderDetails")
    public AjaxResult selectLeaseOrderDetails(@RequestParam(value = "orderNumber",required = false) String orderNumber,
                                              @RequestParam("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrderDetails(orderNumber,userId));
    }

    /**代理端-设备故障*/
    @GetMapping("/selectDeviceFaultList")
    private AjaxResult selectDeviceFaultList(@RequestParam("userId")Long userId,
                                             @RequestParam(value = "status",required = false,defaultValue = "0")Integer status,
                                             @RequestParam(value = "numberOrAddress",required = false,defaultValue = "")String numberOrAddress){
        return AjaxResult.success(agentService.selectDeviceFaultList(userId,status,numberOrAddress));
    }

    /**代理端-故障详情-待维修*/
    @GetMapping("/selectDeviceFaultDetails")
    private AjaxResult selectDeviceFaultDetails(@RequestParam("userId")Long userId,
                                                @RequestParam(value = "status",required = false)Integer status,
                                                @RequestParam("feedbackId")Long feedbackId){
        return AjaxResult.success(agentService.selectDeviceFaultDetails(userId,status,feedbackId));
    }

    /**代理端-故障详情-待维修-填写维修记录*/
    @PostMapping("/writeMaintenanceRecords")
    private AjaxResult writeMaintenanceRecords(@RequestBody FeedbackInfoVo feedback){
        return AjaxResult.success(agentService.writeMaintenanceRecords(feedback));
    }

    /**代理端-故障详情-维修完成*/
    @GetMapping("/feedbackRepairCompleted/{feedbackId}")
    private AjaxResult feedbackRepairCompleted(@PathVariable("feedbackId")Long feedbackId){
        return AjaxResult.success(agentService.feedbackRepairCompleted(feedbackId));
    }

    /**代理端-拍照上传*/
    @PostMapping("/uploadsFile")
    private AjaxResult uploadsFile(@RequestBody FeedbackInfoVo feedbackInfoVo){
        return AjaxResult.success(agentService.uploadsFile(feedbackInfoVo));
    }

    /**代理端-上传的文档列表*/
    @GetMapping("/selectUploadsFileList/{userId}")
    private AjaxResult selectUploadsFileList(@PathVariable("userId")Long userId){
        return AjaxResult.success(agentService.selectUploadsFileList(userId));
    }

    /**代理端-上传文档列表-未处理*/
    @GetMapping("/selectUploadsFileListDetails/{feedbackId}")
    private AjaxResult selectUploadsFileListDetails(@PathVariable("feedbackId")Long feedbackId){
        return AjaxResult.success(agentService.selectUploadsFileListDetails(feedbackId));
    }
}
