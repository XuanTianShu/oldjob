package com.yuepei.system.mapper;

import com.yuepei.system.domain.Discount;
import com.yuepei.system.domain.DiscountRecord;
import com.yuepei.system.domain.DiscountThreshold;
import com.yuepei.system.domain.vo.DiscountRecordVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 优惠券发放记录Mapper接口
 *
 * @author ohy
 * @date 2023-02-24
 */
public interface DiscountRecordMapper
{
    /**
     * 查询优惠券发放记录
     *
     * @param id 优惠券发放记录主键
     * @return 优惠券发放记录
     */
    public DiscountRecord selectDiscountRecordById(Long id);

    /**
     * 查询优惠券发放记录列表
     *
     * @param discountRecordVO 优惠券发放记录
     * @return 优惠券发放记录集合
     */
    public List<DiscountRecordVO> selectDiscountRecordList(DiscountRecordVO discountRecordVO);

    /**
     * 新增优惠券发放记录
     *
     * @param discountRecord 优惠券发放记录
     * @return 结果
     */
    public int insertDiscountRecord(DiscountRecord discountRecord);

    /**
     * 修改优惠券发放记录
     *
     * @param discountRecord 优惠券发放记录
     * @return 结果
     */
    public int updateDiscountRecord(DiscountRecord discountRecord);

    /**
     * 删除优惠券发放记录
     *
     * @param id 优惠券发放记录主键
     * @return 结果
     */
    public int deleteDiscountRecordById(Long id);

    /**
     * 批量删除优惠券发放记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDiscountRecordByIds(Long[] ids);

    /**
     * 添加优惠券记录
     * @param userId
     * @param userId1
     * @param discount
     * @param discountThreshold
     * @param date
     */
    void sendDiscountRecord(@Param("userId") Long userId, @Param("userId1") Long[] userId1,
                            @Param("discount") Discount discount, @Param("discountThreshold") DiscountThreshold discountThreshold,
                            @Param("date") Date date);
}
