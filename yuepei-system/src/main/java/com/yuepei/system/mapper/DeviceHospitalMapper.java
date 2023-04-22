package com.yuepei.system.mapper;

import com.yuepei.system.domain.DeviceHospital;
import com.yuepei.system.domain.vo.PersonnelProportionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceHospitalMapper {
    void insert(DeviceHospital deviceHospital);

    void del(@Param("deviceNumber") String deviceNumber);

    DeviceHospital selectHospitalListByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    void deleteByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    void deleteByDeviceNumbers(List<String> list);

    void updateHospital(PersonnelProportionVO personnelProportionVO);
}
