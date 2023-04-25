package com.yuepei.system.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.yuepei.common.annotation.Excel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zzy
 * @date 2023/4/22 17:21
 */
@Data
public class Withdrawal {
    private Long id;
    @Excel(name = "订单编号")
    private String orderNumber;
    @Excel(name = "用户id")
    private Long userId;
    /**角色类别*/
    @Excel(name = "角色类别", readConverterExp = "03=投资用户,04=医院用户,05=代理用户")
    private String role;
    /**角色名称 */
    @Excel(name = "角色昵称")
    private String roleName;
    /**金额*/
    @Excel(name = "提现金额")
    private BigDecimal amount;
    /**提现信息*/
    @Excel(name = "提现信息")
    private String withdrawalInformation;
    /**到账（微信\银行卡）*/
    @Excel(name = "到账地址", readConverterExp = "0=微信,1=银行卡,2=支付宝")
    private Long received;
    /**状态*/
    @Excel(name = "提现状态", readConverterExp = "0=申请中,1=已通过,2=未通过,3=已打款")
    private Long status;
    /**申请时间*/
    @Excel(name = "申请时间")
    private Date applyTime;
    /**处理时间*/
    @Excel(name = "处理时间")
    private Date handleTime;
    /**余额*/
    private BigDecimal balance;
}
