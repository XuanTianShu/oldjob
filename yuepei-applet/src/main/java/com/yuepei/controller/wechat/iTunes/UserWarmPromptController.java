package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.ServiceWarmPrompt;
import com.yuepei.system.service.IServiceWarmPromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 温馨提示
 */
@RestController
@RequestMapping("/wechat/user/warmPrompt")
public class UserWarmPromptController extends BaseController {
    @Autowired
    private IServiceWarmPromptService serviceWarmPromptService;

    /**
     * 查询温馨提示列表
     */
//    @PreAuthorize("@ss.hasPermi('system:prompt:list')")
    @GetMapping("/list")
    public TableDataInfo list(ServiceWarmPrompt serviceWarmPrompt)
    {
        startPage();
        List<ServiceWarmPrompt> list = serviceWarmPromptService.selectServiceWarmPromptList(serviceWarmPrompt);
        return getDataTable(list);
    }

    /**
     * 导出温馨提示列表
     */
//    @PreAuthorize("@ss.hasPermi('system:prompt:export')")
    @Log(title = "温馨提示", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ServiceWarmPrompt serviceWarmPrompt)
    {
        List<ServiceWarmPrompt> list = serviceWarmPromptService.selectServiceWarmPromptList(serviceWarmPrompt);
        ExcelUtil<ServiceWarmPrompt> util = new ExcelUtil<ServiceWarmPrompt>(ServiceWarmPrompt.class);
        util.exportExcel(response, list, "温馨提示数据");
    }

    /**
     * 获取温馨提示详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:prompt:query')")
    @GetMapping(value = "/{serviceWarmPromptId}")
    public AjaxResult getInfo(@PathVariable("serviceWarmPromptId") Long serviceWarmPromptId)
    {
        return AjaxResult.success(serviceWarmPromptService.selectServiceWarmPromptByServiceWarmPromptId(serviceWarmPromptId));
    }

    /**
     * 新增温馨提示
     */
//    @PreAuthorize("@ss.hasPermi('system:prompt:add')")
    @Log(title = "温馨提示", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ServiceWarmPrompt serviceWarmPrompt)
    {
        return toAjax(serviceWarmPromptService.insertServiceWarmPrompt(serviceWarmPrompt));
    }

    /**
     * 修改温馨提示
     */
//    @PreAuthorize("@ss.hasPermi('system:prompt:edit')")
    @Log(title = "温馨提示", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ServiceWarmPrompt serviceWarmPrompt)
    {
        return toAjax(serviceWarmPromptService.updateServiceWarmPrompt(serviceWarmPrompt));
    }

    /**
     * 删除温馨提示
     */
//    @PreAuthorize("@ss.hasPermi('system:prompt:remove')")
    @Log(title = "温馨提示", businessType = BusinessType.DELETE)
    @DeleteMapping("/{serviceWarmPromptIds}")
    public AjaxResult remove(@PathVariable Long[] serviceWarmPromptIds)
    {
        return toAjax(serviceWarmPromptService.deleteServiceWarmPromptByServiceWarmPromptIds(serviceWarmPromptIds));
    }
}
