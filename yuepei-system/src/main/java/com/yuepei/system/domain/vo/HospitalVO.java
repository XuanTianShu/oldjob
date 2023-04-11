package com.yuepei.system.domain.vo;

import com.yuepei.common.annotation.Excel;
import lombok.Data;

import java.util.List;

@Data
public class HospitalVO {
    /** 主键 */
    private Long hospitalId;

    /** 主键 */
    private Long parentId;

    private String proportion;

    /** 名称 */
    @Excel(name = "名称")
    private String hospitalName;

    private List<String> departmentList;
}
