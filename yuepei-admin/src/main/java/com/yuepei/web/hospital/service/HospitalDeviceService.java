package com.yuepei.web.hospital.service;

import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.system.domain.vo.GoodsOrderVo;
import com.yuepei.system.domain.vo.RevenueStatisticsVo;
import com.yuepei.system.domain.vo.UserLeaseOrderVo;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/9 16:33
 */
public interface HospitalDeviceService {

    DeviceType selectDeviceType(Long userId);

    List<DeviceDetailsVo> selectDeviceTypeDetails(Long deviceTypeId, Long hospitalId);

    void updateDeviceDetails(DeviceDetailsVo deviceDetailsVo);

    List<GoodsOrderVo> selectGoodsOrder(Long userId);

    GoodsOrderVo selectOrderByOrderId(Long orderId);

    List<UserLeaseOrderVo> selectLeaseOrder(Long hospitalId);

    UserLeaseOrderVo selectLeaseOrderDetails(String orderNumber);

    List<UserLeaseOrderVo> selectRevenueStatistics(Long hospitalId, int statistics);

}
