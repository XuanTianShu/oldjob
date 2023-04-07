package com.yuepei.system.mapper;

import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.vo.DeviceInvestorVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceInvestorMapper {
    int delByInvestorId( @Param("deviceNumber") String deviceNumber);

    int insert(@Param("toArray") Long[] toArray, @Param("deviceNumber") String deviceNumber);

    List<DeviceInvestor> deviceProportionList(DeviceInvestor deviceInvestor);

    List<DeviceInvestorVO> deviceByInvestorId(@Param("investorId") Long investorId);

    void deleteByInvestorIds(List<String> list);

    void deleteByInvestorId(@Param("deviceNumber") String deviceNumber);

    DeviceInvestor getDeviceById(@Param("id") Long id);
}
