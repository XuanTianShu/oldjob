package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 　　　　 ┏┓       ┏┓+ +
 * 　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　┃　　　　　　 ┃
 * 　　　　┃　　　━　　　┃ ++ + + +
 * 　　　 █████━█████  ┃+
 * 　　　　┃　　　　　　 ┃ +
 * 　　　　┃　　　┻　　　┃
 * 　　　　┃　　　　　　 ┃ + +
 * 　　　　┗━━┓　　　 ┏━┛
 * 　　　　　　┃　　  ┃
 * 　　　　　　┃　　  ┃ + + + +
 * 　　　　　　┃　　　┃　Code is far away from bug with the animal protection
 * 　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码永无bug
 * 　　　　　　┃　　　┃
 * 　　　　　　┃　　　┃　　+
 * 　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　┃ 　　　　　┣-┓
 * 　　　　　　┃ 　　　　　┏-┛
 * 　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 * *********************************************************
 *
 * @author ：AK
 * @create ：2022/11/2 14:15
 **/
@Data
public class SysUserFeedback extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 意见反馈id */
    private Long feedbackId;

    /** 用户id */
    private Long userId;

    /** 用户姓名 */
    private String userName;

    /** 用户手机号 */
    private String phoneNumber;

    /** 设备编号 */
    private String deviceNumber;

    /** 意见反馈类型 */
    private String feedbackType;

    /** 意见反馈内容 */
    private String feedbackInfo;

    /** 意见反馈图片 */
    private String feedbackUrl;

    /** 意见反馈时间 */
    private String feedbackTime;

    /** 意见反馈状态 */
    private Integer status;
}
