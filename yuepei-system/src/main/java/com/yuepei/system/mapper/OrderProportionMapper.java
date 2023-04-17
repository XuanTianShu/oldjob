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
}
