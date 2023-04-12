package com.yuepei.maintenance.service;

import com.yuepei.maintenance.domain.vo.HomeVO;
import com.yuepei.maintenance.domain.vo.MalfunctionVO;
import com.yuepei.maintenance.domain.vo.StockVO;
import com.yuepei.system.domain.SysUserFeedback;

import java.util.List;

public interface AppletMaintenanceService {
    List<HomeVO> selectAppletMaintenanceList(Long userId);

    int selectAppletMaintenanceDeviceCount(Long userId);

    int selectAppletMaintenanceStockCount(Long userId);

    List<StockVO> selectAppletMaintenanceStockList(Long userId, String deviceNumber);

    int selectAppletMaintenanceStockCountByDeviceNumber(Long userId, String deviceNumber);

    List<MalfunctionVO> selectAppletMaintenanceMalfunctionList(Long userId, String deviceNumber);

    int selectAppletMaintenanceMalfunctionCount(Long userId, String deviceNumber);

    int insertMaintenanceRecord(SysUserFeedback sysUserFeedback);
}
