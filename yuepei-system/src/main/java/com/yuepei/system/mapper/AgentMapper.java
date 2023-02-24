package com.yuepei.system.mapper;

import com.yuepei.system.domain.Agent;
import com.yuepei.system.domain.AgentHospital;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/17 15:17
 */
public interface AgentMapper {

    List<AgentHospital> selectAgentHospitalByHospital(@Param("agentId") Long agentId);

    Agent selectAgentByAgentId(@Param("userId") Long userId);
}
