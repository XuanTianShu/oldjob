package com.yuepei.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 发放记录对象 discount_record
 *
 * @author ou
 * @date 2023-02-24
 */
@Data
public class DiscountRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 优惠卷id */
    @Excel(name = "优惠卷id")
    private Long discountId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userid;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String discountTypeName;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal price;

    /** 门槛 */
    @Excel(name = "门槛")
    private String threshold;

    /** 发放时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发放时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date grantTime;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String userName;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("discountId",getDiscountId())
                .append("userid", getUserid())
                .append("discountTypeName", getDiscountTypeName())
                .append("price", getPrice())
                .append("userName",getUserName())
                .append("threshold", getThreshold())
                .append("grantTime", getGrantTime())
                .toString();
    }
}
