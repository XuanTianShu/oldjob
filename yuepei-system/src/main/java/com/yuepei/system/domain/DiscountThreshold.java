package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 门槛类型对象 discount_threshold
 *
 * @author ohy
 * @date 2023-02-24
 */
public class DiscountThreshold extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 门槛名称 */
    @Excel(name = "门槛名称")
    private String thresholdName;

    /** 满 */
    @Excel(name = "满")
    private Long full;

    /** 减 */
    @Excel(name = "减")
    private Long subtract;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setThresholdName(String thresholdName)
    {
        this.thresholdName = thresholdName;
    }

    public String getThresholdName()
    {
        return thresholdName;
    }
    public void setFull(Long full)
    {
        this.full = full;
    }

    public Long getFull()
    {
        return full;
    }
    public void setSubtract(Long subtract)
    {
        this.subtract = subtract;
    }

    public Long getSubtract()
    {
        return subtract;
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
                .append("thresholdName", getThresholdName())
                .append("full", getFull())
                .append("subtract", getSubtract())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
