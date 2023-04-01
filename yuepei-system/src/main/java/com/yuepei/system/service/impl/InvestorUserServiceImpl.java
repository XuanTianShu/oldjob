package com.yuepei.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.yuepei.common.annotation.DataScope;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.InvestorUser;
import com.yuepei.system.domain.vo.DeviceInvestorVO;
import com.yuepei.system.domain.vo.TotalProportionVO;
import com.yuepei.system.mapper.DeviceMapper;
import com.yuepei.system.mapper.InvestorUserMapper;
import com.yuepei.system.service.IInvestorUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private DeviceMapper deviceMapper;

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

    @Transactional
    @Override
    public int addDevice(DeviceInvestor deviceInvestor) {
        deviceInvestor.setCreateTime(new Date());
        //TODO 修改设备投资人信息
        return investorUserMapper.addDevice(deviceInvestor);
    }

    @Override
    public int updateDevice(DeviceInvestor deviceInvestor) {
        return investorUserMapper.updateDevice(deviceInvestor);
    }

    @Transactional
    @Override
    public int deleteDeviceByIds(Long[] ids) {
        List<DeviceInvestorVO> list = deviceMapper.selectInvestorDeviceByIds(ids);
        for (int i = 0; i < list.size(); i++) {
            String investorId = list.get(i).getInvestorId();
            String userInvestorId = list.get(i).getUserInvestorId();
            Long[] longs = new Long[]{};
            JSONArray objects = JSON.parseArray(investorId);
            List<Long> toJavaList = objects.toJavaList(Long.class);
            for (int k = toJavaList.size() - 1; k >= 0; k--) {
                if (toJavaList.get(k).equals(Long.parseLong(userInvestorId))){
                    toJavaList.remove(k);
                }
            }
            list.get(i).setInvestorId(Arrays.toString(toJavaList.toArray(longs)));
        }
        deviceMapper.updateInvestorDevice(list);
        return investorUserMapper.deleteDeviceByIds(ids);
    }

    @Transactional
    @Override
    public int deleteDeviceById(Long id) {
        List<DeviceInvestorVO> list = deviceMapper.selectInvestorDeviceById(id);
        for (int i = 0; i < list.size(); i++) {
            String investorId = list.get(i).getInvestorId();
            String userInvestorId = list.get(i).getUserInvestorId();
            Long[] longs = new Long[]{};
            JSONArray objects = JSON.parseArray(investorId);
            List<Long> toJavaList = objects.toJavaList(Long.class);
            for (int k = toJavaList.size() - 1; k >= 0; k--) {
                if (toJavaList.get(k).equals(Long.parseLong(userInvestorId))){
                    toJavaList.remove(k);
                }
            }
            list.get(i).setInvestorId(Arrays.toString(toJavaList.toArray(longs)));
        }
        deviceMapper.updateInvestorDevice(list);
        return investorUserMapper.deleteDeviceById(id);
    }
}
