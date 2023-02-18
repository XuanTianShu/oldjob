package com.yuepei.utils;

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
    ORDER_STATUS_2(2, "已完成");

    private int code;

    private String name;

    DictionaryEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
