package com.yuepei.system.service;

import com.yuepei.system.domain.SysUserFeedback;
import com.yuepei.system.domain.pojo.SysUserFeedbackPo;

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
 * @create ：2022/11/2 14:24
 **/
public interface ISysUserFeedbackService {
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
     * 批量删除意见反馈
     *
     * @param feedbackIds 需要删除的意见反馈主键集合
     * @return 结果
     */
    public int deleteSysUserFeedbackByFeedbackIds(Long[] feedbackIds);

    /**
     * 删除意见反馈信息
     *
     * @param feedbackId 意见反馈主键
     * @return 结果
     */
    public int deleteSysUserFeedbackByFeedbackId(Long feedbackId);

    public List<SysUserFeedback> selectFaultFeedbackList(Long userId,Long status,String deviceNumber);
}
