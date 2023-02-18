package com.yuepei.system.service;

import com.yuepei.system.domain.SysUserCooperation;

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
 * @create ：2022/11/1 13:58
 **/
public interface ISysUserCooperationService {
    /**
     * 查询合作加盟
     *
     * @param cooperationId 合作加盟主键
     * @return 合作加盟
     */
    public SysUserCooperation selectCooperationByCooperationId(Long cooperationId);

    /**
     * 查询合作加盟列表
     *
     * @param sysUserCooperation 合作加盟
     * @return 合作加盟集合
     */
    public List<SysUserCooperation> selectCooperationList(SysUserCooperation sysUserCooperation);

    /**
     * 新增合作加盟
     *
     * @param sysUserCooperation 合作加盟
     * @return 结果
     */
    public int insertCooperation(SysUserCooperation sysUserCooperation);

    /**
     * 修改合作加盟
     *
     * @param sysUserCooperation 合作加盟
     * @return 结果
     */
    public int updateCooperation(SysUserCooperation sysUserCooperation);

    /**
     * 批量删除合作加盟
     *
     * @param cooperationIds 需要删除的合作加盟主键集合
     * @return 结果
     */
    public int deleteCooperationByCooperationIds(Long[] cooperationIds);

    /**
     * 删除合作加盟信息
     *
     * @param cooperationId 合作加盟主键
     * @return 结果
     */


    public int deleteCooperationByCooperationId(Long cooperationId);

    List<SysUserCooperation> selectCooperationByCooperationUserId(Long userId,Integer status);

}
