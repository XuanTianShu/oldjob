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

    AgentHospital selectAgentHospital(@Param("hospitalId") Long hospitalId);

    void insertAgentHospital(@Param("agentId") Long agentId,@Param("hospitalId") Long hospitalId);

    void insertHospitalUser(@Param("hospitalId") Long hospitalId,@Param("accountNumber") String accountNumber);

    Agent selectAgentByUserName(@Param("userName") String userName);

    AgentHospital selectAgentByHospitalId(@Param("hospitalId") Long hospitalId);

    Agent selectAgent(@Param("agentId") Long agentId);

    void insertAgent(Agent agent);
}
