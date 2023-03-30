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

    List<String> selectLeaseOrder(@Param("userId") Long userId);

    HospitalUser selectHospitalbyUserName(@Param("userName") String userName);

    DeviceType selectDeviceByType(@Param("deviceType") String deviceType);

    List<Hospital> selectHospitalByParentId(@Param("hospitalId") Long hospitalId);

    Device selectDeviceByTypeNumber(@Param("deviceNumber") String deviceNumber);

    List<Device> selectDeviceByHospitalId(@Param("hospitalId") Long hospitalId);

    List<DeviceType> selectDeviceTypeName();

    List<UserLeaseOrder> selectLeaseOrderByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    List<Hospital> selectHospitalByDepartment(@Param("deviceDepartment") String deviceDepartment);

    HospitalUser selectHospitalByHospitalUserName(@Param("hospitalId") Long hospitalId);

    Hospital selectHospitalByHospitalId(@Param("hospitalName")String hospitalName);

    List<Hospital> selectHospitalList();

    List<Device> selectDeviceByHospitalIdLike(@Param("userId") Long userId,@Param("numberOrAddress") String numberOrAddress);

    void insertHospital(Hospital hospital);

    void insertHospitalUser(@Param("userName")String userName,@Param("hospitalId") Long hospitalId);
}
