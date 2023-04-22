package com.yuepei.system.service.impl;

import com.yuepei.common.annotation.DataScope;
import com.yuepei.system.domain.Discount;
import com.yuepei.system.domain.DiscountRecord;
import com.yuepei.system.domain.DiscountThreshold;
import com.yuepei.system.domain.vo.DiscountRecordVO;
import com.yuepei.system.mapper.DiscountRecordMapper;
import com.yuepei.system.service.IDiscountRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 优惠券发放记录Service业务层处理
 *
 * @author ohy
 * @date 2023-02-24
 */
@Service
public class DiscountRecordServiceImpl implements IDiscountRecordService
{
    @Autowired
    private DiscountRecordMapper discountRecordMapper;

    /**
     * 查询优惠券发放记录
     *
     * @param id 优惠券发放记录主键
     * @return 优惠券发放记录
     */
    @Override
    public DiscountRecord selectDiscountRecordById(Long id)
    {
        return discountRecordMapper.selectDiscountRecordById(id);
    }

    /**
     * 查询优惠券发放记录列表
     *
     * @param discountRecordVO 优惠券发放记录
     * @return 优惠券发放记录
     */
    @Override
    @DataScope(deptAlias = "dr",userAlias = "dr")
    public List<DiscountRecordVO> selectDiscountRecordList(DiscountRecordVO discountRecordVO)
    {
        return discountRecordMapper.selectDiscountRecordList(discountRecordVO);
    }

    /**
     * 新增优惠券发放记录
     *
     * @param discountRecord 优惠券发放记录
     * @return 结果
     */
    @Override
    public int insertDiscountRecord(DiscountRecord discountRecord)
    {
        return discountRecordMapper.insertDiscountRecord(discountRecord);
    }

    /**
     * 修改优惠券发放记录
     *
     * @param discountRecord 优惠券发放记录
     * @return 结果
     */
    @Override
    public int updateDiscountRecord(DiscountRecord discountRecord)
    {
        return discountRecordMapper.updateDiscountRecord(discountRecord);
    }

    /**
     * 批量删除优惠券发放记录
     *
     * @param ids 需要删除的优惠券发放记录主键
     * @return 结果
     */
    @Override
    public int deleteDiscountRecordByIds(Long[] ids)
    {
        return discountRecordMapper.deleteDiscountRecordByIds(ids);
    }

    /**
     * 删除优惠券发放记录信息
     *
     * @param id 优惠券发放记录主键
     * @return 结果
     */
    @Override
    public int deleteDiscountRecordById(Long id)
    {
        return discountRecordMapper.deleteDiscountRecordById(id);
    }

    /**
     * 添加优惠券记录
     * @param userId
     * @param userId1
     * @param discount
     * @param discountThreshold
     * @param date
     */
    @Override
    public void sendDiscountRecord(Long userId, Long[] userId1, Discount discount, DiscountThreshold discountThreshold, Date date) {
        discountRecordMapper.sendDiscountRecord(userId,userId1,discount,discountThreshold,date);
    }
}
