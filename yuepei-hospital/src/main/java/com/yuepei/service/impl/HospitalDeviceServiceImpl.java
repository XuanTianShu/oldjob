package com.yuepei.service.impl;

import com.yuepei.system.domain.vo.DeviceDetailsVo;
import com.yuepei.service.HospitalDeviceService;
import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.mapper.HospitalDeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zzy
 * @date 2023/2/9 16:38
 */
@Service
public class HospitalDeviceServiceImpl implements HospitalDeviceService {
/*
    @Autowired
    private HospitalDeviceMapper hospitalDeviceMapper;

    @Override
    public DeviceType selectDeviceType(Long userId) {
        return hospitalDeviceMapper.selectDeviceType(userId);
    }

    @Override
    public List<DeviceDetailsVo> selectDeviceTypeDetails(Long deviceTypeId) {
        //搜索该设备数量及对应详细地址
        List<DeviceDetailsVo> deviceDetailsVoList = hospitalDeviceMapper.selectDeviceTypeDetails(deviceTypeId);
        //遍历分割详细地址，赋值后返回数据
        deviceDetailsVoList.stream().forEach(map->{
            String device_full_address = map.getDevice_full_address();
            String[] split = device_full_address.split("，");
            for (int i = 0; i < split.length; i++) {
                map.setDevice_fllor(split[0]);
                map.setDevice_department(split[1]);
                map.setDevice_room(split[2]);
                map.setDevice_bed(split[3]);
            }
        });
        return deviceDetailsVoList;
    }*/
}
