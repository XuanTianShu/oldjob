package com.yuepei.system.domain.vo;

import com.yuepei.common.core.domain.BaseEntity;
import com.yuepei.system.domain.DeviceRule;
import com.yuepei.system.domain.Hospital;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DeviceVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long deviceId;

    private Long deviceTypeId;

    private String deviceNumber;

    private String deviceIotNumber;

    private String deviceMac;

    private BigDecimal deviceDeposit;

    private Long billingId;

    private Long hospitalId;

    private String deviceQRCode;

    private String deviceAddress;

    private String deviceFullAddress;

    private String rows;

    private String heartTime;

    private Long status;

    private String rule;

    private String depict;

    private List<HospitalRuleVO> deviceRules;
}
