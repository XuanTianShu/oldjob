package com.yuepei.system.mapper;

import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.UserLeaseOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/17 15:17
 */
public interface AgentMapper {
    List<UserLeaseOrder> selectUserLeaseOrderByDevices(@Param("deviceNumber") List<String> deviceNumbers,@Param("agentId") String agentId);

    Hospital selectHospitalByHospitalId(@Param("hospitalName")String hospitalName);

    List<Hospital> selectHospitalList();

    List<Device> selectDeviceByHospitalIdLike(@Param("userId") Long userId,@Param("numberOrAddress") String numberOrAddress);

    void insertHospital(Hospital hospital);
}
