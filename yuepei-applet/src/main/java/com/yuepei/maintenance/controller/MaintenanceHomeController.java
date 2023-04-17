package com.yuepei.maintenance.controller;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.maintenance.domain.vo.HomeVO;
import com.yuepei.maintenance.domain.vo.LeaseDeviceListVO;
import com.yuepei.maintenance.domain.vo.MalfunctionVO;
import com.yuepei.maintenance.domain.vo.StockVO;
import com.yuepei.maintenance.service.AppletMaintenanceService;
import com.yuepei.service.UnlockingService;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.OrderProportionDetail;
import com.yuepei.system.domain.SysUserFeedback;
import com.yuepei.system.mapper.DeviceInvestorMapper;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.system.service.DeviceService;
import com.yuepei.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.*;

/**
 * 小程序运维/补货端
 */
@Slf4j
@RestController
@RequestMapping("/wechat/user/maintenance/home")
public class MaintenanceHomeController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AppletMaintenanceService appletMaintenanceService;

    @Autowired
    private UnlockingService unlockingService;

    @Autowired
    private DeviceInvestorMapper deviceInvestorMapper;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @GetMapping("/test/{deviceNumber}")
    public AjaxResult test(@PathVariable("deviceNumber") String deviceNumber){
        String uuid = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        String orderNumber = uuid.substring(uuid.length() - 16);
        //添加投资人订单分成明细
        List<OrderProportionDetail> orderProportionDetailList = deviceInvestorMapper.selectInvestorListByDeviceNumber(deviceNumber);
        if (orderProportionDetailList.size() > 0){
            userLeaseOrderMapper.insertOrderProportionDeatail(orderNumber,orderProportionDetailList);
        }
        return AjaxResult.success();
    }

    /**
     * 首页
     * @param request
     * @return
     */
    @GetMapping("/list")
    public AjaxResult list(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        log.info("{}",user.getUserId());
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
//        SysUser user = tokenUtils.analysis(request);
        Map<String,Object> map = new HashMap<>();
        List<MalfunctionVO> list = appletMaintenanceService.selectAppletMaintenanceMalfunctionList(201L,deviceNumber);
        int malfunctionCount = appletMaintenanceService.selectAppletMaintenanceMalfunctionCount(201L,deviceNumber);
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
    public AjaxResult insertMaintenanceRecord(@RequestBody SysUserFeedback sysUserFeedback, HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        sysUserFeedback.setFeedbackUserId(user.getUserId());
        sysUserFeedback.setCreateTime(new Date());
        return AjaxResult.success(appletMaintenanceService.insertMaintenanceRecord(sysUserFeedback));
    }

    /**
     * 维修完成
     * @param feedbackId
     * @return
     */
    @GetMapping("/getDetail/{feedbackId}")
    public AjaxResult getDetail(@PathVariable("feedbackId") Long feedbackId){
        return AjaxResult.success(appletMaintenanceService.getDetail(feedbackId));
    }

    /**
     * 租借设备列表
     * @param deviceNumber 设备编号或设备地址
     * @param request
     * @return
     */
    @GetMapping("/leaseDeviceList")
    public AjaxResult leaseDeviceList(String deviceNumber,HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        Map<String,Object> map = new HashMap<>();
        List<LeaseDeviceListVO> leaseDeviceListVOS = appletMaintenanceService.leaseDeviceList(deviceNumber, user.getUserId());
        int leaseDeviceCount = appletMaintenanceService.leaseDeviceCount(deviceNumber,user.getUserId());
        map.put("list",leaseDeviceListVOS);
        map.put("leaseDeviceCount",leaseDeviceCount);
        return AjaxResult.success(map);
    }

    /**
     * 租借设备列表详情
     * @param deviceNumber 设备编号
     * @param request
     * @return
     */
    @GetMapping("/leaseDeviceDetails")
    public AjaxResult leaseDeviceDetails(String deviceNumber,HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(appletMaintenanceService.leaseDeviceDetails(deviceNumber,user.getUserId()));
    }

    /**
     * 测试设备流程扫码
     * @param request
     * @param deviceNumber
     * @return
     */
    @GetMapping("/testDevice")
    public AjaxResult testDevice(HttpServletRequest request,String deviceNumber){
        SysUser user = tokenUtils.analysis(request);
        Device device = deviceService.checkDevice(user.getUserId(),deviceNumber);
        if (device == null){
            return AjaxResult.error("没有该设备的访问权限");
        }
        return AjaxResult.success(appletMaintenanceService.testDevice(deviceNumber));
    }

    /**
     * 开锁
     * @param device
     * @return
     */
    @PostMapping("/unlocking")
    public AjaxResult unlocking(@RequestBody Device device){
        Device device1 = deviceService.selectDeviceByDeviceNumber(device.getDeviceNumber());
        device1.setLock(device.getLock());
        return unlockingService.unlocking(device1);
    }
}
