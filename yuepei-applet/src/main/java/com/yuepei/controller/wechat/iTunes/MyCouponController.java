package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.service.MyCouponService;
import com.yuepei.system.domain.Coupon;
import com.yuepei.system.service.CouponService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 我的卡包
 */
@RestController
@RequestMapping("/wechat/user/coupon")
public class MyCouponController {

    @Autowired
    private MyCouponService couponService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/selectCouponList")
    public AjaxResult selectCouponList() {
        return AjaxResult.success(couponService.selectCouponList());
    }

    @PostMapping("/insertUserCoupon")
    public AjaxResult insertUserCoupon(HttpServletRequest request, @RequestBody Coupon coupon) {
        SysUser user = tokenUtils.analysis(request);
        return couponService.insertUserCoupon(user.getUserId(), coupon);
    }

    @GetMapping("/selectUserCoupon")
    public AjaxResult selectUserCoupon(HttpServletRequest request,Integer status){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(couponService.selectUserCoupon(user.getUserId(),status));
    }

}
