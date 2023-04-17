package com.yuepei.system.service;


import com.yuepei.system.domain.vo.HospitalVO;

import java.util.List;

public interface MaintenanceService {
    List<HospitalVO> getHospital(Long userId);

    List<HospitalVO> getBinding(Long userId);
}
