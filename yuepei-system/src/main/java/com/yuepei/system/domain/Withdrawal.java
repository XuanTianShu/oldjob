package com.yuepei.system.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zzy
 * @date 2023/4/22 17:21
 */
@Data
public class Withdrawal {
    private Long id;
    private Long userId;
    /**角色类别*/
    private String role;
    /**角色名称 */
    private String roleName;
    /**金额*/
    private BigDecimal amount;
    /**提现信息*/
    private String withdrawalInformation;
    /**到账（微信\银行卡）*/
    private Long received;
    /**状态*/
    private Long status;
    /**申请时间*/
    private Date applyTime;
    /**处理时间*/
    private Date handleTime;
}
