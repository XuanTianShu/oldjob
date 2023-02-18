package com.yuepei.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

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
 * @create ：2022/11/21 17:49
 **/
@Data
public class NoteInfoLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 短信日志id */
    private Long noteLogId;

    /** 短信日志标题 */
    private String noteLogTitle;

    /** 发送手机号 */
    private String noteLogPhone;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /** 发送状态 */
    private Integer sendStatus;

    /** 回执状态 */
    private String receiptStatus;

    /** 回执时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiptTime;

    /** 短信日志方法 */
    private String requestMethod;

    /** 短信日志操作用户名称 */
    private String noteLogName;

    /** 短信日志操作用户角色 */
    private String noteLogRole;

    /** 请求路径 */
    private String noteLogUrl;

    /** 请求路径 */
    private String noteLogIp;

    /** 错误消息 */
    private String errorMsg;
}
