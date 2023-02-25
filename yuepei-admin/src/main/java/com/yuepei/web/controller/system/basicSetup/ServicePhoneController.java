package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.ServicePhone;
import com.yuepei.system.service.ServicePhoneService;
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
 * @create ：2022/11/7 15:31
 **/
@RestController
@RequestMapping("/system/service")
public class ServicePhoneController extends BaseController {
    @Autowired
    private ServicePhoneService serviceService;

    /**
     * 查询客服列表
     */
    @PreAuthorize("@ss.hasPermi('system:service:list')")
    @GetMapping("/list")
    public TableDataInfo list(ServicePhone servicePhone) {
        startPage();
        List<ServicePhone> list = serviceService.selectServicePhoneList(servicePhone);
        return getDataTable(list);
    }

    /**
     * 获取客服详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:service:query')")
    @GetMapping(value = "/{serviceId}")
    public AjaxResult getInfo(@PathVariable("serviceId") Long serviceId) {
        return AjaxResult.success(serviceService.selectServicePhoneByServiceId(serviceId));
    }

    /**
     * 新增客服
     */
    @PreAuthorize("@ss.hasPermi('system:service:add')")
    @Log(title = "客服", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ServicePhone servicePhone) {
        return toAjax(serviceService.insertServicePhone(servicePhone));
    }

    /**
     * 修改客服
     */
    @PreAuthorize("@ss.hasPermi('system:service:edit')")
    @Log(title = "客服", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ServicePhone servicePhone) {
        return toAjax(serviceService.updateServicePhone(servicePhone));
    }

    /**
     * 删除客服
     */
    @PreAuthorize("@ss.hasPermi('system:service:remove')")
    @Log(title = "客服", businessType = BusinessType.DELETE)
    @DeleteMapping("/{serviceIds}")
    public AjaxResult remove(@PathVariable Long[] serviceIds) {
        return toAjax(serviceService.deleteServicePhoneByServiceIds(serviceIds));
    }


}
