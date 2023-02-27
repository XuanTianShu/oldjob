package com.yuepei.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券对象 discount
 *
 * @author ohy
 * @date 2023-02-27
 */
public class Discount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 优惠券名称 */
    @Excel(name = "优惠券名称")
    private String discountName;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal money;

    /** 门槛编号 */
    @Excel(name = "门槛编号")
    private Long thresholdId;

    /** 有效期(代表天数) */
    @Excel(name = "有效期(代表天数)")
    private Long period;

    /** 状态(0正常1禁用) */
    @Excel(name = "状态(0正常1禁用)")
    private Long status;

    /** 发放数量 */
    @Excel(name = "发放数量")
    private Long sentNum;

    /** 未发放数量 */
    @Excel(name = "未发放数量")
    private Long unbilledNum;

    /** 是否为新人优惠券 */
    @Excel(name = "是否为新人优惠券")
    private Long isStatus;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDiscountName(String discountName)
    {
        this.discountName = discountName;
    }

    public String getDiscountName()
    {
        return discountName;
    }
    public void setMoney(BigDecimal money)
    {
        this.money = money;
    }

    public BigDecimal getMoney()
    {
        return money;
    }
    public void setThresholdId(Long thresholdId)
    {
        this.thresholdId = thresholdId;
    }

    public Long getThresholdId()
    {
        return thresholdId;
    }
    public void setPeriod(Long period)
    {
        this.period = period;
    }

    public Long getPeriod()
    {
        return period;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setSentNum(Long sentNum)
    {
        this.sentNum = sentNum;
    }

    public Long getSentNum()
    {
        return sentNum;
    }
    public void setUnbilledNum(Long unbilledNum)
    {
        this.unbilledNum = unbilledNum;
    }

    public Long getUnbilledNum()
    {
        return unbilledNum;
    }
    public void setIsStatus(Long isStatus)
    {
        this.isStatus = isStatus;
    }

    public Long getIsStatus()
    {
        return isStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("discountName", getDiscountName())
                .append("money", getMoney())
                .append("thresholdId", getThresholdId())
                .append("period", getPeriod())
                .append("status", getStatus())
                .append("sentNum", getSentNum())
                .append("unbilledNum", getUnbilledNum())
                .append("isStatus", getIsStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
