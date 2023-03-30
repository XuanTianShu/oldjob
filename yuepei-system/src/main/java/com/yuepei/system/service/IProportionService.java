package com.yuepei.system.service;

import com.yuepei.system.domain.Proportion;

import java.util.List;

/**
 * 分成比例Service接口
 *
 * @author ohy
 * @date 2023-02-28
 */
public interface IProportionService
{
    /**
     * 查询分成比例列表
     *
     * @param proportion 分成比例
     * @return 分成比例集合
     */
    public List<Proportion> selectProportionList(Proportion proportion);

    /**
     * 修改分成比例
     *
     * @param proportion 分成比例
     * @return 结果
     */
    public int updateProportion(Proportion proportion);
}
