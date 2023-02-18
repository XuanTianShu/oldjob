package com.yuepei.system.mapper;

import com.yuepei.system.domain.UserPayOrder;

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
 * @create ：2023/1/5 15:05
 **/
public interface UserPayOrderMapper {

    /**
     * 查询用户充值订单
     *
     * @param id 用户充值订单主键
     * @return 用户充值订单
     */
    public UserPayOrder selectUserPayOrderById(Long id);

    /**
     * 查询用户充值订单列表
     *
     * @param userPayOrder 用户充值订单
     * @return 用户充值订单集合
     */
    public List<UserPayOrder> selectUserPayOrderList(UserPayOrder userPayOrder);

    /**
     * 新增用户充值订单
     *
     * @param userPayOrder 用户充值订单
     * @return 结果
     */
    public int insertUserPayOrder(UserPayOrder userPayOrder);

    /**
     * 修改用户充值订单
     *
     * @param userPayOrder 用户充值订单
     * @return 结果
     */
    public int updateUserPayOrder(UserPayOrder userPayOrder);

    /**
     * 删除用户充值订单
     *
     * @param id 用户充值订单主键
     * @return 结果
     */
    public int deleteUserPayOrderById(Long id);

    /**
     * 批量删除用户充值订单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserPayOrderByIds(Long[] ids);

}
