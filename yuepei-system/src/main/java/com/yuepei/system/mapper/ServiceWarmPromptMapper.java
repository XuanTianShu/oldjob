package com.yuepei.system.mapper;

import com.yuepei.system.domain.ServiceWarmPrompt;

import java.util.List;

/**
 * 温馨提示Mapper接口
 *
 * @author ohy
 * @date 2023-03-18
 */
public interface ServiceWarmPromptMapper
{
    /**
     * 查询温馨提示
     *
     * @param serviceWarmPromptId 温馨提示主键
     * @return 温馨提示
     */
    public ServiceWarmPrompt selectServiceWarmPromptByServiceWarmPromptId(Long serviceWarmPromptId);

    /**
     * 查询温馨提示列表
     *
     * @param serviceWarmPrompt 温馨提示
     * @return 温馨提示集合
     */
    public List<ServiceWarmPrompt> selectServiceWarmPromptList(ServiceWarmPrompt serviceWarmPrompt);

    /**
     * 新增温馨提示
     *
     * @param serviceWarmPrompt 温馨提示
     * @return 结果
     */
    public int insertServiceWarmPrompt(ServiceWarmPrompt serviceWarmPrompt);

    /**
     * 修改温馨提示
     *
     * @param serviceWarmPrompt 温馨提示
     * @return 结果
     */
    public int updateServiceWarmPrompt(ServiceWarmPrompt serviceWarmPrompt);

    /**
     * 删除温馨提示
     *
     * @param serviceWarmPromptId 温馨提示主键
     * @return 结果
     */
    public int deleteServiceWarmPromptByServiceWarmPromptId(Long serviceWarmPromptId);

    /**
     * 批量删除温馨提示
     *
     * @param serviceWarmPromptIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteServiceWarmPromptByServiceWarmPromptIds(Long[] serviceWarmPromptIds);
}
