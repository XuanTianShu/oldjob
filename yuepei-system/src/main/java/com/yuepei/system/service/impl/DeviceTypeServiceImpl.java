package com.yuepei.system.service.impl;

import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.mapper.DeviceTypeMapper;
import com.yuepei.system.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @author ：xiyang
 * @create ：2022/10/31 15:38
 **/
@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    /**
     * 查询设备类型
     *
     * @param deviceTypeId 设备类型主键
     * @return 设备类型
     */
    @Override
    public DeviceType selectDeviceTypeByDeviceTypeId(Long deviceTypeId)
    {
        return deviceTypeMapper.selectDeviceTypeByDeviceTypeId(deviceTypeId);
    }

    /**
     * 查询设备类型列表
     *
     * @param deviceType 设备类型
     * @return 设备类型
     */
    @Override
    public List<DeviceType> selectDeviceTypeList(DeviceType deviceType)
    {
        return deviceTypeMapper.selectDeviceTypeList(deviceType);
    }

    /**
     * 新增设备类型
     *
     * @param deviceType 设备类型
     * @return 结果
     */
    @Override
    public int insertDeviceType(DeviceType deviceType)
    {
        return deviceTypeMapper.insertDeviceType(deviceType);
    }

    /**
     * 修改设备类型
     *
     * @param deviceType 设备类型
     * @return 结果
     */
    @Override
    public int updateDeviceType(DeviceType deviceType)
    {
        return deviceTypeMapper.updateDeviceType(deviceType);
    }

    /**
     * 批量删除设备类型
     *
     * @param deviceTypeIds 需要删除的设备类型主键
     * @return 结果
     */
    @Override
    public int deleteDeviceTypeByDeviceTypeIds(Long[] deviceTypeIds)
    {
        return deviceTypeMapper.deleteDeviceTypeByDeviceTypeIds(deviceTypeIds);
    }

    /**
     * 删除设备类型信息
     *
     * @param deviceTypeId 设备类型主键
     * @return 结果
     */
    @Override
    public int deleteDeviceTypeByDeviceTypeId(Long deviceTypeId)
    {
        return deviceTypeMapper.deleteDeviceTypeByDeviceTypeId(deviceTypeId);
    }
}
