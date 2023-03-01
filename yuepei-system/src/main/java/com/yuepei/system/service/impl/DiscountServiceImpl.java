package com.yuepei.system.service.impl;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.Discount;
import com.yuepei.system.domain.DiscountRecord;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.UserDiscount;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.mapper.*;
import com.yuepei.system.service.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 优惠券Service业务层处理
 *
 * @author ohy
 * @date 2023-02-21
 */
@Service
public class DiscountServiceImpl implements IDiscountService
{
    @Autowired
    private DiscountMapper discountMapper;

    @Autowired
    private UserIntegralOrderMapper userIntegralOrderMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private DiscountRecordMapper discountRecordMapper;

    @Autowired
    private HospitalMapper hospitalMapper;

    @Autowired
    private UserDiscountMapper userDiscountMapper;

    /**
     * 查询优惠券
     *
     * @param id 优惠券主键
     * @return 优惠券
     */
    @Override
    public Discount selectDiscountById(Long id)
    {
        return discountMapper.selectDiscountById(id);
    }

    /**
     * 查询优惠券列表
     *
     * @param discount 优惠券
     * @return 优惠券
     */
    @Override
    public List<Discount> selectDiscountList(Discount discount)
    {
        return discountMapper.selectDiscountList(discount);
    }

    /**
     * 新增优惠券
     *
     * @param discount 优惠券
     * @return 结果
     */
    @Override
    public int insertDiscount(Discount discount)
    {
        discount.setCreateTime(DateUtils.getNowDate());
        return discountMapper.insertDiscount(discount);
    }

    /**
     * 修改优惠券
     *
     * @param discount 优惠券
     * @return 结果
     */
    @Override
    public int updateDiscount(Discount discount)
    {
        return discountMapper.updateDiscount(discount);
    }

    /**
     * 批量删除优惠券
     *
     * @param ids 需要删除的优惠券主键
     * @return 结果
     */
    @Override
    public int deleteDiscountByIds(Long[] ids)
    {
        return discountMapper.deleteDiscountByIds(ids);
    }

    /**
     * 删除优惠券信息
     *
     * @param id 优惠券主键
     * @return 结果
     */
    @Override
    public int deleteDiscountById(Long id)
    {
        return discountMapper.deleteDiscountById(id);
    }

    /**
     * 用户兑换优惠券
     * @param discountId 优惠券编号
     * @param user 用户
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult updateUserIntegral(Long discountId, SysUser user) {
        try {
            SysUser sysUser = sysUserMapper.selectUserByOpenid(user.getOpenid());
            Discount discount = discountMapper.selectDiscountById(discountId);
            int num = Integer.parseInt(String.valueOf(sysUser.getIntegral() - discount.getIntegral())) ;
            if (num >= 0){
                //添加兑换记录
                DiscountRecord discountRecord = new DiscountRecord();
                Hospital hospital = hospitalMapper.selectHospitalByHospitalId(discount.getHospitalId());
                discountRecord.setUserid(sysUser.getUserId());
                discountRecord.setIntegral(discount.getIntegral());
                discountRecord.setGrantTime(new Date());
                discountRecord.setIsJyb(1L);
                discountRecord.setHospitalname(hospital.getHospitalName());
                discountRecord.setPrice(discount.getMoney());
                discountRecordMapper.insertDiscountRecord(discountRecord);

                //添加用户消费积分明细
                UserIntegralBalanceDepositVo userIntegralBalanceDepositVo = new UserIntegralBalanceDepositVo();
                userIntegralBalanceDepositVo.setOpenid(user.getOpenid());
                BigDecimal bigDecimal = new BigDecimal(discount.getIntegral());
                userIntegralBalanceDepositVo.setSum(bigDecimal);
                userIntegralBalanceDepositVo.setCreateTime(new Date());
                userIntegralBalanceDepositVo.setStatus(1);
                userIntegralOrderMapper.insertUserIntegralOrder(userIntegralBalanceDepositVo);

                //添加优惠券到用户卡包
                UserDiscount userDiscount = new UserDiscount();
                userDiscount.setUserId(sysUser.getUserId());
                userDiscount.setThresholdName("无");
                userDiscount.setPrice(discount.getMoney());
                userDiscount.setFull(0L);
                userDiscount.setCreateTime(new Date());
                userDiscount.setStatus(0L);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar instance = Calendar.getInstance();
                String format = simpleDateFormat.format(instance.getTime());
                instance.add(Calendar.DAY_OF_MONTH,Integer.parseInt(discount.getPeriod().toString()));
                String s = simpleDateFormat.format(instance.getTime());
                userDiscount.setExpireTime(simpleDateFormat.parse(s));
                userDiscountMapper.insertUserDiscount(userDiscount);

                //更新用户积分
                user.setIntegral(Integer.parseInt(String.valueOf(sysUser.getIntegral() - discount.getIntegral())));
                sysUserMapper.updateUser(sysUser);
            }else {
                return AjaxResult.error();
            }
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
}
