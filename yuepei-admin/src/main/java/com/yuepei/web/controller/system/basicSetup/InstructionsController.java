package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.Instructions;
import com.yuepei.system.service.InstructionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
 * @create ：2022/11/9 10:20
 **/
@RestController
@RequestMapping("/system/instructions")
public class InstructionsController extends BaseController {
    @Autowired
    private InstructionsService instructionsService;

    /**
     * 查询使用说明列表
     */
    @PreAuthorize("@ss.hasPermi('system:instructions:list')")
    @GetMapping("/list")
    public TableDataInfo list(Instructions instructions)
    {
        startPage();
        List<Instructions> list = instructionsService.selectInstructionsList(instructions);
        return getDataTable(list);
    }
    /**
     * 获取使用说明详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:instructions:query')")
    @GetMapping(value = "/{instructionsId}")
    public AjaxResult getInfo(@PathVariable("instructionsId") Long instructionsId)
    {
        return AjaxResult.success(instructionsService.selectInstructionsByInstructionsId(instructionsId));
    }

    /**
     * 新增使用说明
     */
    @PreAuthorize("@ss.hasPermi('system:instructions:add')")
    @Log(title = "使用说明", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Instructions instructions)
    {
        return toAjax(instructionsService.insertInstructions(instructions));
    }

    /**
     * 修改使用说明
     */
    @PreAuthorize("@ss.hasPermi('system:instructions:edit')")
    @Log(title = "使用说明", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Instructions instructions)
    {
        return toAjax(instructionsService.updateInstructions(instructions));
    }

    /**
     * 删除使用说明
     */
    @PreAuthorize("@ss.hasPermi('system:instructions:remove')")
    @Log(title = "使用说明", businessType = BusinessType.DELETE)
    @DeleteMapping("/{instructionsIds}")
    public AjaxResult remove(@PathVariable Long[] instructionsIds)
    {
        return toAjax(instructionsService.deleteInstructionsByInstructionsIds(instructionsIds));
    }
}
