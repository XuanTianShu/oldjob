package com.yuepei.system.domain.vo;

import lombok.Data;

/**
 * @author zzy
 * @date 2023/3/9 11:30
 */
@Data
public class SubAccountManageVo {
    private String investorName;
    private String agentName;
    private Integer hospitalSum;
    private Integer deviceSum;
    private Long proportion;
}
