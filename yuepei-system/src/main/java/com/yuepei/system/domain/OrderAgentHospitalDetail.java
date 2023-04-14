package com.yuepei.system.domain;

import lombok.Data;

@Data
public class OrderAgentHospitalDetail {
    private String orderNumber;

    private String hospitalId;

    private String userId;

    private String proportion;
}
