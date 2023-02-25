package com.yuepei.system.service;

import com.yuepei.system.domain.DeviceType;

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
 * @create ：2022/10/31 15:28
 **/
public interface DeviceTypeService {

    /**
     * 查询设备类型
     *
     * @param deviceTypeId 设备类型主键
     * @return 设备类型
     */
    public DeviceType selectDeviceTypeByDeviceTypeId(Long deviceTypeId);

    /**
     * 查询设备类型列表
     *
     * @param deviceType 设备类型
     * @return 设备类型集合
     */
    public List<DeviceType> selectDeviceTypeList(DeviceType deviceType);

    /**
     * 新增设备类型
     *
     * @param deviceType 设备类型
     * @return 结果
     */
    public int insertDeviceType(DeviceType deviceType);

    /**
     * 修改设备类型
     *
     * @param deviceType 设备类型
     * @return 结果
     */
    public int updateDeviceType(DeviceType deviceType);

    /**
     * 批量删除设备类型
     *
     * @param deviceTypeIds 需要删除的设备类型主键集合
     * @return 结果
     */
    public int deleteDeviceTypeByDeviceTypeIds(Long[] deviceTypeIds);

    /**
     * 删除设备类型信息
     *
     * @param deviceTypeId 设备类型主键
     * @return 结果
     */
    public int deleteDeviceTypeByDeviceTypeId(Long deviceTypeId);
}
