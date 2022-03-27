package com.yuepei.system.mapper;

import com.yuepei.system.domain.Deposit;
import com.yuepei.system.domain.UserDepositOrder;
import com.yuepei.system.domain.vo.UserDepositVO;
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
 * @create ：2023/1/9 11:22
 **/
public interface UserDepositOrderMapper {

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UserDepositOrder selectUserDepositOrderById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userDepositOrder 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserDepositOrder> selectUserDepositOrderList(UserDepositOrder userDepositOrder);

    /**
     * 新增【请填写功能名称】
     *
     * @param userDepositOrder 【请填写功能名称】
     * @return 结果
     */
    public int insertUserDepositOrder(UserDepositOrder userDepositOrder);

    /**
     * 修改【请填写功能名称】
     *
     * @param userDepositOrder 【请填写功能名称】
     * @return 结果
     */
    public int updateUserDepositOrder(UserDepositOrder userDepositOrder);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUserDepositOrderById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserDepositOrderByIds(Long[] ids);

    List<UserDepositOrder> selectUserDepositInfo(@Param("openid") String openid, @Param("status") String status,@Param("depositStatus") String depositStatus);

    UserDepositOrder selectUserDepositOrderByOrderNumber(String orderNumber);

    int  checkLeaseOrderByOpenId(@Param("deviceNumber") String deviceNumber);

    int checkLeaseOrder(@Param("orderNumber") String orderNumber);

    List<UserDepositVO> selectUserDepositList(@Param("openid") String openid);

    void bathUpdateUserDeposit(@Param("userDepositVOList") List<UserDepositVO> userDepositVOList);
}
