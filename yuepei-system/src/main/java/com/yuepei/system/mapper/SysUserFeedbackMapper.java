package com.yuepei.system.mapper;

import com.yuepei.system.domain.SysUserFeedback;
import com.yuepei.system.domain.pojo.SysUserFeedbackPo;
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
 * @author ：AK
 * @create ：2022/11/2 14:23
 **/
public interface SysUserFeedbackMapper {
    /**
     * 查询意见反馈
     *
     * @param feedbackId 意见反馈主键
     * @return 意见反馈
     */
    public SysUserFeedbackPo selectSysUserFeedbackByFeedbackId(Long feedbackId);

    /**
     * 查询意见反馈列表
     *
     * @param sysUserFeedback 意见反馈
     * @return 意见反馈集合
     */
    public List<SysUserFeedbackPo> selectSysUserFeedbackList(SysUserFeedback sysUserFeedback);

    /**
     * 新增意见反馈
     *
     * @param sysUserFeedback 意见反馈
     * @return 结果
     */
    public int insertSysUserFeedback(SysUserFeedback sysUserFeedback);

    /**
     * 修改意见反馈
     *
     * @param sysUserFeedback 意见反馈
     * @return 结果
     */
    public int updateSysUserFeedback(SysUserFeedback sysUserFeedback);

    /**
     * 删除意见反馈
     *
     * @param feedbackId 意见反馈主键
     * @return 结果
     */
    public int deleteSysUserFeedbackByFeedbackId(Long feedbackId);

    /**
     * 批量删除意见反馈
     *
     * @param feedbackIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUserFeedbackByFeedbackIds(Long[] feedbackIds);

    List<SysUserFeedback> selectFaultFeedbackList(@Param("userId") Long userId , @Param("status") Long status);

    List<SysUserFeedback> selectDeviceFaultList(@Param("deviceNumber") String deviceNumber,@Param("status")Integer status);
}
