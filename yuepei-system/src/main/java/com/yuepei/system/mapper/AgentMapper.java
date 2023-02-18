package com.yuepei.system.mapper;

import com.yuepei.system.domain.Agent;
import org.apache.ibatis.annotations.Param;

/**
 * @author zzy
 * @date 2023/2/17 15:17
 */
public interface AgentMapper {

    Agent selectAgentByAgentId(@Param("userId") Long userId);
}
