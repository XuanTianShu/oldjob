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
 * 优惠券发放记录对象 discount_record
 *
 * @author ohy
 * @date 2023-03-01
 */
public class DiscountRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 发放人 */
    @Excel(name = "发放人")
    private Long issuer;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userid;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal price;

    /** 门槛 */
    @Excel(name = "门槛")
    private String threshold;

    /** 是否为新人优惠券 */
    @Excel(name = "是否为新人优惠券")
    private Long status;

    /** 积分值 */
    @Excel(name = "积分值")
    private Long integral;

    /** 医院名称 */
    @Excel(name = "医院名称")
    private String hospitalname;

    /** 1兑换券2优惠券 */
    @Excel(name = "1兑换券2优惠券")
    private Long isJyb;

    /** 发放时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发放时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date grantTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setIssuer(Long issuer)
    {
        this.issuer = issuer;
    }

    public Long getIssuer()
    {
        return issuer;
    }
    public void setUserid(Long userid)
    {
        this.userid = userid;
    }

    public Long getUserid()
    {
        return userid;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
    public void setThreshold(String threshold)
    {
        this.threshold = threshold;
    }

    public String getThreshold()
    {
        return threshold;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setIntegral(Long integral)
    {
        this.integral = integral;
    }

    public Long getIntegral()
    {
        return integral;
    }
    public void setHospitalname(String hospitalname)
    {
        this.hospitalname = hospitalname;
    }

    public String getHospitalname()
    {
        return hospitalname;
    }
    public void setIsJyb(Long isJyb)
    {
        this.isJyb = isJyb;
    }

    public Long getIsJyb()
    {
        return isJyb;
    }
    public void setGrantTime(Date grantTime)
    {
        this.grantTime = grantTime;
    }

    public Date getGrantTime()
    {
        return grantTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("issuer", getIssuer())
                .append("userid", getUserid())
                .append("price", getPrice())
                .append("threshold", getThreshold())
                .append("status", getStatus())
                .append("integral", getIntegral())
                .append("hospitalname", getHospitalname())
                .append("isJyb", getIsJyb())
                .append("grantTime", getGrantTime())
                .toString();
    }
}
