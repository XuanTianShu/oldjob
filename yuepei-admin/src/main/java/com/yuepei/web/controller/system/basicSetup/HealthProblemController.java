package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.HealthProblem;
import com.yuepei.system.service.IHealthProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 康养Controller
 *
 * @author AK
 * @date 2023-03-24
 */
@RestController
@RequestMapping("/health/problem")
public class HealthProblemController extends BaseController
{
    @Autowired
    private IHealthProblemService healthProblemService;

    /**
     * 查询康养列表
     */
    @PreAuthorize("@ss.hasPermi('system:problem:list')")
    @GetMapping("/list")
    public TableDataInfo list(HealthProblem healthProblem)
    {
        startPage();
        List<HealthProblem> list = healthProblemService.selectHealthProblemList(healthProblem);
        return getDataTable(list);
    }

    /**
     * 导出康养列表
     */
    @PreAuthorize("@ss.hasPermi('system:problem:export')")
    @Log(title = "康养", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HealthProblem healthProblem)
    {
        List<HealthProblem> list = healthProblemService.selectHealthProblemList(healthProblem);
        ExcelUtil<HealthProblem> util = new ExcelUtil<HealthProblem>(HealthProblem.class);
        util.exportExcel(response, list, "康养数据");
    }

    /**
     * 获取康养详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:problem:query')")
    @GetMapping(value = "/{healthProblemId}")
    public AjaxResult getInfo(@PathVariable("healthProblemId") Long healthProblemId)
    {
        return AjaxResult.success(healthProblemService.selectHealthProblemByHealthProblemId(healthProblemId));
    }

    /**
     * 新增康养
     */
    @PreAuthorize("@ss.hasPermi('system:problem:add')")
    @Log(title = "康养", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HealthProblem healthProblem)
    {
        return toAjax(healthProblemService.insertHealthProblem(healthProblem));
    }

    /**
     * 修改康养
     */
    @PreAuthorize("@ss.hasPermi('system:problem:edit')")
    @Log(title = "康养", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HealthProblem healthProblem)
    {
        return toAjax(healthProblemService.updateHealthProblem(healthProblem));
    }

    /**
     * 删除康养
     */
    @PreAuthorize("@ss.hasPermi('system:problem:remove')")
    @Log(title = "康养", businessType = BusinessType.DELETE)
    @DeleteMapping("/{healthProblemIds}")
    public AjaxResult remove(@PathVariable Long[] healthProblemIds)
    {
        return toAjax(healthProblemService.deleteHealthProblemByHealthProblemIds(healthProblemIds));
    }
}
