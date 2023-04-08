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

    List<Device> selectDeviceTypeDetails(@Param("deviceTypeId") Long deviceTypeId, @Param("hospitalId")Long hospitalId);

    Hospital selectHospitalByHospitalName(@Param("hospitalId") Long hospitalId);

    void updateDeviceDetails(@Param("deviceNumber") String deviceNumber,@Param("deviceFullAddress") String deviceFullAddress);

    List<GoodsOrder> selectGoodsOrder(@Param("userId") Long userId);

    GoodsOrder selectOrderByOrderId(@Param("orderId") Long orderId);

    DeviceType selectDeviceByTypeName(@Param("deviceTypeId") Long deviceTypeId);

    Goods selectGoodsByGoodsName(@Param("goodsId") Long goodsId);

    List<Device> selectDeviceNumber(@Param("userId") Long userId);

    DeviceType selectDeviceByType(@Param("deviceType") String deviceType);

    List<Hospital> selectHospitalByParentId(@Param("hospitalId") Long hospitalId);

    Device selectDeviceByTypeNumber(@Param("deviceNumber") String deviceNumber);

    List<Device> selectDeviceByHospitalId(@Param("hospitalId") Long hospitalId);

    List<DeviceType> selectDeviceTypeName();

    List<UserLeaseOrder> selectLeaseOrderByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    List<Hospital> selectHospitalByDepartment(@Param("deviceDepartment") String deviceDepartment);

    Hospital selectHospitalByHospitalId(@Param("hospitalName")String hospitalName);

    List<Hospital> selectHospitalList();

    List<Device> selectDeviceByHospitalIdLike(@Param("userId") Long userId,@Param("numberOrAddress") String numberOrAddress);

    void insertHospital(Hospital hospital);

    List<String> selectDeviceNumberByHospitalId(@Param("hospitalId")Long hospitalId);

    List<Device> selectInvestorId(@Param("userId") Long userId);
}
