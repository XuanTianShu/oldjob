package com.yuepei.system.domain;

import lombok.Data;

@Data
public class DeviceAgent {
    private Long id;

    private String deviceNumber;

    private String agentId;

    private String proportion;

    private String parentId;
}
