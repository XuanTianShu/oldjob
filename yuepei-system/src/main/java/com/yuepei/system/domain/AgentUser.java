package com.yuepei.system.domain;

import lombok.Data;

/**
 * @author zzy
 * @date 2023/2/17 15:22
 */
@Data
public class AgentUser {

    /**
     * 代理商id
     * */
    private Long agentId;

    /**
     * 用户id
     * */
    private Long userId;
}
