package com.yuepei.system.mapper;

import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.DeviceDetailsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/10 10:19
 */
public interface HospitalDeviceMapper {
    List<DeviceType> selectDeviceType(@Param("userId") Long userId);

    List<Device> selectDeviceTypeDetails(@Param("deviceTypeId") Long deviceTypeId, @Param("hospitalId")Long hospitalId);

    Hospital selectHospitalByHospitalName(@Param("hospitalId") Long hospitalId);

    void updateDeviceDetails(@Param("deviceNumber") String deviceNumber,@Param("deviceFullAddress") String deviceFullAddress);

    List<GoodsOrder> selectGoodsOrder(@Param("userId") Long userId);

    GoodsOrder selectOrderByOrderId(@Param("orderId") Long orderId);

    DeviceType selectDeviceByTypeName(@Param("deviceTypeId") Long deviceTypeId);

    Goods selectGoodsByGoodsName(@Param("goodsId") Long goodsId);

    List<String> selectLeaseOrder(@Param("hospitalId") Long hospitalId);

    String selectDeviceByDeviceAddress(@Param("hospitalId") Long hospitalId);

    HospitalUser selectHospitalbyUserName(@Param("userName") String userName);
}
