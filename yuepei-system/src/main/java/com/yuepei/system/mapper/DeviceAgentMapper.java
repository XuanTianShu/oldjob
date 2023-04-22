package com.yuepei.system.mapper;

import com.yuepei.system.domain.DeviceAgent;
import com.yuepei.system.domain.vo.PersonnelProportionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceAgentMapper {
    DeviceAgent selectAgentListByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    List<DeviceAgent> selectAgentAccountList(@Param("deviceNumber") String deviceNumber);

    void insert(@Param("deviceNumber") String deviceNumber, @Param("userId") Long userId, @Param("agentProportion") String agentProportion);

    void del(@Param("deviceNumber") String deviceNumber);

    void deleteByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    void deleteByDeviceNumbers(List<String> list);

    void updateAgent(PersonnelProportionVO personnelProportionVO);
}
