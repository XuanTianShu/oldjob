package com.yuepei.system.service.impl;

import com.yuepei.system.mapper.DeviceInvestorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceInvestorServiceImpl {

    @Autowired
    private DeviceInvestorMapper deviceInvestorMapper;
}
