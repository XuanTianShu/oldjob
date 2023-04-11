package com.yuepei.system.mapper;

import com.yuepei.system.domain.DeviceHospital;
import org.apache.ibatis.annotations.Param;

public interface DeviceHospitalMapper {
    void insert(DeviceHospital deviceHospital);

    void del(@Param("deviceNumber") String deviceNumber);
}
