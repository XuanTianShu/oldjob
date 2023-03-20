package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.system.service.IUserDiscountService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/wechat/user/discount")
public class MyDiscountController extends BaseController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private IUserDiscountService userDiscountService;
}
