package com.yuepei.web.controller.discount;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.DiscountRecord;
import com.yuepei.system.service.IDiscountRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 优惠券发放记录Controller
 *
 * @author ohy
 * @date 2023-02-24
 */
@RestController
@RequestMapping("/system/record")
public class DiscountRecordController extends BaseController
{
    @Autowired
    private IDiscountRecordService discountRecordService;

    /**
     * 查询优惠券发放记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(DiscountRecord discountRecord)
    {
        startPage();
        List<DiscountRecord> list = discountRecordService.selectDiscountRecordList(discountRecord);
        return getDataTable(list);
    }

    /**
     * 导出优惠券发放记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "优惠券发放记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DiscountRecord discountRecord)
    {
        List<DiscountRecord> list = discountRecordService.selectDiscountRecordList(discountRecord);
        ExcelUtil<DiscountRecord> util = new ExcelUtil<DiscountRecord>(DiscountRecord.class);
        util.exportExcel(response, list, "优惠券发放记录数据");
    }

    /**
     * 获取优惠券发放记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(discountRecordService.selectDiscountRecordById(id));
    }

    /**
     * 新增优惠券发放记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "优惠券发放记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DiscountRecord discountRecord)
    {
        return toAjax(discountRecordService.insertDiscountRecord(discountRecord));
    }

    /**
     * 修改优惠券发放记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "优惠券发放记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DiscountRecord discountRecord)
    {
        return toAjax(discountRecordService.updateDiscountRecord(discountRecord));
    }

    /**
     * 删除优惠券发放记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "优惠券发放记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(discountRecordService.deleteDiscountRecordByIds(ids));
    }
}
