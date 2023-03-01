package com.yuepei.system.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author zzy
 * @date 2023/2/17 15:22
 */
@Data
public class Agent {

    /**
     * 代理商id
     * */
    private Long agentId;

    /**
     * 代理商名称
     * */
    private String agentName;

    /**主代理账号*/
    private String userName;

    /**
     * 创建时间
     * */
    private Date createTime;

    /**
     * 更新时间
     * */
    private Date updateTime;
}
