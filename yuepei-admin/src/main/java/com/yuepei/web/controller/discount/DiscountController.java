package com.yuepei.web.controller.discount;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.Discount;
import com.yuepei.system.service.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 优惠券Controller
 *
 * @author ohy
 * @date 2023-02-21
 */
@RestController
@RequestMapping("/system/discount")
public class DiscountController extends BaseController
{
    @Autowired
    private IDiscountService discountService;

    /**
     * 查询优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('system:discount:list')")
    @GetMapping("/list")
    public TableDataInfo list(Discount discount)
    {
        startPage();
        List<Discount> list = discountService.selectDiscountList(discount);
        return getDataTable(list);
    }

    /**
     * 导出优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('system:discount:export')")
    @Log(title = "优惠券", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Discount discount)
    {
        List<Discount> list = discountService.selectDiscountList(discount);
        ExcelUtil<Discount> util = new ExcelUtil<Discount>(Discount.class);
        util.exportExcel(response, list, "优惠券数据");
    }

    /**
     * 获取优惠券详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:discount:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(discountService.selectDiscountById(id));
    }

    /**
     * 新增优惠券
     */
    @PreAuthorize("@ss.hasPermi('system:discount:add')")
    @Log(title = "优惠券", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Discount discount)
    {
        return toAjax(discountService.insertDiscount(discount));
    }

    /**
     * 修改优惠券
     */
    @PreAuthorize("@ss.hasPermi('system:discount:edit')")
    @Log(title = "优惠券", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Discount discount)
    {
        return toAjax(discountService.updateDiscount(discount));
    }

    /**
     * 删除优惠券
     */
    @PreAuthorize("@ss.hasPermi('system:discount:remove')")
    @Log(title = "优惠券", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(discountService.deleteDiscountByIds(ids));
    }
}
