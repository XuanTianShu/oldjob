package com.yuepei.system.service.impl;

import com.yuepei.common.annotation.DataScope;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.InvestorUser;
import com.yuepei.system.domain.vo.TotalProportionVO;
import com.yuepei.system.mapper.InvestorUserMapper;
import com.yuepei.system.service.IInvestorUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 投资人管理Service业务层处理
 *
 * @author ohy
 * @date 2023-02-14
 */
@Service
public class InvestorUserServiceImpl implements IInvestorUserService
{
    @Autowired
    private InvestorUserMapper investorUserMapper;

    /**
     * 查询投资人管理
     *
     * @param id 投资人管理主键
     * @return 投资人管理
     */
    @Override
    public InvestorUser selectInvestorUserById(Long id)
    {
        return investorUserMapper.selectInvestorUserById(id);
    }

    /**
     * 查询投资人管理列表
     *
     * @param investorUser 投资人管理
     * @return 投资人管理
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectInvestorUserList(SysUser user)
    {
        return investorUserMapper.selectInvestorUserList(user);
    }

    /**
     * 新增投资人管理
     *
     * @param investorUser 投资人管理
     * @return 结果
     */
    @Override
    public int insertInvestorUser(InvestorUser investorUser)
    {
        investorUser.setCreateTime(DateUtils.getNowDate());
        investorUser.setPassword(SecurityUtils.encryptPassword(investorUser.getPassword()));
        return investorUserMapper.insertInvestorUser(investorUser);
    }

    /**
     * 修改投资人管理
     *
     * @param investorUser 投资人管理
     * @return 结果
     */
    @Override
    public int updateInvestorUser(InvestorUser investorUser)
    {
        investorUser.setUpdateTime(DateUtils.getNowDate());
        return investorUserMapper.updateInvestorUser(investorUser);
    }

    /**
     * 批量删除投资人管理
     *
     * @param ids 需要删除的投资人管理主键
     * @return 结果
     */
    @Override
    public int deleteInvestorUserByIds(Long[] ids)
    {
        return investorUserMapper.deleteInvestorUserByIds(ids);
    }

    /**
     * 删除投资人管理信息
     *
     * @param id 投资人管理主键
     * @return 结果
     */
    @Override
    public int deleteInvestorUserById(Long id)
    {
        return investorUserMapper.deleteInvestorUserById(id);
    }

    @Override
    public TotalProportionVO totalProportion(DeviceInvestor deviceInvestor) {
        return investorUserMapper.totalProportion(deviceInvestor);
    }

    @Override
    public int addDevice(DeviceInvestor deviceInvestor) {
        deviceInvestor.setCreateTime(new Date());
        return investorUserMapper.addDevice(deviceInvestor);
    }

    @Override
    public int updateDevice(DeviceInvestor deviceInvestor) {
        return investorUserMapper.updateDevice(deviceInvestor);
    }
}
