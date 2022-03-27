package com.yuepei.system.mapper;

import com.yuepei.system.domain.MedicalProblem;

import java.util.List;

/**
 * 医疗Mapper接口
 *
 * @author AK
 * @date 2023-03-24
 */
public interface MedicalProblemMapper
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
     * 删除医疗
     *
     * @param medicalProblemId 医疗主键
     * @return 结果
     */
    public int deleteMedicalProblemByMedicalProblemId(Long medicalProblemId);

    /**
     * 批量删除医疗
     *
     * @param medicalProblemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMedicalProblemByMedicalProblemIds(Long[] medicalProblemIds);
}
