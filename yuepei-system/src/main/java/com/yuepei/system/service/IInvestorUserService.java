package com.yuepei.system.service;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.InvestorUser;
import com.yuepei.system.domain.vo.TotalProportionVO;

import java.util.List;

/**
 * 投资人管理Service接口
 *
 * @author ohy
 * @date 2023-02-14
 */
public interface IInvestorUserService
{
    /**
     * 查询投资人管理
     *
     * @param id 投资人管理主键
     * @return 投资人管理
     */
    public InvestorUser selectInvestorUserById(Long id);

    /**
     * 查询投资人管理列表
     *
     * @param investorUser 投资人管理
     * @return 投资人管理集合
     */
    public List<SysUser> selectInvestorUserList(SysUser user);

    /**
     * 新增投资人管理
     *
     * @param investorUser 投资人管理
     * @return 结果
     */
    public int insertInvestorUser(InvestorUser investorUser);

    /**
     * 修改投资人管理
     *
     * @param investorUser 投资人管理
     * @return 结果
     */
    public int updateInvestorUser(InvestorUser investorUser);

    /**
     * 批量删除投资人管理
     *
     * @param ids 需要删除的投资人管理主键集合
     * @return 结果
     */
    public int deleteInvestorUserByIds(Long[] ids);

    /**
     * 删除投资人管理信息
     *
     * @param id 投资人管理主键
     * @return 结果
     */
    public int deleteInvestorUserById(Long id);

    TotalProportionVO totalProportion(DeviceInvestor deviceInvestor);

    int addDevice(DeviceInvestor deviceInvestor);

    int updateDevice(DeviceInvestor deviceInvestor);
}
