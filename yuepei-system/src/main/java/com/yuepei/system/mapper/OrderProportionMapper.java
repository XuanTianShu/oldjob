package com.yuepei.system.mapper;

import com.yuepei.system.domain.DeviceAgent;
import com.yuepei.system.domain.DeviceInvestorAccount;
import com.yuepei.system.domain.OrderProportionDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderProportionMapper {
    void insertAgentHospitalProportion(@Param("orderNumber") String orderNumber, @Param("hospitalId") String hospitalId, @Param("userId") String userId, @Param("proportion") String proportion);

    void insertAgentProportion(@Param("orderNumber") String orderNumber, @Param("deviceAgentList") List<DeviceAgent> deviceAgentList);

    List<DeviceInvestorAccount> selectInvestorAccount(@Param("orderProportionDetailList") List<OrderProportionDetail> orderProportionDetailList);

    void insertAgentAccountProportion(@Param("orderNumber") String orderNumber, @Param("deviceInvestorAccountList") List<DeviceInvestorAccount> deviceInvestorAccountList);

    List<OrderProportionDetail> selectInvestorAccountProportion(@Param("orderNumber") String orderNumber);

    List<OrderProportionDetail> selectInvestorProportion(@Param("orderNumber") String orderNumber);

    List<OrderProportionDetail> selectAgentAccountProportion(@Param("orderNumber") String orderNumber);

    List<OrderProportionDetail> selectAgentHospitalProportion(@Param("orderNumber") String orderNumber);

    void updateAgentAccountProportion(@Param("list") List<OrderProportionDetail> agentAccountProportion);

    void updateAgentHospitalProportion(@Param("list") List<OrderProportionDetail> agentHospitalProportion);

    void updateInvestorProportion(@Param("list") List<OrderProportionDetail> investorProportion);

    void updateInvestorAccountProportion(@Param("list") List<OrderProportionDetail> investorAccountProportion);
}
