package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.MedicalProblem;
import com.yuepei.system.service.IMedicalProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 医疗Controller
 *
 * @author AK
 * @date 2023-03-24
 */
@RestController
@RequestMapping("/medical/problem")
public class MedicalProblemController extends BaseController
{
    @Autowired
    private IMedicalProblemService medicalProblemService;

    /**
     * 查询医疗列表
     */
    @PreAuthorize("@ss.hasPermi('system:problem:list')")
    @GetMapping("/list")
    public TableDataInfo list(MedicalProblem medicalProblem)
    {
        startPage();
        List<MedicalProblem> list = medicalProblemService.selectMedicalProblemList(medicalProblem);
        return getDataTable(list);
    }

    /**
     * 导出医疗列表
     */
    @PreAuthorize("@ss.hasPermi('system:problem:export')")
    @Log(title = "医疗", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MedicalProblem medicalProblem)
    {
        List<MedicalProblem> list = medicalProblemService.selectMedicalProblemList(medicalProblem);
        ExcelUtil<MedicalProblem> util = new ExcelUtil<MedicalProblem>(MedicalProblem.class);
        util.exportExcel(response, list, "医疗数据");
    }

    /**
     * 获取医疗详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:problem:query')")
    @GetMapping(value = "/{medicalProblemId}")
    public AjaxResult getInfo(@PathVariable("medicalProblemId") Long medicalProblemId)
    {
        return AjaxResult.success(medicalProblemService.selectMedicalProblemByMedicalProblemId(medicalProblemId));
    }

    /**
     * 新增医疗
     */
    @PreAuthorize("@ss.hasPermi('system:problem:add')")
    @Log(title = "医疗", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MedicalProblem medicalProblem)
    {
        return toAjax(medicalProblemService.insertMedicalProblem(medicalProblem));
    }

    /**
     * 修改医疗
     */
    @PreAuthorize("@ss.hasPermi('system:problem:edit')")
    @Log(title = "医疗", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MedicalProblem medicalProblem)
    {
        return toAjax(medicalProblemService.updateMedicalProblem(medicalProblem));
    }

    /**
     * 删除医疗
     */
    @PreAuthorize("@ss.hasPermi('system:problem:remove')")
    @Log(title = "医疗", businessType = BusinessType.DELETE)
    @DeleteMapping("/{medicalProblemIds}")
    public AjaxResult remove(@PathVariable Long[] medicalProblemIds)
    {
        return toAjax(medicalProblemService.deleteMedicalProblemByMedicalProblemIds(medicalProblemIds));
    }
}
