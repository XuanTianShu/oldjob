package com.yuepei.system.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/20 11:27
 */
@Data
public class HospitalAgentVo {
    /**
     * 代理商id
     * */
    private Long agentId;
    /**医院id*/
    private Long hospitalId;
    /**医院名称*/
    private String hospitalName;
    /**医院账号*/
    private String accountNumber;
    /**医院密码*/
    private String password;
    /**医院地址*/
    private String hospitalAddress;
    /**联系人*/
    private String contacts;
    /**分成比例*/
    private String divided;
    /**划分名下设备*/
    private List<DeviceInfoVo> deviceDetails;
}
