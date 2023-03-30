package com.yuepei.system.service;

import java.util.List;
import com.yuepei.system.domain.DeviceRule;

/**
 * 医院套餐Service接口
 *
 * @author ohy
 * @date 2023-02-10
 */
public interface IDeviceRuleService
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
     * 批量删除医院套餐
     *
     * @param ids 需要删除的医院套餐主键集合
     * @return 结果
     */
    public int deleteDeviceRuleByIds(Long[] ids);

    /**
     * 删除医院套餐信息
     *
     * @param id 医院套餐主键
     * @return 结果
     */
    public int deleteDeviceRuleById(Long id);

    /**
     * 根据医院查询套餐
     * @param id
     * @return
     */
    List<DeviceRule> selectHospitalRule(Long id);

    DeviceRule selectDeviceRuleByDeviceNumber(String deviceNumber);
}
