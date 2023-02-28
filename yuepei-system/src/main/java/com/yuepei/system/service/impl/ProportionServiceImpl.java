package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.Proportion;
import com.yuepei.system.mapper.ProportionMapper;
import com.yuepei.system.service.IProportionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分成比例Service业务层处理
 *
 * @author ohy
 * @date 2023-02-28
 */
@Service
public class ProportionServiceImpl implements IProportionService
{
    @Autowired
    private ProportionMapper proportionMapper;

    /**
     * 查询分成比例列表
     *
     * @param proportion 分成比例
     * @return 分成比例
     */
    @Override
    public List<Proportion> selectProportionList(Proportion proportion)
    {
        return proportionMapper.selectProportionList(proportion);
    }

    /**
     * 修改分成比例
     *
     * @param proportion 分成比例
     * @return 结果
     */
    @Override
    public int updateProportion(Proportion proportion)
    {
        proportion.setUpdateTime(DateUtils.getNowDate());
        return proportionMapper.updateProportion(proportion);
    }
}
