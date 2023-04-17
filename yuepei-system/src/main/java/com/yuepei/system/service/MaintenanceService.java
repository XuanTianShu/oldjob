package com.yuepei.system.service;


import com.yuepei.system.domain.vo.HospitalVO;

import java.util.List;

public interface MaintenanceService {
    List<HospitalVO> getHospital(Long userId);

    List<HospitalVO> getBinding(Long userId);

    int add(HospitalVO hospitalVO);

    int deleteHospitalByHospitalIds(Long[] ids);

    int deleteHospitalByHospitalId(Long ids);
}
