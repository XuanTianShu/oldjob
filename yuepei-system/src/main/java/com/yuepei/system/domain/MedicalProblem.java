package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 医疗对象 medical_problem
 *
 * @author AK
 * @date 2023-03-24
 */
public class MedicalProblem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long medicalProblemId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String medicalProblemTitle;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String medicalProblemSolution;

    public void setMedicalProblemId(Long medicalProblemId)
    {
        this.medicalProblemId = medicalProblemId;
    }

    public Long getMedicalProblemId()
    {
        return medicalProblemId;
    }
    public void setMedicalProblemTitle(String medicalProblemTitle)
    {
        this.medicalProblemTitle = medicalProblemTitle;
    }

    public String getMedicalProblemTitle()
    {
        return medicalProblemTitle;
    }
    public void setMedicalProblemSolution(String medicalProblemSolution)
    {
        this.medicalProblemSolution = medicalProblemSolution;
    }

    public String getMedicalProblemSolution()
    {
        return medicalProblemSolution;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("medicalProblemId", getMedicalProblemId())
                .append("medicalProblemTitle", getMedicalProblemTitle())
                .append("medicalProblemSolution", getMedicalProblemSolution())
                .toString();
    }
}
