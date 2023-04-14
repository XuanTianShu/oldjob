package com.yuepei.maintenance.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.yuepei.maintenance.domain.vo.*;
import com.yuepei.maintenance.mapper.AppletMaintenanceMapper;
import com.yuepei.maintenance.service.AppletMaintenanceService;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.SysUserFeedback;
import com.yuepei.system.mapper.HospitalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppletMaintenanceServiceImpl implements AppletMaintenanceService {

    @Autowired
    private AppletMaintenanceMapper appletMaintenanceMapper;

    @Autowired
    private HospitalMapper hospitalMapper;

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

    @Override
    public List<LeaseDeviceListVO> leaseDeviceList(String deviceNumber, Long userId) {
        return appletMaintenanceMapper.leaseDeviceList(deviceNumber,userId);
    }

    @Override
    public LeaseDeviceDetailsVO leaseDeviceDetails(String deviceNumber, Long userId) {
        LeaseDeviceDetailsVO leaseDeviceDetailsVO = appletMaintenanceMapper.leaseDeviceDetails(deviceNumber, userId);
        String deviceFullAddress = leaseDeviceDetailsVO.getDeviceFullAddress();
        if (!deviceFullAddress.equals("0")){
            StringBuffer stringBuffer = new StringBuffer();
            JSONArray objects = JSON.parseArray(deviceFullAddress);
            List<Long> list = objects.toJavaList(Long.class);
            List<Hospital> hospitals = hospitalMapper.selectHospitalList(null);
            for (int i = 0; i < list.size(); i++) {
                for (int k = 0; k < hospitals.size(); k++) {
                    if (list.get(i).equals(hospitals.get(k).getHospitalId())){
                        stringBuffer.append(hospitals.get(k).getHospitalName());
                    }
                }
            }
            leaseDeviceDetailsVO.setDeviceFullAddress(new String(stringBuffer));
        }else {
            leaseDeviceDetailsVO.setDeviceFullAddress("无");
        }
        return leaseDeviceDetailsVO;
    }

    @Override
    public int leaseDeviceCount(String deviceNumber, Long userId) {
        return appletMaintenanceMapper.leaseDeviceCount(deviceNumber,userId);
    }

    @Override
    public TestDeviceVO testDevice(String deviceNumber) {
        return appletMaintenanceMapper.testDevice(deviceNumber);
    }

    @Override
    public MalfunctionDetailVO getDetail(Long feedbackId) {
        return appletMaintenanceMapper.getDetail(feedbackId);
    }
}
