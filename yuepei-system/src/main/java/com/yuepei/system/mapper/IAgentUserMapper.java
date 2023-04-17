package com.yuepei.system.mapper;

import com.yuepei.system.domain.vo.HospitalVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IAgentUserMapper {
    List<HospitalVO> getHospital(@Param("userId") Long userId);

    void deleteHospitalByHospitalIds(Long[] hospitalIds);

    void deleteHospitalByHospitalId(@Param("hospitalId") Long hospitalId);

    int insert(@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);
}
