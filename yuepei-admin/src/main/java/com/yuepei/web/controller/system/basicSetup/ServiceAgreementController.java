package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.ServiceAgreement;
import com.yuepei.system.service.ServiceAgreementService;
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
 * @create ：2022/11/10 14:41
 **/
@RestController
@RequestMapping("/system/agreement")
public class ServiceAgreementController extends BaseController {
    @Autowired
    private ServiceAgreementService serviceAgreementService;

    /**
     * 查询服务协议列表
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:list')")
    @GetMapping("/list")
    public TableDataInfo list(ServiceAgreement serviceAgreement)
    {
        startPage();
        List<ServiceAgreement> list = serviceAgreementService.selectServiceAgreementList(serviceAgreement);
        return getDataTable(list);
    }

    /**
     * 获取服务协议详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:query')")
    @GetMapping(value = "/{serviceAgreementId}")
    public AjaxResult getInfo(@PathVariable("serviceAgreementId") Long serviceAgreementId)
    {
        return AjaxResult.success(serviceAgreementService.selectServiceAgreementByServiceAgreementId(serviceAgreementId));
    }

    /**
     * 新增服务协议
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:add')")
    @Log(title = "服务协议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ServiceAgreement serviceAgreement)
    {
        return toAjax(serviceAgreementService.insertServiceAgreement(serviceAgreement));
    }

    /**
     * 修改服务协议
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:edit')")
    @Log(title = "服务协议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ServiceAgreement serviceAgreement)
    {
        return toAjax(serviceAgreementService.updateServiceAgreement(serviceAgreement));
    }

    /**
     * 删除服务协议
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:remove')")
    @Log(title = "服务协议", businessType = BusinessType.DELETE)
    @DeleteMapping("/{serviceAgreementIds}")
    public AjaxResult remove(@PathVariable Long[] serviceAgreementIds)
    {
        return toAjax(serviceAgreementService.deleteServiceAgreementByServiceAgreementIds(serviceAgreementIds));
    }
}
