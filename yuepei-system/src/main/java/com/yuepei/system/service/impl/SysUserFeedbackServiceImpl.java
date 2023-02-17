package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.SysUserFeedback;
import com.yuepei.system.domain.pojo.SysUserFeedbackPo;
import com.yuepei.system.mapper.SysUserFeedbackMapper;
import com.yuepei.system.service.ISysUserFeedbackService;
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
 * @create ：2022/11/2 14:41
 **/
@Service
public class SysUserFeedbackServiceImpl implements ISysUserFeedbackService {
    @Autowired
    private SysUserFeedbackMapper sysUserFeedbackMapper;

    /**
     * 查询意见反馈
     *
     * @param feedbackId 意见反馈主键
     * @return 意见反馈
     */
    @Override
    public SysUserFeedbackPo selectSysUserFeedbackByFeedbackId(Long feedbackId)
    {
        return sysUserFeedbackMapper.selectSysUserFeedbackByFeedbackId(feedbackId);
    }

    /**
     * 查询意见反馈列表
     *
     * @param sysUserFeedback 意见反馈
     * @return 意见反馈
     */
    @Override
    public List<SysUserFeedbackPo> selectSysUserFeedbackList(SysUserFeedback sysUserFeedback)
    {
        return sysUserFeedbackMapper.selectSysUserFeedbackList(sysUserFeedback);
    }

    /**
     * 新增意见反馈
     *
     * @param sysUserFeedback 意见反馈
     * @return 结果
     */
    @Override
    public int insertSysUserFeedback(SysUserFeedback sysUserFeedback)
    {
        sysUserFeedback.setStatus(0);
        sysUserFeedback.setFeedbackTime(DateUtils.getDate());
        return sysUserFeedbackMapper.insertSysUserFeedback(sysUserFeedback);
    }

    /**
     * 修改意见反馈
     *
     * @param sysUserFeedback 意见反馈
     * @return 结果
     */
    @Override
    public int updateSysUserFeedback(SysUserFeedback sysUserFeedback)
    {
        return sysUserFeedbackMapper.updateSysUserFeedback(sysUserFeedback);
    }

    /**
     * 批量删除意见反馈
     *
     * @param feedbackIds 需要删除的意见反馈主键
     * @return 结果
     */
    @Override
    public int deleteSysUserFeedbackByFeedbackIds(Long[] feedbackIds)
    {
        return sysUserFeedbackMapper.deleteSysUserFeedbackByFeedbackIds(feedbackIds);
    }

    /**
     * 删除意见反馈信息
     *
     * @param feedbackId 意见反馈主键
     * @return 结果
     */
    @Override
    public int deleteSysUserFeedbackByFeedbackId(Long feedbackId)
    {
        return sysUserFeedbackMapper.deleteSysUserFeedbackByFeedbackId(feedbackId);
    }

    @Override
    public List<SysUserFeedback> selectFaultFeedbackList(Long userId ,Long status) {
        return sysUserFeedbackMapper.selectFaultFeedbackList(userId,status);
    }

}
