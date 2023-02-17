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
 * @create ：2022/12/19 14:15
 **/
public interface UserIntegralOrderMapper {

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UserIntegralBalanceDepositVo selectUserIntegralOrderById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userIntegralDetail 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserIntegralBalanceDepositVo> selectUserIntegralOrderList(UserIntegralBalanceDepositVo userIntegralDetail);

    /**
     * 新增【请填写功能名称】
     *
     * @param userIntegralDetail 【请填写功能名称】
     * @return 结果
     */
    public int insertUserIntegralOrder(UserIntegralBalanceDepositVo userIntegralDetail);

    public List<UserIntegralBalanceDepositVo> selectUserIntegralOrderByUserId(@Param("openid") String openid , @Param("status") Integer status);

    List<UserIntegralBalanceDepositVo> selectIntegralDetailList(UserIntegralBalanceDepositVo userIntegralBalanceDepositVo);
}
