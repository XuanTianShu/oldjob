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
 * @date 2023-02-21
 */
public class Discount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 医院id */
    @Excel(name = "医院id")
    private Long hospitalId;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal money;

    /** 门槛 */
    @Excel(name = "门槛")
    private BigDecimal threshold;

    /** 发放数量 */
    @Excel(name = "发放数量")
    private Long sentNum;

    /** 未发数量 */
    @Excel(name = "未发数量")
    private Long unbilledNum;

    /** 类型 */
    @Excel(name = "类型")
    private Long discountType;

    /** 到期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 发放状态 */
    @Excel(name = "发放状态")
    private Long status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setHospitalId(Long hospitalId)
    {
        this.hospitalId = hospitalId;
    }

    public Long getHospitalId()
    {
        return hospitalId;
    }
    public void setMoney(BigDecimal money)
    {
        this.money = money;
    }

    public BigDecimal getMoney()
    {
        return money;
    }
    public void setThreshold(BigDecimal threshold)
    {
        this.threshold = threshold;
    }

    public BigDecimal getThreshold()
    {
        return threshold;
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
    public void setDiscountType(Long discountType)
    {
        this.discountType = discountType;
    }

    public Long getDiscountType()
    {
        return discountType;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("hospitalId", getHospitalId())
                .append("money", getMoney())
                .append("threshold", getThreshold())
                .append("sentNum", getSentNum())
                .append("unbilledNum", getUnbilledNum())
                .append("discountType", getDiscountType())
                .append("createTime", getCreateTime())
                .append("endTime", getEndTime())
                .append("status", getStatus())
                .toString();
    }
}
