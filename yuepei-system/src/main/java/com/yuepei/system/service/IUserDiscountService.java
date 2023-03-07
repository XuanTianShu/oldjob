package com.yuepei.system.service;

import com.yuepei.system.domain.DiscountThreshold;
import com.yuepei.system.domain.UserDiscount;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户卡包Service接口
 *
 * @author ohy
 * @date 2023-02-27
 */
public interface IUserDiscountService
{
    /**
     * 查询用户卡包
     *
     * @param id 用户卡包主键
     * @return 用户卡包
     */
    public UserDiscount selectUserDiscountById(Long id);

    /**
     * 查询用户卡包列表
     *
     * @param userDiscount 用户卡包
     * @return 用户卡包集合
     */
    public List<UserDiscount> selectUserDiscountList(UserDiscount userDiscount);

    /**
     * 新增用户卡包
     *
     * @param userDiscount 用户卡包
     * @return 结果
     */
    public int insertUserDiscount(UserDiscount userDiscount);

    /**
     * 修改用户卡包
     *
     * @param userDiscount 用户卡包
     * @return 结果
     */
    public int updateUserDiscount(UserDiscount userDiscount);

    /**
     * 批量删除用户卡包
     *
     * @param ids 需要删除的用户卡包主键集合
     * @return 结果
     */
    public int deleteUserDiscountByIds(Long[] ids);

    /**
     * 删除用户卡包信息
     *
     * @param id 用户卡包主键
     * @return 结果
     */
    public int deleteUserDiscountById(Long id);

    /**
     * 发放优惠券到卡包
     * @param userId
     * @param discountThreshold
     * @param parse
     * @param parse1
     * @param money
     */
    void sendUserDiscount(Long[] userId, DiscountThreshold discountThreshold, Date parse, Date parse1, BigDecimal money);

    /**
     * 根据状态查看用户卡包
     * @param openid
     * @param userDiscount
     * @return
     */
    List<UserDiscount> selectMyDiscountByOpenId(String openid,UserDiscount userDiscount);

//    List<UserDiscount> selectMyDiscountByOpenId(String openid);
}
