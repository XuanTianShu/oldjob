package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zzy
 * @date 2023/3/8 10:25
 */
@Data
public class DeviceManageVo {
    private Long hospitalId;
    private String hospitalName;
    private Integer hospitalSum;
    private Integer deviceSum;
    private Long utilizationRate;
    private BigDecimal deviceAmount;
    private List<DeviceDetailsVo> detailsVos;
    private List<HospitalManagementVo> hospitalManagementVos;
}
