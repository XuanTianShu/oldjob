package com.yuepei.system.service.impl;

import com.yuepei.system.domain.vo.HospitalVO;
import com.yuepei.system.mapper.IAgentUserMapper;
import com.yuepei.system.service.IAgentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IAgentUserServiceImpl implements IAgentUserService {

    @Autowired
    private IAgentUserMapper agentUserMapper;

    @Override
    public List<HospitalVO> getHospital(Long userId) {
        return agentUserMapper.getHospital(userId);
    }
}
