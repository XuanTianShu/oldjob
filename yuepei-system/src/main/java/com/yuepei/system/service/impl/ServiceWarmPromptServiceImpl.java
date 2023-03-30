package com.yuepei.system.service.impl;

import java.util.List;
import com.yuepei.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuepei.system.mapper.ServiceWarmPromptMapper;
import com.yuepei.system.domain.ServiceWarmPrompt;
import com.yuepei.system.service.IServiceWarmPromptService;

/**
 * 温馨提示Service业务层处理
 *
 * @author ohy
 * @date 2023-03-18
 */
@Service
public class ServiceWarmPromptServiceImpl implements IServiceWarmPromptService
{
    @Autowired
    private ServiceWarmPromptMapper serviceWarmPromptMapper;

    /**
     * 查询温馨提示
     *
     * @param serviceWarmPromptId 温馨提示主键
     * @return 温馨提示
     */
    @Override
    public ServiceWarmPrompt selectServiceWarmPromptByServiceWarmPromptId(Long serviceWarmPromptId)
    {
        return serviceWarmPromptMapper.selectServiceWarmPromptByServiceWarmPromptId(serviceWarmPromptId);
    }

    /**
     * 查询温馨提示列表
     *
     * @param serviceWarmPrompt 温馨提示
     * @return 温馨提示
     */
    @Override
    public List<ServiceWarmPrompt> selectServiceWarmPromptList(ServiceWarmPrompt serviceWarmPrompt)
    {
        return serviceWarmPromptMapper.selectServiceWarmPromptList(serviceWarmPrompt);
    }

    /**
     * 新增温馨提示
     *
     * @param serviceWarmPrompt 温馨提示
     * @return 结果
     */
    @Override
    public int insertServiceWarmPrompt(ServiceWarmPrompt serviceWarmPrompt)
    {
        serviceWarmPrompt.setCreateTime(DateUtils.getNowDate());
        return serviceWarmPromptMapper.insertServiceWarmPrompt(serviceWarmPrompt);
    }

    /**
     * 修改温馨提示
     *
     * @param serviceWarmPrompt 温馨提示
     * @return 结果
     */
    @Override
    public int updateServiceWarmPrompt(ServiceWarmPrompt serviceWarmPrompt)
    {
        serviceWarmPrompt.setUpdateTime(DateUtils.getNowDate());
        return serviceWarmPromptMapper.updateServiceWarmPrompt(serviceWarmPrompt);
    }

    /**
     * 批量删除温馨提示
     *
     * @param serviceWarmPromptIds 需要删除的温馨提示主键
     * @return 结果
     */
    @Override
    public int deleteServiceWarmPromptByServiceWarmPromptIds(Long[] serviceWarmPromptIds)
    {
        return serviceWarmPromptMapper.deleteServiceWarmPromptByServiceWarmPromptIds(serviceWarmPromptIds);
    }

    /**
     * 删除温馨提示信息
     *
     * @param serviceWarmPromptId 温馨提示主键
     * @return 结果
     */
    @Override
    public int deleteServiceWarmPromptByServiceWarmPromptId(Long serviceWarmPromptId)
    {
        return serviceWarmPromptMapper.deleteServiceWarmPromptByServiceWarmPromptId(serviceWarmPromptId);
    }
}
