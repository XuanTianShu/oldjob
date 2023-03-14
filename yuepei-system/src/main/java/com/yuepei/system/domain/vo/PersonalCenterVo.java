package com.yuepei.system.domain.vo;

import com.yuepei.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zzy
 * @date 2023/3/13 15:04
 */
@Data
public class PersonalCenterVo {

    /** 用户昵称 */
    private String userName;

    /** 用户头像 */
    private String avatar;

    /**用户资金*/
    private BigDecimal amount;
}
