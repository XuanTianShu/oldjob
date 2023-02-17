package com.yuepei.system.mapper;

import com.yuepei.system.domain.Instructions;

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
 * @create ：2022/11/9 10:17
 **/
public interface InstructionsMapper {
    /**
     * 查询使用说明
     *
     * @param instructionsId 使用说明主键
     * @return 使用说明
     */
    public Instructions selectInstructionsByInstructionsId(Long instructionsId);

    /**
     * 查询使用说明列表
     *
     * @param instructions 使用说明
     * @return 使用说明集合
     */
    public List<Instructions> selectInstructionsList(Instructions instructions);

    /**
     * 新增使用说明
     *
     * @param instructions 使用说明
     * @return 结果
     */
    public int insertInstructions(Instructions instructions);

    /**
     * 修改使用说明
     *
     * @param instructions 使用说明
     * @return 结果
     */
    public int updateInstructions(Instructions instructions);

    /**
     * 删除使用说明
     *
     * @param instructionsId 使用说明主键
     * @return 结果
     */
    public int deleteInstructionsByInstructionsId(Long instructionsId);

    /**
     * 批量删除使用说明
     *
     * @param instructionsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInstructionsByInstructionsIds(Long[] instructionsIds);
}
