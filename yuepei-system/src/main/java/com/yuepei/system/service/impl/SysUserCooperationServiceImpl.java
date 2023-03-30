package com.yuepei.system.service.impl;
import com.yuepei.system.domain.SysUserCooperation;
import com.yuepei.system.mapper.SysUserCooperationMapper;
import com.yuepei.system.service.ISysUserCooperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 　　　　 ┏┓       ┏┓+ +
 * 　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　┃　　　　　　 ┃
 * 　　　　┃　　　━　　　┃ ++ + + +
 * 　　　 █████━█████  ┃+
 * 　　　　┃　　　　　　 ┃ +
 * 　　　　┃　　　┻　　　┃
 * 　　　　┃　　　　　　 ┃ + +
 * 　　　　┗━━┓　　　 ┏━┛
 * 　　　　　　┃　　  ┃
 * 　　　　　　┃　　  ┃ + + + +
 * 　　　　　　┃　　　┃　Code is far away from bug with the animal protection
 * 　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码永无bug
 * 　　　　　　┃　　　┃
 * 　　　　　　┃　　　┃　　+
 * 　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　┃ 　　　　　┣-┓
 * 　　　　　　┃ 　　　　　┏-┛
 * 　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 * *********************************************************
 *
 * @author ：AK
 * @create ：2022/11/1 13:59
 **/
@Service
public class SysUserCooperationServiceImpl implements ISysUserCooperationService {
    @Autowired
    private SysUserCooperationMapper cooperationMapper;

    /**
     * 查询合作加盟
     *
     * @param cooperationId 合作加盟主键
     * @return 合作加盟
     */
    @Override
    public SysUserCooperation selectCooperationByCooperationId(Long cooperationId)
    {
        return cooperationMapper.selectCooperationByCooperationId(cooperationId);
    }

    /**
     * 查询合作加盟列表
     *
     * @param sysUserCooperation 合作加盟
     * @return 合作加盟
     */
    @Override
    public List<SysUserCooperation> selectCooperationList(SysUserCooperation sysUserCooperation)
    {
        return cooperationMapper.selectCooperationList(sysUserCooperation);
    }

    /**
     * 新增合作加盟
     *
     * @param sysUserCooperation 合作加盟
     * @return 结果
     */
    @Override
    public int insertCooperation(SysUserCooperation sysUserCooperation)
    {
        System.out.println(sysUserCooperation.getApplyFor()+"-------------------");
        return cooperationMapper.insertCooperation(sysUserCooperation);
    }

    /**
     * 修改合作加盟
     *
     * @param sysUserCooperation 合作加盟
     * @return 结果
     */
    @Override
    public int updateCooperation(SysUserCooperation sysUserCooperation)
    {
        return cooperationMapper.updateCooperation(sysUserCooperation);
    }

    /**
     * 批量删除合作加盟
     *
     * @param cooperationIds 需要删除的合作加盟主键
     * @return 结果
     */
    @Override
    public int deleteCooperationByCooperationIds(Long[] cooperationIds)
    {
        return cooperationMapper.deleteCooperationByCooperationIds(cooperationIds);
    }

    /**
     * 删除合作加盟信息
     *
     * @param cooperationId 合作加盟主键
     * @return 结果
     */
    @Override
    public int deleteCooperationByCooperationId(Long cooperationId)
    {
        return cooperationMapper.deleteCooperationByCooperationId(cooperationId);
    }

    @Override
    public List<SysUserCooperation> selectCooperationByCooperationUserId(Long userId ,Integer status) {
        return cooperationMapper.selectCooperationByCooperationUserId(userId ,status);
    }
}
