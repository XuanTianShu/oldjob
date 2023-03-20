package com.yuepei.system.domain.vo;

import com.yuepei.system.domain.DeviceType;
import lombok.Data;

import java.util.List;

/**
 * @author zzy
 * @date 2023/3/9 15:45
 */
@Data
public class AgentDeviceVo {
    private Integer deviceSum;
    private Integer hospitalSum;
    private Integer useDeviceSum;

    private List<DeviceType> deviceTypes;
}
