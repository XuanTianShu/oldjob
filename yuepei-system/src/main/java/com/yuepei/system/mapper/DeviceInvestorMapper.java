package com.yuepei.system.mapper;

import org.apache.ibatis.annotations.Param;

public interface DeviceInvestorMapper {
    int delByInvestorId(@Param("toArray") Long[] toArray, @Param("deviceNumber") String deviceNumber);

    int insert(@Param("toArray") Long[] toArray, @Param("deviceNumber") String deviceNumber);
}
