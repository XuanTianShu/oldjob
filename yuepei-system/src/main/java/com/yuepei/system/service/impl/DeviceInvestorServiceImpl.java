package com.yuepei.system.service.impl;

import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.vo.DeviceInvestorVO;
import com.yuepei.system.mapper.DeviceInvestorMapper;
import com.yuepei.system.service.DeviceInvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceInvestorServiceImpl implements DeviceInvestorService {

    @Autowired
    private DeviceInvestorMapper deviceInvestorMapper;

    @Override
    public List<DeviceInvestor> deviceProportionList(DeviceInvestor deviceInvestor) {
        return deviceInvestorMapper.deviceProportionList(deviceInvestor);
    }

    @Override
    public List<DeviceInvestorVO> deviceByInvestorId(Long investorId) {
        return deviceInvestorMapper.deviceByInvestorId(investorId);
    }
}
