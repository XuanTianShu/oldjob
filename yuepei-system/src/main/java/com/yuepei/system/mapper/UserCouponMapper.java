package com.yuepei.system.mapper;

import com.yuepei.system.domain.Coupon;
import com.yuepei.system.domain.UserCoupon;
import org.apache.ibatis.annotations.Param;

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
 * @create ：2022/12/19 9:43
 **/
public interface UserCouponMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UserCoupon selectUserCouponById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userCoupon 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserCoupon> selectUserCouponList(UserCoupon userCoupon);

    /**
     * 新增【请填写功能名称】
     *
     * @param userCoupon 【请填写功能名称】
     * @return 结果
     */
    public int insertUserCoupon(UserCoupon userCoupon);

    /**
     * 修改【请填写功能名称】
     *
     * @param userCoupon 【请填写功能名称】
     * @return 结果
     */
    public int updateUserCoupon(UserCoupon userCoupon);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUserCouponById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserCouponByIds(Long[] ids);

    List<UserCoupon> selectUserCoupon(@Param("userId") Long userId, @Param("status") Integer status);

    void batchUpdateUserCoupon(@Param("userId") String userId);
}
