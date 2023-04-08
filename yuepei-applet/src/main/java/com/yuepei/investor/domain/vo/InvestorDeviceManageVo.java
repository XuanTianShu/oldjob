package com.yuepei.investor.domain.vo;

import com.yuepei.system.domain.vo.DeviceDetailsVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zzy
 * @date 2023/4/7 09:58
 */
@Data
public class InvestorDeviceManageVo {
    //使用率
    private Long utilizationRate;
    //设备收益
    private BigDecimal deviceAmount;
    //设备数量
    private Integer deviceSum;
    //设备信息
    private List<DeviceDetailsVo> DeviceDetails;
}
