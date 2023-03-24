package com.yuepei.system.mapper;

import com.yuepei.system.domain.AgentHospital;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/17 15:17
 */
public interface AgentMapper {

    List<AgentHospital> selectAgentHospitalByHospital(@Param("userId") Long userId);

    AgentHospital selectAgentHospital(@Param("hospitalId") Long hospitalId);

    void insertAgentHospital(@Param("agentId") Long agentId,@Param("hospitalId") Long hospitalId);

    AgentHospital selectAgentByHospitalId(@Param("hospitalId") Long hospitalId);
}
