package com.yuepei.system.service;

import com.yuepei.system.domain.MedicalProblem;

import java.util.List;

/**
 * 医疗Service接口
 *
 * @author AK
 * @date 2023-03-24
 */
public interface IMedicalProblemService
{
    /**
     * 查询医疗
     *
     * @param medicalProblemId 医疗主键
     * @return 医疗
     */
    public MedicalProblem selectMedicalProblemByMedicalProblemId(Long medicalProblemId);

    /**
     * 查询医疗列表
     *
     * @param medicalProblem 医疗
     * @return 医疗集合
     */
    public List<MedicalProblem> selectMedicalProblemList(MedicalProblem medicalProblem);

    /**
     * 新增医疗
     *
     * @param medicalProblem 医疗
     * @return 结果
     */
    public int insertMedicalProblem(MedicalProblem medicalProblem);

    /**
     * 修改医疗
     *
     * @param medicalProblem 医疗
     * @return 结果
     */
    public int updateMedicalProblem(MedicalProblem medicalProblem);

    /**
     * 批量删除医疗
     *
     * @param medicalProblemIds 需要删除的医疗主键集合
     * @return 结果
     */
    public int deleteMedicalProblemByMedicalProblemIds(Long[] medicalProblemIds);

    /**
     * 删除医疗信息
     *
     * @param medicalProblemId 医疗主键
     * @return 结果
     */
    public int deleteMedicalProblemByMedicalProblemId(Long medicalProblemId);
}
