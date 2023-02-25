package com.yuepei.system.mapper;

import com.yuepei.system.domain.Deposit;
import com.yuepei.system.domain.UserDepositOrder;
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
 * @create ：2023/1/9 13:52
 **/
public interface UserDepositDetailMapper {
    /**
     * 查询用户押金明细
     *
     * @param openid 用户押金明细主键
     * @return 用户押金明细
     */
    public List<UserIntegralBalanceDepositVo> selectUserDepositDetailByOpenid(@Param("openid") String openid , @Param("status") Integer status);

    /**
     * 查询用户押金明细列表
     *
     * @param userDepositDetail 用户押金明细
     * @return 用户押金明细集合
     */
    public List<UserIntegralBalanceDepositVo> selectUserDepositDetailList(UserIntegralBalanceDepositVo userDepositDetail);

    /**
     * 新增用户押金明细
     *
     * @param userDepositDetail 用户押金明细
     * @return 结果
     */
    public int insertUserDepositDetail(UserIntegralBalanceDepositVo userDepositDetail);

    /**
     * 修改用户押金明细
     *
     * @param userDepositDetail 用户押金明细
     * @return 结果
     */
    public int updateUserDepositDetail(UserIntegralBalanceDepositVo userDepositDetail);

    /**
     * 删除用户押金明细
     *
     * @param id 用户押金明细主键
     * @return 结果
     */
    public int deleteUserDepositDetailById(Long id);

    /**
     * 批量删除用户押金明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserDepositDetailByIds(Long[] ids);

}
