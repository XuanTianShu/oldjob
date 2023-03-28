package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 康养对象 health_problem
 *
 * @author AK
 * @date 2023-03-24
 */
public class HealthProblem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long healthProblemId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String healthProblemTitle;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String healthProblemSolution;

    public void setHealthProblemId(Long healthProblemId)
    {
        this.healthProblemId = healthProblemId;
    }

    public Long getHealthProblemId()
    {
        return healthProblemId;
    }
    public void setHealthProblemTitle(String healthProblemTitle)
    {
        this.healthProblemTitle = healthProblemTitle;
    }

    public String getHealthProblemTitle()
    {
        return healthProblemTitle;
    }
    public void setHealthProblemSolution(String healthProblemSolution)
    {
        this.healthProblemSolution = healthProblemSolution;
    }

    public String getHealthProblemSolution()
    {
        return healthProblemSolution;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("healthProblemId", getHealthProblemId())
                .append("healthProblemTitle", getHealthProblemTitle())
                .append("healthProblemSolution", getHealthProblemSolution())
                .toString();
    }
}
