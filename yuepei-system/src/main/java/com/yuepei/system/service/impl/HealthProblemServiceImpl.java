package com.yuepei.system.service.impl;

import com.yuepei.system.domain.HealthProblem;
import com.yuepei.system.mapper.HealthProblemMapper;
import com.yuepei.system.service.IHealthProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 康养Service业务层处理
 *
 * @author AK
 * @date 2023-03-24
 */
@Service
public class HealthProblemServiceImpl implements IHealthProblemService
{
    @Autowired
    private HealthProblemMapper healthProblemMapper;

    /**
     * 查询康养
     *
     * @param healthProblemId 康养主键
     * @return 康养
     */
    @Override
    public HealthProblem selectHealthProblemByHealthProblemId(Long healthProblemId)
    {
        return healthProblemMapper.selectHealthProblemByHealthProblemId(healthProblemId);
    }

    /**
     * 查询康养列表
     *
     * @param healthProblem 康养
     * @return 康养
     */
    @Override
    public List<HealthProblem> selectHealthProblemList(HealthProblem healthProblem)
    {
        return healthProblemMapper.selectHealthProblemList(healthProblem);
    }

    /**
     * 新增康养
     *
     * @param healthProblem 康养
     * @return 结果
     */
    @Override
    public int insertHealthProblem(HealthProblem healthProblem)
    {
        return healthProblemMapper.insertHealthProblem(healthProblem);
    }

    /**
     * 修改康养
     *
     * @param healthProblem 康养
     * @return 结果
     */
    @Override
    public int updateHealthProblem(HealthProblem healthProblem)
    {
        return healthProblemMapper.updateHealthProblem(healthProblem);
    }

    /**
     * 批量删除康养
     *
     * @param healthProblemIds 需要删除的康养主键
     * @return 结果
     */
    @Override
    public int deleteHealthProblemByHealthProblemIds(Long[] healthProblemIds)
    {
        return healthProblemMapper.deleteHealthProblemByHealthProblemIds(healthProblemIds);
    }

    /**
     * 删除康养信息
     *
     * @param healthProblemId 康养主键
     * @return 结果
     */
    @Override
    public int deleteHealthProblemByHealthProblemId(Long healthProblemId)
    {
        return healthProblemMapper.deleteHealthProblemByHealthProblemId(healthProblemId);
    }
}
