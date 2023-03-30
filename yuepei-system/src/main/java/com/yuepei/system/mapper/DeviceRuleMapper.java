package com.yuepei.system.mapper;

import com.yuepei.system.domain.DeviceRule;

import java.util.List;

/**
 * 医院套餐接口
 *
 * @author ohy
 * @date 2023-02-10
 */
public interface DeviceRuleMapper
{
    /**
     * 查询医院套餐
     *
     * @param id 医院套餐主键
     * @return 医院套餐
     */
    public DeviceRule selectDeviceRuleById(Long id);

    /**
     * 查询医院套餐列表
     *
     * @param deviceRule 医院套餐
     * @return 医院套餐集合
     */
    public List<DeviceRule> selectDeviceRuleList(DeviceRule deviceRule);

    /**
     * 新增医院套餐
     *
     * @param deviceRule 医院套餐
     * @return 结果
     */
    public int insertDeviceRule(DeviceRule deviceRule);

    /**
     * 修改医院套餐
     *
     * @param deviceRule 医院套餐
     * @return 结果
     */
    public int updateDeviceRule(DeviceRule deviceRule);

    /**
     * 删除医院套餐
     *
     * @param id 医院套餐主键
     * @return 结果
     */
    public int deleteDeviceRuleById(Long id);

    /**
     * 批量删除医院套餐
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceRuleByIds(Long[] ids);

    /**
     * 根据医院查询套餐
     * @param id
     * @return
     */
    List<DeviceRule> selectHospitalRule(Long id);

    DeviceRule selectDeviceRuleByDeviceNumber(String deviceNumber);
}
