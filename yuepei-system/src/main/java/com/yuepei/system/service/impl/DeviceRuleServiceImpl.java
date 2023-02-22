package com.yuepei.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuepei.system.mapper.DeviceRuleMapper;
import com.yuepei.system.domain.DeviceRule;
import com.yuepei.system.service.IDeviceRuleService;

/**
 * 医院套餐Service业务层处理
 *
 * @author ohy
 * @date 2023-02-10
 */
@Service
public class DeviceRuleServiceImpl implements IDeviceRuleService
{
    @Autowired
    private DeviceRuleMapper deviceRuleMapper;

    /**
     * 查询医院套餐列表
     *
     * @param deviceRule 医院套餐
     * @return 医院套餐
     */
    @Override
    public List<DeviceRule> selectDeviceRuleList(DeviceRule deviceRule)
    {
        return deviceRuleMapper.selectDeviceRuleList(deviceRule);
    }

    /**
     * 新增医院套餐
     *
     * @param deviceRule 医院套餐
     * @return 结果
     */
    @Override
    public int insertDeviceRule(DeviceRule deviceRule)
    {
        return deviceRuleMapper.insertDeviceRule(deviceRule);
    }

    /**
     * 修改医院套餐
     *
     * @param deviceRule 医院套餐
     * @return 结果
     */
    @Override
    public int updateDeviceRule(DeviceRule deviceRule)
    {
        return deviceRuleMapper.updateDeviceRule(deviceRule);
    }

    /**
     * 批量删除医院套餐
     *
     * @param ids 需要删除的医院套餐主键
     * @return 结果
     */
    @Override
    public int deleteDeviceRuleByIds(Long[] ids)
    {
        return deviceRuleMapper.deleteDeviceRuleByIds(ids);
    }

    /**
     * 删除医院套餐信息
     *
     * @param id 医院套餐主键
     * @return 结果
     */
    @Override
    public int deleteDeviceRuleById(Long id)
    {
        return deviceRuleMapper.deleteDeviceRuleById(id);
    }

    /**
     * 根据医院查询套餐
     * @param id
     * @return
     */
    @Override
    public List<DeviceRule> selectHospitalRule(Long id) {
        return deviceRuleMapper.selectHospitalRule(id);
    }

    @Override
    public DeviceRule selectDeviceRuleByDeviceNumber(Long deviceNumber) {
        return deviceRuleMapper.selectDeviceRuleByDeviceNumber(deviceNumber);
    }
}
