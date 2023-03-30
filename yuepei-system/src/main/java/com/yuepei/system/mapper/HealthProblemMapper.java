package com.yuepei.system.mapper;

import com.yuepei.system.domain.HealthProblem;

import java.util.List;

/**
 * 康养Mapper接口
 *
 * @author AK
 * @date 2023-03-24
 */
public interface HealthProblemMapper
{
    /**
     * 查询康养
     *
     * @param healthProblemId 康养主键
     * @return 康养
     */
    public HealthProblem selectHealthProblemByHealthProblemId(Long healthProblemId);

    /**
     * 查询康养列表
     *
     * @param healthProblem 康养
     * @return 康养集合
     */
    public List<HealthProblem> selectHealthProblemList(HealthProblem healthProblem);

    /**
     * 新增康养
     *
     * @param healthProblem 康养
     * @return 结果
     */
    public int insertHealthProblem(HealthProblem healthProblem);

    /**
     * 修改康养
     *
     * @param healthProblem 康养
     * @return 结果
     */
    public int updateHealthProblem(HealthProblem healthProblem);

    /**
     * 删除康养
     *
     * @param healthProblemId 康养主键
     * @return 结果
     */
    public int deleteHealthProblemByHealthProblemId(Long healthProblemId);

    /**
     * 批量删除康养
     *
     * @param healthProblemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHealthProblemByHealthProblemIds(Long[] healthProblemIds);
}
