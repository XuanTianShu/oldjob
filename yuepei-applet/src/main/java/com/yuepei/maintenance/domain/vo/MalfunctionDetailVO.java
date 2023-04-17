package com.yuepei.maintenance.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MalfunctionDetailVO {
    private String feedbackId;

    private String deviceNumber;

    private String deviceAddress;

    private String feedbackType;

    private String status;

    private String feedbackInfo;

    private String feedbackUrl;

    private Date createTime;
}
