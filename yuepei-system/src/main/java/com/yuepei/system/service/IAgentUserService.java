package com.yuepei.system.service;

import com.yuepei.system.domain.vo.HospitalVO;

import java.util.List;

public interface IAgentUserService {
    List<HospitalVO> getHospital(Long userId);
}
