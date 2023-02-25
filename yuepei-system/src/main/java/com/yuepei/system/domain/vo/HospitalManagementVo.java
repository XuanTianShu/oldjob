package com.yuepei.system.domain.vo;

import lombok.Data;

/**
 * @author zzy
 * @date 2023/2/20 09:47
 */
@Data
public class HospitalManagementVo {

    /**代理商id*/
    private Long agentId;

    /**医院id*/
    private Long hospitalId;

    /**医院名称*/
    private String hospitalName;

    /**设备数量*/
    private int deviceNum;

    /**设备地址*/
    private String deviceAddress;
}
