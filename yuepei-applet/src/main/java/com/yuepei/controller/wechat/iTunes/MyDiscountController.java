package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.system.domain.Discount;
import com.yuepei.system.mapper.DiscountMapper;
import com.yuepei.system.service.IDiscountService;
import com.yuepei.system.service.IUserDiscountService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    private DiscountMapper discountMapper;

    @GetMapping("/list")
    public TableDataInfo list(Discount discount)
    {
        startPage();
        List<Discount> list = discountMapper.selectDiscountList(discount);
        return getDataTable(list);
    }
}
