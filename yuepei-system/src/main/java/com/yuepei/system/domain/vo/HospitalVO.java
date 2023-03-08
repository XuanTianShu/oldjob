package com.yuepei.system.domain.vo;

import com.yuepei.common.annotation.Excel;
import lombok.Data;

@Data
public class HospitalVO {
    /** 主键 */
    private Long hospitalId;

    /** 主键 */
    private Long parentId;

    /** 名称 */
    @Excel(name = "名称")
    private String hospitalName;
}
