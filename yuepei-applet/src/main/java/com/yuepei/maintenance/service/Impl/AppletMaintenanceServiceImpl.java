package com.yuepei.maintenance.service.Impl;

import com.yuepei.maintenance.domain.vo.HomeVO;
import com.yuepei.maintenance.domain.vo.MalfunctionVO;
import com.yuepei.maintenance.domain.vo.StockVO;
import com.yuepei.maintenance.mapper.AppletMaintenanceMapper;
import com.yuepei.maintenance.service.AppletMaintenanceService;
import com.yuepei.system.domain.SysUserFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppletMaintenanceServiceImpl implements AppletMaintenanceService {

    @Autowired
    private AppletMaintenanceMapper appletMaintenanceMapper;

    @Override
    public List<HomeVO> selectAppletMaintenanceList(Long userId) {
        return appletMaintenanceMapper.selectAppletMaintenanceList(userId);
    }

    @Override
    public int selectAppletMaintenanceDeviceCount(Long userId) {
        return appletMaintenanceMapper.selectAppletMaintenanceDeviceCount(userId);
    }

    @Override
    public int selectAppletMaintenanceStockCount(Long userId) {
        return appletMaintenanceMapper.selectAppletMaintenanceStockCount(userId);
    }

    @Override
    public List<StockVO> selectAppletMaintenanceStockList(Long userId, String deviceNumber) {
        return appletMaintenanceMapper.selectAppletMaintenanceStockList(userId,deviceNumber);
    }

    @Override
    public int selectAppletMaintenanceStockCountByDeviceNumber(Long userId, String deviceNumber) {
        return appletMaintenanceMapper.selectAppletMaintenanceStockCountByDeviceNumber(userId,deviceNumber);
    }

    @Override
    public List<MalfunctionVO> selectAppletMaintenanceMalfunctionList(Long userId, String deviceNumber) {
        return appletMaintenanceMapper.selectAppletMaintenanceMalfunctionList(userId,deviceNumber);
    }

    @Override
    public int selectAppletMaintenanceMalfunctionCount(Long userId, String deviceNumber) {
        return appletMaintenanceMapper.selectAppletMaintenanceMalfunctionCount(userId,deviceNumber);
    }

    @Override
    public int insertMaintenanceRecord(SysUserFeedback sysUserFeedback) {
        return appletMaintenanceMapper.insertMaintenanceRecord(sysUserFeedback);
    }
}
