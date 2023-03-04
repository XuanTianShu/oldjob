package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.DiscountThreshold;
import com.yuepei.system.domain.UserDiscount;
import com.yuepei.system.mapper.UserDiscountMapper;
import com.yuepei.system.service.IUserDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户卡包Service业务层处理
 *
 * @author ohy
 * @date 2023-02-27
 */
@Service
public class UserDiscountServiceImpl implements IUserDiscountService
{
    @Autowired
    private UserDiscountMapper userDiscountMapper;

    /**
     * 查询用户卡包
     *
     * @param id 用户卡包主键
     * @return 用户卡包
     */
    @Override
    public UserDiscount selectUserDiscountById(Long id)
    {
        return userDiscountMapper.selectUserDiscountById(id);
    }

    /**
     * 查询用户卡包列表
     *
     * @param userDiscount 用户卡包
     * @return 用户卡包
     */
    @Override
    public List<UserDiscount> selectUserDiscountList(UserDiscount userDiscount)
    {
        return userDiscountMapper.selectUserDiscountList(userDiscount);
    }

    /**
     * 新增用户卡包
     *
     * @param userDiscount 用户卡包
     * @return 结果
     */
    @Override
    public int insertUserDiscount(UserDiscount userDiscount)
    {
        userDiscount.setCreateTime(DateUtils.getNowDate());
        return userDiscountMapper.insertUserDiscount(userDiscount);
    }

    /**
     * 修改用户卡包
     *
     * @param userDiscount 用户卡包
     * @return 结果
     */
    @Override
    public int updateUserDiscount(UserDiscount userDiscount)
    {
        userDiscount.setUpdateTime(DateUtils.getNowDate());
        return userDiscountMapper.updateUserDiscount(userDiscount);
    }

    /**
     * 批量删除用户卡包
     *
     * @param ids 需要删除的用户卡包主键
     * @return 结果
     */
    @Override
    public int deleteUserDiscountByIds(Long[] ids)
    {
        return userDiscountMapper.deleteUserDiscountByIds(ids);
    }

    /**
     * 删除用户卡包信息
     *
     * @param id 用户卡包主键
     * @return 结果
     */
    @Override
    public int deleteUserDiscountById(Long id)
    {
        return userDiscountMapper.deleteUserDiscountById(id);
    }

    /**
     * 发放优惠券到卡包
     * @param userId
     * @param discountThreshold
     * @param parse
     * @param parse1
     * @param money
     */
    @Override
    public void sendUserDiscount(Long[] userId, DiscountThreshold discountThreshold, Date parse, Date parse1, BigDecimal money) {
        userDiscountMapper.sendUserDiscount(userId,discountThreshold,parse,parse1,money);
    }

    @Override
    public List<UserDiscount> selectMyDiscountByOpenId(String openid,UserDiscount userDiscount) {
        return userDiscountMapper.selectMyDiscountByOpenId(openid,userDiscount);
    }
}
