package com.yuepei.controller.wechat.user;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.Discount;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.service.IDiscountService;
import com.yuepei.utils.TokenUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户兑换券
 */
@RestController
@RequestMapping("/wechat/user/jyb")
public class JYBController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private IDiscountService discountService;

    @Autowired
    private SysUserMapper sysUserMapper;


    /**
     * 用户使用积分兑换优惠券
     * @param discountId 优惠券编号
     * @param request
     * @return
     */
    @PostMapping("/assignUser")
    public AjaxResult assignUser(Long discountId, HttpServletRequest request) {
        SysUser user = tokenUtils.analysis(request);
//        SysUser user = sysUserMapper.selectUserByOpenid("oc0od5fazQUUOnkUbxreEkeYopfI");
        return discountService.updateUserIntegral(discountId,user);
    }
}
