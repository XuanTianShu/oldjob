package com.yuepei.system.mapper;

import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
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
 * @create ：2023/1/7 15:43
 **/
public interface UserBalanceDetailMapper {
    /**
     * 查询用户余额明细
     *
     * @param id 用户余额明细主键
     * @return 用户余额明细
     */
    public UserIntegralBalanceDepositVo selectUserBalanceDetailById(Long id);

    /**
     * 查询用户余额明细列表
     *
     * @param userBalanceDetail 用户余额明细
     * @return 用户余额明细集合
     */
    public List<UserIntegralBalanceDepositVo> selectUserBalanceDetailList(UserIntegralBalanceDepositVo userBalanceDetail);

    /**
     * 新增用户余额明细
     *
     * @param userBalanceDetail 用户余额明细
     * @return 结果
     */
    public int insertUserBalanceDetail(UserIntegralBalanceDepositVo userBalanceDetail);

    /**
     * 修改用户余额明细
     *
     * @param userBalanceDetail 用户余额明细
     * @return 结果
     */
    public int updateUserBalanceDetail(UserIntegralBalanceDepositVo userBalanceDetail);

    /**
     * 删除用户余额明细
     *
     * @param id 用户余额明细主键
     * @return 结果
     */
    public int deleteUserBalanceDetailById(Long id);

    /**
     * 批量删除用户余额明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBalanceDetailByIds(Long[] ids);

    public List<UserIntegralBalanceDepositVo> selectUserBalanceDetailByOpenid(@Param("openid") String openid, @Param("status") Integer status);
}
