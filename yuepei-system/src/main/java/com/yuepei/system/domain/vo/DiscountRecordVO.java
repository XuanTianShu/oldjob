package com.yuepei.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DiscountRecordVO extends BaseEntity {

    /** 发放人名称 */
    @Excel(name = "发放人名称")
    private String sNickName;

    /** 归属医院 */
    @Excel(name = "归属医院")
    private String hospitalName;

    /** 积分值 */
    @Excel(name = "积分值")
    private Long integral;

    /** 1兑换券2优惠券 */
    @Excel(name = "1兑换券2优惠券")
    private Long isJyb;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String uNickName;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal price;

    /** 状态 */
    @Excel(name = "门槛")
    private String threshold;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 发放时间 */
    @Excel(name = "发放时间")
    private Date grantTime;
}
