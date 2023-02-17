package com.yuepei.system.service;

import com.yuepei.system.domain.CommonProblem;

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
 * @create ：2022/11/4 10:04
 **/
public interface CommonProblemService {
    /**
     * 查询常见问题
     *
     * @param commonProblemId 常见问题主键
     * @return 常见问题
     */
    public CommonProblem selectCommonProblemByCommonProblemId(Long commonProblemId);

    /**
     * 查询常见问题列表
     *
     * @param commonProblem 常见问题
     * @return 常见问题集合
     */
    public List<CommonProblem> selectCommonProblemList(CommonProblem commonProblem);

    /**
     * 新增常见问题
     *
     * @param commonProblem 常见问题
     * @return 结果
     */
    public int insertCommonProblem(CommonProblem commonProblem);

    /**
     * 修改常见问题
     *
     * @param commonProblem 常见问题
     * @return 结果
     */
    public int updateCommonProblem(CommonProblem commonProblem);

    /**
     * 批量删除常见问题
     *
     * @param commonProblemIds 需要删除的常见问题主键集合
     * @return 结果
     */
    public int deleteCommonProblemByCommonProblemIds(Long[] commonProblemIds);

    /**
     * 删除常见问题信息
     *
     * @param commonProblemId 常见问题主键
     * @return 结果
     */
    public int deleteCommonProblemByCommonProblemId(Long commonProblemId);
}
