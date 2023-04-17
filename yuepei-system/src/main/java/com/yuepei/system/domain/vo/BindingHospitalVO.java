package com.yuepei.system.domain.vo;

import lombok.Data;

@Data
public class BindingHospitalVO {
    private Long id;

    private String deviceNumber;

    private String type;

    private String hospitalId;

    private String proportion;
}
