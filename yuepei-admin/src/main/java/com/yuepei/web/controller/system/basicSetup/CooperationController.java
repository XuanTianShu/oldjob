package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.SysUserCooperation;
import com.yuepei.system.service.ISysUserCooperationService;
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
 * @create ：2022/11/1 14:03
 **/
@RestController
@RequestMapping("/system/cooperation")
public class CooperationController extends BaseController {
    @Autowired
    private ISysUserCooperationService ISysUserCooperationService;

    /**
     * 查询合作加盟列表
     */
    @PreAuthorize("@ss.hasPermi('system:cooperation:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUserCooperation sysUserCooperation)
    {
        startPage();
        List<SysUserCooperation> list = ISysUserCooperationService.selectCooperationList(sysUserCooperation);
        return getDataTable(list);
    }

    /**
     * 获取合作加盟详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:cooperation:query')")
    @GetMapping(value = "/{cooperationId}")
    public AjaxResult getInfo(@PathVariable("cooperationId") Long cooperationId)
    {
        return AjaxResult.success(ISysUserCooperationService.selectCooperationByCooperationId(cooperationId));
    }

    /**
     * 修改合作加盟
     */
    @PreAuthorize("@ss.hasPermi('system:cooperation:edit')")
    @Log(title = "合作加盟", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUserCooperation sysUserCooperation)
    {
        return toAjax(ISysUserCooperationService.updateCooperation(sysUserCooperation));
    }

    /**
     * 删除合作加盟
     */
    @PreAuthorize("@ss.hasPermi('system:cooperation:remove')")
    @Log(title = "合作加盟", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cooperationIds}")
    public AjaxResult remove(@PathVariable Long[] cooperationIds)
    {
        return toAjax(ISysUserCooperationService.deleteCooperationByCooperationIds(cooperationIds));
    }
}
