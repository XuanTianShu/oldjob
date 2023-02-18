package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.Instructions;
import com.yuepei.system.mapper.InstructionsMapper;
import com.yuepei.system.service.InstructionsService;
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
 * @create ：2022/11/9 10:18
 **/
@Service
public class InstructionsServiceImpl implements InstructionsService {
    @Autowired
    private InstructionsMapper instructionsMapper;

    /**
     * 查询使用说明
     *
     * @param instructionsId 使用说明主键
     * @return 使用说明
     */
    @Override
    public Instructions selectInstructionsByInstructionsId(Long instructionsId)
    {
        return instructionsMapper.selectInstructionsByInstructionsId(instructionsId);
    }

    /**
     * 查询使用说明列表
     *
     * @param instructions 使用说明
     * @return 使用说明
     */
    @Override
    public List<Instructions> selectInstructionsList(Instructions instructions)
    {
        return instructionsMapper.selectInstructionsList(instructions);
    }

    /**
     * 新增使用说明
     *
     * @param instructions 使用说明
     * @return 结果
     */
    @Override
    public int insertInstructions(Instructions instructions)
    {
        instructions.setCreateTime(DateUtils.getNowDate());
        return instructionsMapper.insertInstructions(instructions);
    }

    /**
     * 修改使用说明
     *
     * @param instructions 使用说明
     * @return 结果
     */
    @Override
    public int updateInstructions(Instructions instructions)
    {
        return instructionsMapper.updateInstructions(instructions);
    }

    /**
     * 批量删除使用说明
     *
     * @param instructionsIds 需要删除的使用说明主键
     * @return 结果
     */
    @Override
    public int deleteInstructionsByInstructionsIds(Long[] instructionsIds)
    {
        return instructionsMapper.deleteInstructionsByInstructionsIds(instructionsIds);
    }

    /**
     * 删除使用说明信息
     *
     * @param instructionsId 使用说明主键
     * @return 结果
     */
    @Override
    public int deleteInstructionsByInstructionsId(Long instructionsId)
    {
        return instructionsMapper.deleteInstructionsByInstructionsId(instructionsId);
    }
}
