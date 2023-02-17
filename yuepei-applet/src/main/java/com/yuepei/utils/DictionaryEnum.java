package com.yuepei.utils;

import com.yuepei.common.config.YuePeiConfig;
import lombok.Getter;

/**
 * Description:
 * date: 2023/2/4 17:50
 * 数据字段枚举
 * @author: ohy
 * @since JDK 1.8
 */
@Getter
public enum DictionaryEnum {
    ORDER_STATUS_2(2, "已完成"),
    CHECK_TYPE(100,"已绑定无法删除！"),
    LEASE_ORDER(101,"存在进行中或待支付的订单，无法退押金！"),
    CHECK_DEVICE_NUMBER(102,"该设备或MAC地址存在！"),
    CHECK_GOODS_TYPE(103,"该商品类型存在！"),
    CHECK_GOODS_TYPE_ID(104,"该商品类型已绑定无法删除！");


    private int code;

    private String name;

    DictionaryEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
