package com.yuepei.system.mapper;

import com.yuepei.system.domain.SysUserCooperation;
import org.apache.ibatis.annotations.Param;

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
 * @author ：xiyang
 * @create ：2022/11/1 11:47
 **/
public interface SysUserCooperationMapper {
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
     * 删除合作加盟
     *
     * @param cooperationId 合作加盟主键
     * @return 结果
     */
    public int deleteCooperationByCooperationId(Long cooperationId);

    /**
     * 批量删除合作加盟
     *
     * @param cooperationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCooperationByCooperationIds(Long[] cooperationIds);

    List<SysUserCooperation> selectCooperationByCooperationUserId(@Param("userId") Long userId, @Param("status") Integer status);
}
