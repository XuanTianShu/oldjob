package com.yuepei.maintenance.mapper;

import com.yuepei.maintenance.domain.vo.HomeVO;
import com.yuepei.maintenance.domain.vo.MalfunctionVO;
import com.yuepei.maintenance.domain.vo.StockVO;
import com.yuepei.system.domain.SysUserFeedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppletMaintenanceMapper {
    List<HomeVO> selectAppletMaintenanceList(@Param("userId") Long userId);

    int selectAppletMaintenanceDeviceCount(@Param("userId") Long userId);

    int selectAppletMaintenanceStockCount(@Param("userId") Long userId);

    List<StockVO> selectAppletMaintenanceStockList(@Param("userId") Long userId, @Param("deviceNumber") String deviceNumber);

    int selectAppletMaintenanceStockCountByDeviceNumber(@Param("userId") Long userId, @Param("deviceNumber") String deviceNumber);

    List<MalfunctionVO> selectAppletMaintenanceMalfunctionList(@Param("userId") Long userId, @Param("deviceNumber") String deviceNumber);

    int selectAppletMaintenanceMalfunctionCount(@Param("userId") Long userId, @Param("deviceNumber") String deviceNumber);

    int insertMaintenanceRecord(SysUserFeedback sysUserFeedback);
}
