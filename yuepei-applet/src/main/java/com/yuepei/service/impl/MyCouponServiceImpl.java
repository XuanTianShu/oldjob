package com.yuepei.service.impl;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.service.MyCouponService;
import com.yuepei.system.domain.Coupon;
import com.yuepei.system.domain.UserCoupon;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.mapper.CouponMapper;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.mapper.UserCouponMapper;
import com.yuepei.system.mapper.UserIntegralOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 　　　　 ┏┓       ┏┓+ +
 * 　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　┃　　　　　　 ┃
 * 　　　　┃　　　━　　　┃ ++ + + +
 * 　　　 █████━█████  ┃+
 * 　　　　┃　　　　　　 ┃ +
 * 　　　　┃　　　┻　　　┃
 * 　　　　┃　　　　　　 ┃ + +
 * 　　　　┗━━┓　　　 ┏━┛
 * 　　　　　　┃　　  ┃
 * 　　　　　　┃　　  ┃ + + + +
 * 　　　　　　┃　　　┃　Code is far away from bug with the animal protection
 * 　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码永无bug
 * 　　　　　　┃　　　┃
 * 　　　　　　┃　　　┃　　+
 * 　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　┃ 　　　　　┣-┓
 * 　　　　　　┃ 　　　　　┏-┛
 * 　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 * *********************************************************
 *
 * @author ：AK
 * @create ：2022/12/17 15:44
 **/
@Service
public class MyCouponServiceImpl implements MyCouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private UserIntegralOrderMapper userIntegralOrderMapper;




    /**
     * 查询【请填写功能名称】列表
     *
     * @return 【请填写功能名称】
     */
    @Override
    public List<Coupon> selectCouponList()
    {
        return couponMapper.selectCoupon();
    }

    @Override
    @Transactional
    public AjaxResult insertUserCoupon(Long userId, Coupon coupon) {
        SysUser user = sysUserMapper.selectUserById(userId);
        if(user.getIntegral() < coupon.getPrice()){
            return AjaxResult.error("积分不足");
        }

        UserCoupon userCoupon = new UserCoupon();
        //新增优惠券
        userCoupon.setUserId(userId);
        userCoupon.setStatus(0);
        userCoupon.setCouponStartTime(DateUtils.getNowDate());
        System.out.println(DateUtils.addDays(DateUtils.getNowDate(), coupon.getValidityDays())+"================================");
        userCoupon.setCouponEndTime(DateUtils.addDays(DateUtils.getNowDate(),coupon.getValidityDays()));
        userCoupon.setTitle(coupon.getTitle());
        userCoupon.setUseLimit(coupon.getUseLimit());
        userCoupon.setDiscountAmount(coupon.getDiscountAmount());
        userCoupon.setCouponCreateTime(DateUtils.getNowDate());
        userCouponMapper.insertUserCoupon(userCoupon);
        //新增优惠券购买详细
        UserIntegralBalanceDepositVo userIntegralDetail = new UserIntegralBalanceDepositVo();
        userIntegralDetail.setOpenid(user.getOpenid());
        userIntegralDetail.setSum(new BigDecimal(coupon.getPrice()));
        userIntegralDetail.setStatus(1);
        userIntegralDetail.setCreateTime(DateUtils.getNowDate());
        userIntegralOrderMapper.insertUserIntegralOrder(userIntegralDetail);

        //购买成功用户减积分
        user.setIntegral(coupon.getPrice());
        sysUserMapper.updateUser(user);
        //购买成功 优惠券减数量
        coupon.setSum(1);
        coupon.setInSum(1);
        couponMapper.updateCoupon(coupon);
        return AjaxResult.success();
    }

    @Override
    public List<UserCoupon> selectUserCoupon(Long userId,Integer status) {
        return userCouponMapper.selectUserCoupon(userId,status);
    }
}
