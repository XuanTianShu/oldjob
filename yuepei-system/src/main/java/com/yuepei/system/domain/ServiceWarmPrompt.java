package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 温馨提示对象 service_warm_prompt
 *
 * @author ohy
 * @date 2023-03-18
 */
public class ServiceWarmPrompt extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 温馨提示编号 */
    private Long serviceWarmPromptId;

    /** 温馨提示内容 */
    @Excel(name = "温馨提示内容")
    private String serviceWarmPromptInfo;

    public void setServiceWarmPromptId(Long serviceWarmPromptId)
    {
        this.serviceWarmPromptId = serviceWarmPromptId;
    }

    public Long getServiceWarmPromptId()
    {
        return serviceWarmPromptId;
    }
    public void setServiceWarmPromptInfo(String serviceWarmPromptInfo)
    {
        this.serviceWarmPromptInfo = serviceWarmPromptInfo;
    }

    public String getServiceWarmPromptInfo()
    {
        return serviceWarmPromptInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("serviceWarmPromptId", getServiceWarmPromptId())
                .append("serviceWarmPromptInfo", getServiceWarmPromptInfo())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
