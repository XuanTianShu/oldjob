package com.yuepei.web.hospital.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.domain.vo.*;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/9 16:33
 */
public interface HospitalDeviceService {

    List<DeviceType> selectDeviceType(Long userId);

    List<DeviceDetailsVo> selectDeviceTypeDetails(Long deviceTypeId, Long userId);

    void updateDeviceDetails(DeviceDetailsVo deviceDetailsVo);

    List<GoodsOrderVo> selectGoodsOrder(Long userId);

    GoodsOrderVo selectOrderByOrderId(Long orderId);

    List<UserLeaseOrderVo> selectLeaseOrder(String userName);

    UserLeaseOrderVo selectLeaseOrderDetails(String orderNumber);

    TotalVo selectRevenueStatistics(String userName, int statistics);

    AjaxResult loginHospitalPort(String userName, String password);
}
