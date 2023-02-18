package com.yuepei.system.domain;

import com.yuepei.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class CabinetSwitch extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //开关主键
    private Long switchId;

    //用户主键
    private Long userId;

    //医院主键
    private Long hospitalId;

    //设备编号
    private String deviceNumber;

    //开关锁号
    private String cabinetNumber;

    //设备类型 1 陪护床（床头柜）2 折叠椅 3 2折床（一拖五）4 轮椅（一拖五）5 商品
    private Integer deviceType;

    //操作类型 1开锁  2关锁
    private Integer switchType;
}
