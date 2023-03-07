package com.yuepei.system.domain.vo;

import lombok.Data;

/**
 * @author zzy
 * @date 2023/3/2 17:06
 */
@Data
public class HospitalVo {
    private Long hospitalId;

    private String hospitalName;

    private Long parentId;
}
