package com.yuepei.system.service;

import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.vo.DeviceInvestorVO;

import java.util.List;

public interface DeviceInvestorService {
    List<DeviceInvestor> deviceProportionList(DeviceInvestor deviceInvestor);

    List<DeviceInvestorVO> deviceByInvestorId(Long investorId);

    DeviceInvestor getDeviceById(Long id);
}
