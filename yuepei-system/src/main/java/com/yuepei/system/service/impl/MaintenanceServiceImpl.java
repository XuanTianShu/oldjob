package com.yuepei.system.service.impl;

import com.yuepei.system.domain.vo.HospitalVO;
import com.yuepei.system.mapper.MaintenanceMapper;
import com.yuepei.system.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceMapper maintenanceMapper;

    @Override
    public List<HospitalVO> getHospital(Long userId) {
        return maintenanceMapper.getHospital(userId);
    }

    @Override
    public List<HospitalVO> getBinding(Long userId) {
        return maintenanceMapper.getBinding(userId);
    }

    @Override
    public int add(HospitalVO hospitalVO) {
        return maintenanceMapper.add(hospitalVO);
    }

    @Override
    public int deleteHospitalByHospitalIds(Long[] ids) {
        return maintenanceMapper.deleteHospitalByHospitalIds(ids);
    }

    @Override
    public int deleteHospitalByHospitalId(Long ids) {
        return maintenanceMapper.deleteHospitalByHospitalId(ids);
    }
}
