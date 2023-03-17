package com.yuepei.system.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zzy
 * @date 2023/3/16 17:07
 */
@Data
public class FeedbackInfoVo {

    /** 意见反馈id */
    private Long feedbackId;

    /** 设备编号 */
    private String deviceNumber;

    /**设备地址*/
    private String deviceAddress;

    /** 意见反馈类型 */
    private String feedbackType;

    /** 意见反馈内容 */
    private String feedbackInfo;

    /** 意见反馈图片 */
    private List<String> feedbackUrl;

    /** 意见反馈时间 */
    private String feedbackTime;

    /** 意见反馈状态 */
    private Integer status;
}
