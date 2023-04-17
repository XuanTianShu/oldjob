package com.yuepei.system.mapper;

import com.yuepei.system.domain.OrderProportionDetail;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.system.domain.vo.*;
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
 * @create ：2023/1/9 18:32
 **/
public interface UserLeaseOrderMapper {
    /**
     * 查询用户租赁订单
     *
     * @param id 用户租赁订单主键
     * @return 用户租赁订单
     */
    public UserLeaseOrder selectUserLeaseOrderById(Long id);

    /**
     * 查询用户租赁订单列表
     *
     * @param userLeaseOrder 用户租赁订单
     * @return 用户租赁订单集合
     */
    public List<UserLeaseOrder> selectUserLeaseOrderList(UserLeaseOrder userLeaseOrder);

    /**
     * 新增用户租赁订单
     *
     * @param userLeaseOrder 用户租赁订单
     * @return 结果
     */
    public int insertUserLeaseOrder(UserLeaseOrder userLeaseOrder);

    /**
     * 修改用户租赁订单
     *
     * @param userLeaseOrder 用户租赁订单
     * @return 结果
     */
    public int updateUserLeaseOrder(UserLeaseOrder userLeaseOrder);

    /**
     * 删除用户租赁订单
     *
     * @param id 用户租赁订单主键
     * @return 结果
     */
    public int deleteUserLeaseOrderById(Long id);

    /**
     * 批量删除用户租赁订单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserLeaseOrderByIds(Long[] ids);

    List<UserLeaseOrder> userLeaseOrder(@Param("openid") String openid, @Param("status") Integer status);

    public int updateUserLeaseOrderByOrderNumber(UserLeaseOrder userLeaseOrder);

    /**
     * 查询租赁订单
     * @param leaseOrderVO
     * @return
     */
    List<UserLeaseOrder> leaseOrderList(LeaseOrderVO leaseOrderVO);

    List<UserLeaseOrder> selectUserLeaseOrder(@Param("deviceNumber") List<String> deviceNumber);

    UserLeaseOrder selectLeaseOrderDetails(@Param("orderNumber") String orderNumber);

    List<UserLeaseOrder> selectRevenueStatistics(@Param("deviceNumber") List<String> deviceNumber,@Param("hospitalId") Long hospitalId);

    OrderSumAndMoneyVO selectDayOrder();

    ConditionOrderVO selectConditionOrder(LeaseOrderVO leaseOrderVO);

    List<UserLeaseOrder> selectUserLeaseOrderByDeviceNumber(@Param("deviceNumber") String deviceNumber,@Param("hospitalId") String hospitalId);

    int selectUserOrderByDeviceNumber(@Param("deviceNumber") String deviceNumber, @Param("openid") String openid);

    List<String> selectUserDepositList(@Param("openid") String openid, @Param("deviceNumber") String deviceNumber);

    List<String> selectUSerLeaseOrderDeposit(@Param("openid") String openid, @Param("deviceNumber") String deviceNumber);

    UserLeaseOrder selectUseDevice(@Param("deviceNumber") String deviceNumber,@Param("hospitalId") Long hospitalId);

    List<UserLeaseOrder> selectUserLeaseOrderByOrderNumber(@Param("orderNumber") String orderNumber,@Param("hospitalId") String hospitalId);

    List<UserLeaseOrder> selectUserLeaseOrderByInvestor(@Param("investorId") String investorId);

    List<UserOrderVO> selectUserOrderDepositList(@Param("openid") String openid);

    UserLeaseOrder selectOrderByDeviceNumberAndChoose(@Param("substring1") String substring1);

    UserLeaseOrder selectUserLeaseOrderByOpenId(@Param("orderNumber") String orderNumber);

    UserLeaseOrder selectOrderByDeviceNumber(@Param("substring1") String substring1);

    void deleteUserLeaseOrderByOrderNumber(@Param("orderNumber") String orderNumber);

    List<UserLeaseOrder> selectUserLeaseOrderByAgentId(@Param("userId") String userId);

    List<UserLeaseOrder> selectUserLeaseOrderByAgentId(@Param("deviceNumbers")List<String> deviceNumbers, @Param("userId") String userId);

    AgentAndHospitalNameVO selectUserNameAndHospitalName(@Param("deviceNumber") String deviceNumber);

    void updateUserLeaseOrderByDeviceNumber(UserLeaseOrder userLeaseOrder);

    UserLeaseOrder selectLeaseOrderByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    int selectUSerLeaseOrderByChild(UserLeaseOrder userLeaseOrder);

    void insertOrderProportionDeatail(@Param("orderNumber") String orderNumber, @Param("orderProportionDetailList") List<OrderProportionDetail> orderProportionDetailList);

    List<UserLeaseOrder> selectUserLeaseOrderByHospitalId(@Param("hospitalId") String hospitalId);

    List<UserLeaseOrder> selectUserLeaseOrderByAgentIdAndStatus(@Param("userId") String userId);

    List<UserLeaseOrder> selectUserLeaseOrderByHospitalIdAndStatus(@Param("hospitalId") String hospitalId);
}
