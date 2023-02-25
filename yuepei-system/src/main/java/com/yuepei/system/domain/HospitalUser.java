package com.yuepei.system.domain;

import lombok.Data;

/**
 * @author zzy
 * @date 2023/2/24 17:25
 */
@Data
public class HospitalUser {
    /**医院id*/
    private Long hospitalId;
    /**用户账号*/
    private String userName;
}
