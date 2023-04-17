package com.yuepei.system.mapper;

import com.yuepei.system.domain.vo.HospitalVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaintenanceMapper {
    List<HospitalVO> getHospital(@Param("userId") Long userId);

    List<HospitalVO> getBinding(@Param("userId") Long userId);

    int add(HospitalVO hospitalVO);

    int deleteHospitalByHospitalIds(Long[] ids);

    int deleteHospitalByHospitalId(@Param("ids") Long ids);
}
