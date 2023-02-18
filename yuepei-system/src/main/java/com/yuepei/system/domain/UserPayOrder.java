package com.yuepei.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @create ：2023/1/5 15:04
 **/
@Data
public class UserPayOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 用户唯一标识 */
    private String openid;

    /** 支付订单号 */
    private String outTradeNo;

    /** 订单金额 --单位 分 */
    private Long price;

    /** 支付完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 支付状态 -- 0-待支付 1-已支付 2-支付失败  */
    private Integer status;

    /** 错误描述 */
    private String errMsg;
}
