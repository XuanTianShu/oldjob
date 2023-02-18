package com.yuepei.system.service.impl;

import com.yuepei.system.domain.CommonProblem;
import com.yuepei.system.mapper.CommonProblemMapper;
import com.yuepei.system.service.CommonProblemService;
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
 * @create ：2022/11/4 10:05
 **/
@Service
public class CommonProblemServiceImpl implements CommonProblemService {

    @Autowired
    private CommonProblemMapper commonProblemMapper;

    /**
     * 查询常见问题
     *
     * @param commonProblemId 常见问题主键
     * @return 常见问题
     */
    @Override
    public CommonProblem selectCommonProblemByCommonProblemId(Long commonProblemId)
    {
        return commonProblemMapper.selectCommonProblemByCommonProblemId(commonProblemId);
    }

    /**
     * 查询常见问题列表
     *
     * @param commonProblem 常见问题
     * @return 常见问题
     */
    @Override
    public List<CommonProblem> selectCommonProblemList(CommonProblem commonProblem)
    {
        return commonProblemMapper.selectCommonProblemList(commonProblem);
    }

    /**
     * 新增常见问题
     *
     * @param commonProblem 常见问题
     * @return 结果
     */
    @Override
    public int insertCommonProblem(CommonProblem commonProblem)
    {
        return commonProblemMapper.insertCommonProblem(commonProblem);
    }

    /**
     * 修改常见问题
     *
     * @param commonProblem 常见问题
     * @return 结果
     */
    @Override
    public int updateCommonProblem(CommonProblem commonProblem)
    {
        return commonProblemMapper.updateCommonProblem(commonProblem);
    }

    /**
     * 批量删除常见问题
     *
     * @param commonProblemIds 需要删除的常见问题主键
     * @return 结果
     */
    @Override
    public int deleteCommonProblemByCommonProblemIds(Long[] commonProblemIds)
    {
        return commonProblemMapper.deleteCommonProblemByCommonProblemIds(commonProblemIds);
    }

    /**
     * 删除常见问题信息
     *
     * @param commonProblemId 常见问题主键
     * @return 结果
     */
    @Override
    public int deleteCommonProblemByCommonProblemId(Long commonProblemId)
    {
        return commonProblemMapper.deleteCommonProblemByCommonProblemId(commonProblemId);
    }
}
