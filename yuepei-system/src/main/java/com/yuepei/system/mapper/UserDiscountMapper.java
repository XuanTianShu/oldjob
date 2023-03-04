package com.yuepei.system.mapper;

import com.yuepei.system.domain.DiscountThreshold;
import com.yuepei.system.domain.UserDiscount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户卡包Mapper接口
 *
 * @author ohy
 * @date 2023-02-27
 */
public interface UserDiscountMapper
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
     * 删除用户卡包
     *
     * @param id 用户卡包主键
     * @return 结果
     */
    public int deleteUserDiscountById(Long id);

    /**
     * 批量删除用户卡包
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserDiscountByIds(Long[] ids);

    /**
     * 发放优惠券到卡包
     * @param userId
     * @param discountThreshold
     * @param parse
     * @param parse1
     * @param money
     */
    void sendUserDiscount(@Param("userId") Long[] userId,
                          @Param("discountThreshold") DiscountThreshold discountThreshold,
                          @Param("parse") Date parse, @Param("parse1") Date parse1,
                          @Param("money") BigDecimal money);

    /**
     * 查看用户的卡包
     * @param openid
     * @return
     */
    List<UserDiscount> selectMyDiscountByOpenId(@Param("openid") String openid, @Param("userDiscount") UserDiscount userDiscount);
}
