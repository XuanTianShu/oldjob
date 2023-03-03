package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.mapper.UserInsuranceMapper;
import com.yuepei.system.domain.UserInsurance;
import com.yuepei.system.service.IUserDiscountService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wechat/user/discount")
public class MyDiscountController extends BaseController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private IUserDiscountService userDiscountService;

    /**
     * 查询用户的卡包
     * @param request
     * @return
     */
    @PostMapping("/selectMyDiscountByOpenId")
    public TableDataInfo selectMyDiscountByOpenId(HttpServletRequest request,String openId){
        startPage();
//        SysUser user = tokenUtils.analysis(request);
        return getDataTable(userDiscountService.selectMyDiscountByOpenId(openId));
    }
}
