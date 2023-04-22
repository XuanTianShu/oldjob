package com.yuepei.system.domain.vo;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class HospitalVO extends BaseEntity {
    /** 主键 */
    private Long hospitalId;

    /** 主键 */
    private Long parentId;

    private String proportion;

    /** 名称 */
    @Excel(name = "名称")
    private String hospitalName;

    private String type;

    private String deviceSum;

    private List<String> departmentList;

    private String userId;
}
