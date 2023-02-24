package com.yuepei.web.controller.discount;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.DiscountThreshold;
import com.yuepei.system.service.IDiscountThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 门槛类型Controller
 *
 * @author ohy
 * @date 2023-02-24
 */
@RestController
@RequestMapping("/system/threshold")
public class DiscountThresholdController extends BaseController
{
    @Autowired
    private IDiscountThresholdService discountThresholdService;

    /**
     * 查询门槛类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:threshold:list')")
    @GetMapping("/list")
    public TableDataInfo list(DiscountThreshold discountThreshold)
    {
        startPage();
        List<DiscountThreshold> list = discountThresholdService.selectDiscountThresholdList(discountThreshold);
        return getDataTable(list);
    }

    /**
     * 导出门槛类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:threshold:export')")
    @Log(title = "门槛类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DiscountThreshold discountThreshold)
    {
        List<DiscountThreshold> list = discountThresholdService.selectDiscountThresholdList(discountThreshold);
        ExcelUtil<DiscountThreshold> util = new ExcelUtil<DiscountThreshold>(DiscountThreshold.class);
        util.exportExcel(response, list, "门槛类型数据");
    }

    /**
     * 获取门槛类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:threshold:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(discountThresholdService.selectDiscountThresholdById(id));
    }

    /**
     * 新增门槛类型
     */
    @PreAuthorize("@ss.hasPermi('system:threshold:add')")
    @Log(title = "门槛类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DiscountThreshold discountThreshold)
    {
        return toAjax(discountThresholdService.insertDiscountThreshold(discountThreshold));
    }

    /**
     * 修改门槛类型
     */
    @PreAuthorize("@ss.hasPermi('system:threshold:edit')")
    @Log(title = "门槛类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DiscountThreshold discountThreshold)
    {
        return toAjax(discountThresholdService.updateDiscountThreshold(discountThreshold));
    }

    /**
     * 删除门槛类型
     */
    @PreAuthorize("@ss.hasPermi('system:threshold:remove')")
    @Log(title = "门槛类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(discountThresholdService.deleteDiscountThresholdByIds(ids));
    }
}
