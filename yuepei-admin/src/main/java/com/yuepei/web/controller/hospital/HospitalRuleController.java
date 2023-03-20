package com.yuepei.web.controller.hospital;
import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.DeviceRule;
import com.yuepei.system.service.IDeviceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 医院套餐Controller
 *
 * @author ohy
 * @date 2023-02-10
 */
@RestController
@RequestMapping("/system/rule")
public class HospitalRuleController extends BaseController
{
    @Autowired
    private IDeviceRuleService deviceRuleService;

    /**
     * 查询医院套餐列表
     */
    @PreAuthorize("@ss.hasPermi('system:rule:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceRule deviceRule)
    {
        startPage();
        List<DeviceRule> list = deviceRuleService.selectDeviceRuleList(deviceRule);
        return getDataTable(list);
    }

    /**
     * 导出医院套餐列表
     */
    @PreAuthorize("@ss.hasPermi('system:rule:export')")
    @Log(title = "医院套餐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceRule deviceRule)
    {
        List<DeviceRule> list = deviceRuleService.selectDeviceRuleList(deviceRule);
        ExcelUtil<DeviceRule> util = new ExcelUtil<DeviceRule>(DeviceRule.class);
        util.exportExcel(response, list, "医院套餐数据");
    }

    /**
     * 获取该设备套餐详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:rule:query')")
    @GetMapping(value = "/{deviceNumber}")
    public AjaxResult getInfo(@PathVariable("deviceNumber") String deviceNumber)
    {
        return AjaxResult.success(deviceRuleService.selectDeviceRuleByDeviceNumber(deviceNumber));
    }

    /**
     * 根据医院查询套餐
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:rule:query')")
    @GetMapping(value = "/selectHospitalRule/{id}")
    public AjaxResult selectHospitalRule(@PathVariable("id") Long id)
    {
        return AjaxResult.success(deviceRuleService.selectHospitalRule(id));
    }

    /**
     * 新增设备套餐
     */
    @PreAuthorize("@ss.hasPermi('system:rule:add')")
    @Log(title = "设备套餐", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceRule deviceRule)
    {
        return toAjax(deviceRuleService.insertDeviceRule(deviceRule));
    }

    /**
     * 修改设备套餐
     */
    @PreAuthorize("@ss.hasPermi('system:rule:edit')")
    @Log(title = "设备套餐", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceRule deviceRule)
    {
        return toAjax(deviceRuleService.updateDeviceRule(deviceRule));
    }

    /**
     * 删除医院套餐
     */
    @PreAuthorize("@ss.hasPermi('system:rule:remove')")
    @Log(title = "医院套餐", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deviceRuleService.deleteDeviceRuleByIds(ids));
    }
}
