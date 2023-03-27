package com.yuepei.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zzy
 * @date 2023/3/2 11:49
 */
@Data
public class DeviceStatisticsVo {
    /**医院id*/
    private Long hospitalId;

    /**医院名称*/
    private String hospitalName;

    /**设备收益*/
    private BigDecimal deviceAmount;

    /**使用率*/
    private String utilizationRate;

    /**设备列表*/
    private List<DeviceDetailsVo> deviceDetailsVoList;


    /**医院id*/
    private Long hospitalId;
}
