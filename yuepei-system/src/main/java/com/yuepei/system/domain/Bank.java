package com.yuepei.system.domain;

import lombok.Data;

/**
 * @author zzy
 * @date 2023/4/21 14:26
 */
@Data
public class Bank {
    /**主键*/
    private Long id;
    /**用户id*/
    private Long userId;
    /**开户人*/
    private String accountHolder;
    /**银行名称*/
    private String bankName;
    /**银行卡号*/
    private String bankNumber;

}
