package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 分成比例对象 proportion
 *
 * @author ohy
 * @date 2023-02-28
 */
public class Proportion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    private Long id;

    /** 医院分成比例 */
    @Excel(name = "医院分成比例")
    private Long proportionHospital;

    /** 平台分成比例 */
    @Excel(name = "平台分成比例")
    private Long proportionPlatform;

    /** 代理商分成比例 */
    @Excel(name = "代理商分成比例")
    private Long proportionAgent;

    /** 投资人分成比例 */
    @Excel(name = "投资人分成比例")
    private Long proportionInvestor;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setProportionHospital(Long proportionHospital)
    {
        this.proportionHospital = proportionHospital;
    }

    public Long getProportionHospital()
    {
        return proportionHospital;
    }
    public void setProportionPlatform(Long proportionPlatform)
    {
        this.proportionPlatform = proportionPlatform;
    }

    public Long getProportionPlatform()
    {
        return proportionPlatform;
    }
    public void setProportionAgent(Long proportionAgent)
    {
        this.proportionAgent = proportionAgent;
    }

    public Long getProportionAgent()
    {
        return proportionAgent;
    }
    public void setProportionInvestor(Long proportionInvestor)
    {
        this.proportionInvestor = proportionInvestor;
    }

    public Long getProportionInvestor()
    {
        return proportionInvestor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("proportionHospital", getProportionHospital())
                .append("proportionPlatform", getProportionPlatform())
                .append("proportionAgent", getProportionAgent())
                .append("proportionInvestor", getProportionInvestor())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
