package com.yuepei.system.domain.vo;

import lombok.Data;

/**
 * @author zzy
 * @date 2023/3/9 10:47
 */
@Data
public class SubAccountVo {
    /**当前账号id*/
    private Long userId;
    /**用户账号*/
    private String userName;
    /**用户密码*/
    private String password;
    /**手机号*/
    private String number;
    /**所属角色*/
    private String role;
    /**分成比例*/
    private Long proportion;
    /**代理商id*/
    private Long agentId;
    /**代理商名称*/
    private String agentName;
    /**城区*/
    private String area;
    /**详细地址*/
    private String address;
    /**联系人*/
    private String contacts;
}
