package com.yuepei.system.domain.pojo;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import com.yuepei.common.core.domain.entity.SysUser;
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
 * @create ：2022/11/2 17:30
 **/
@Data
public class SysUserFeedbackPo extends BaseEntity {

    /** 意见反馈id */
    private Long feedbackId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;


    /** 意见反馈内容 */
    @Excel(name = "意见反馈内容")
    private String feedbackInfo;

    /** 意见反馈图片 */
    @Excel(name = "意见反馈图片")
    private String feedbackUrl;

    /** 意见反馈时间 */
    @Excel(name = "意见反馈时间")
    private String feedbackTime;

    /** 意见反馈状态 */
    @Excel(name = "意见反馈状态")
    private Long status;

    /** 用户对象 */
    private SysUser sysUser;
}
