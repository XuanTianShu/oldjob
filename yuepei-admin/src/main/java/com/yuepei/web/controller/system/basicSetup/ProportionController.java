package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.Proportion;
import com.yuepei.system.service.IProportionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分成比例
 *
 * @author ohy
 * @date 2023-02-28
 */
@RestController
@RequestMapping("/system/proportion")
public class ProportionController extends BaseController
{
    @Autowired
    private IProportionService proportionService;

    /**
     * 查询分成比例列表
     */
    @PreAuthorize("@ss.hasPermi('system:proportion:list')")
    @GetMapping("/list")
    public TableDataInfo list(Proportion proportion)
    {
        startPage();
        List<Proportion> list = proportionService.selectProportionList(proportion);
        return getDataTable(list);
    }

    /**
     * 修改分成比例
     */
    @PreAuthorize("@ss.hasPermi('system:proportion:edit')")
    @Log(title = "分成比例", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Proportion proportion)
    {
        return toAjax(proportionService.updateProportion(proportion));
    }
}
