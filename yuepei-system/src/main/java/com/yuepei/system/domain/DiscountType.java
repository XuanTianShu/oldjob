package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 优惠券类型对象 discount_type
 *
 * @author ohy
 * @date 2023-02-22
 */
public class DiscountType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String discountTypeName;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;
    private String discountName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDiscountTypeName(String discountTypeName)
    {
        this.discountTypeName = discountTypeName;
    }

    public String getDiscountTypeName()
    {
        return discountTypeName;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus() {
        return status;
    }
    public void setDiscountName(String discountName)
    {
        this.discountName = discountName;
    }

    public String getDiscountName()
    {
        return discountName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("discountTypeName", getDiscountTypeName())
                .append("status", getStatus())
                .append("discountName", getDiscountName())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
