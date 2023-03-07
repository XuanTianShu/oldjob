package com.yuepei.system.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @author zzy
 * @date 2023/2/9 16:33
 */
public interface HospitalDeviceService {

    List<DeviceType> selectDeviceType(Long userId);

    DeviceStatisticsVo selectDeviceTypeDetails(Long deviceTypeId, Long userId, String deviceDepartment);

    void updateDeviceDetails(Long floorId,Long departmentId,Long roomId,Long bedId,String deviceNumber);

    List<GoodsOrderVo> selectGoodsOrder(String userId);

    GoodsOrderVo selectOrderByOrderId(Long orderId);

    List<UserLeaseOrderVo> selectLeaseOrder(String userId,String deviceDepartment,String deviceTypeName,String orderNumber);

    UserLeaseOrderVo selectLeaseOrderDetails(String orderNumber);

    TotalVo selectRevenueStatistics(String userName, int statistics);

    AjaxResult loginHospitalPort(String userName, String password);

    List<Map<String,Object>> selectDeviceAddress(Long hospitalId);

//    Map<String,List<Object>> selectDeviceAddress1(Long hospitalId);

    List<String> selectDepartment(Long userId);

    List<String> selectDeviceTypeName();

}
