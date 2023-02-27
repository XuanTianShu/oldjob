package com.yuepei.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户卡包对象 user_discount
 *
 * @author ohy
 * @date 2023-02-27
 */
public class UserDiscount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userId;

    /** 门槛名称 */
    @Excel(name = "门槛名称")
    private String thresholdName;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 满减金额 */
    @Excel(name = "满减金额")
    private Long full;

    /** 到期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 状态 0待使用1已使用2已过期 */
    @Excel(name = "状态 0待使用1已使用2已过期")
    private Long status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setThresholdName(String thresholdName)
    {
        this.thresholdName = thresholdName;
    }

    public String getThresholdName()
    {
        return thresholdName;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
    public void setFull(Long full)
    {
        this.full = full;
    }

    public Long getFull()
    {
        return full;
    }
    public void setExpireTime(Date expireTime)
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime()
    {
        return expireTime;
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
                .append("userId", getUserId())
                .append("thresholdName", getThresholdName())
                .append("price", getPrice())
                .append("full", getFull())
                .append("expireTime", getExpireTime())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
