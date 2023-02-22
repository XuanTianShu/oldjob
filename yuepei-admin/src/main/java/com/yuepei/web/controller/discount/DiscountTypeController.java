package com.yuepei.web.controller.discount;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.DiscountType;
import com.yuepei.system.service.IDiscountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 优惠券类型Controller
 *
 * @author ohy
 * @date 2023-02-22
 */
@RestController
@RequestMapping("/system/type")
public class DiscountTypeController extends BaseController
{
    @Autowired
    private IDiscountTypeService discountTypeService;

    /**
     * 查询优惠券类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(DiscountType discountType)
    {
        startPage();
        List<DiscountType> list = discountTypeService.selectDiscountTypeList(discountType);
        return getDataTable(list);
    }

    /**
     * 导出优惠券类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:export')")
    @Log(title = "优惠券类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DiscountType discountType)
    {
        List<DiscountType> list = discountTypeService.selectDiscountTypeList(discountType);
        ExcelUtil<DiscountType> util = new ExcelUtil<DiscountType>(DiscountType.class);
        util.exportExcel(response, list, "优惠券类型数据");
    }

    /**
     * 获取优惠券类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(discountTypeService.selectDiscountTypeById(id));
    }

    /**
     * 新增优惠券类型
     */
    @PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "优惠券类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DiscountType discountType)
    {
        return toAjax(discountTypeService.insertDiscountType(discountType));
    }

    /**
     * 修改优惠券类型
     */
    @PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "优惠券类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DiscountType discountType)
    {
        return toAjax(discountTypeService.updateDiscountType(discountType));
    }

    /**
     * 删除优惠券类型
     */
    @PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "优惠券类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(discountTypeService.deleteDiscountTypeByIds(ids));
    }
}
