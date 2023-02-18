package com.yuepei.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
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
 * @create ：2022/12/15 14:58
 **/
@Data
public class Coupon {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 优惠券标题 */
    private String title;

    /** 使用限额 */
    private Integer useLimit;

    /** 优惠金额 */
    private Integer discountAmount;

    /** 优惠券价格 */
    private Integer price;

    /** 优惠券有效时间 */
    private Integer validityDays;

    /** 优惠券描述 */
    private String describe;

    /** 优惠券数量 */
    private int sum;

    /** 领取或发放数量 */
    private int inSum;

    /** 优惠券类型-  0 - 新人优惠券   1-积分优惠券 */
    private String couponType;

    /** 优惠券创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date couponCreateTime;
}
