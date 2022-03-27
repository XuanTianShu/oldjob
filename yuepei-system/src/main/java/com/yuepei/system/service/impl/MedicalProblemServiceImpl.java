package com.yuepei.system.service.impl;

import com.yuepei.system.domain.MedicalProblem;
import com.yuepei.system.mapper.MedicalProblemMapper;
import com.yuepei.system.service.IMedicalProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医疗Service业务层处理
 *
 * @author AK
 * @date 2023-03-24
 */
@Service
public class MedicalProblemServiceImpl implements IMedicalProblemService
{
    @Autowired
    private MedicalProblemMapper medicalProblemMapper;

    /**
     * 查询医疗
     *
     * @param medicalProblemId 医疗主键
     * @return 医疗
     */
    @Override
    public MedicalProblem selectMedicalProblemByMedicalProblemId(Long medicalProblemId)
    {
        return medicalProblemMapper.selectMedicalProblemByMedicalProblemId(medicalProblemId);
    }

    /**
     * 查询医疗列表
     *
     * @param medicalProblem 医疗
     * @return 医疗
     */
    @Override
    public List<MedicalProblem> selectMedicalProblemList(MedicalProblem medicalProblem)
    {
        return medicalProblemMapper.selectMedicalProblemList(medicalProblem);
    }

    /**
     * 新增医疗
     *
     * @param medicalProblem 医疗
     * @return 结果
     */
    @Override
    public int insertMedicalProblem(MedicalProblem medicalProblem)
    {
        return medicalProblemMapper.insertMedicalProblem(medicalProblem);
    }

    /**
     * 修改医疗
     *
     * @param medicalProblem 医疗
     * @return 结果
     */
    @Override
    public int updateMedicalProblem(MedicalProblem medicalProblem)
    {
        return medicalProblemMapper.updateMedicalProblem(medicalProblem);
    }

    /**
     * 批量删除医疗
     *
     * @param medicalProblemIds 需要删除的医疗主键
     * @return 结果
     */
    @Override
    public int deleteMedicalProblemByMedicalProblemIds(Long[] medicalProblemIds)
    {
        return medicalProblemMapper.deleteMedicalProblemByMedicalProblemIds(medicalProblemIds);
    }

    /**
     * 删除医疗信息
     *
     * @param medicalProblemId 医疗主键
     * @return 结果
     */
    @Override
    public int deleteMedicalProblemByMedicalProblemId(Long medicalProblemId)
    {
        return medicalProblemMapper.deleteMedicalProblemByMedicalProblemId(medicalProblemId);
    }
}

