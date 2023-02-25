package com.yuepei.web.controller.peoples.investor;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.InvestorUser;
import com.yuepei.system.service.IInvestorUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 投资人管理Controller
 *
 * @author ohy
 * @date 2023-02-14
 */
@RestController
@RequestMapping("/system/investorUser")
public class InvestorUserController extends BaseController
{
    @Autowired
    private IInvestorUserService investorUserService;

    /**
     * 查询投资人管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(InvestorUser investorUser)
    {
        startPage();
        List<InvestorUser> list = investorUserService.selectInvestorUserList(investorUser);
        return getDataTable(list);
    }

    /**
     * 导出投资人管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:export')")
    @Log(title = "投资人管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InvestorUser investorUser)
    {
        List<InvestorUser> list = investorUserService.selectInvestorUserList(investorUser);
        ExcelUtil<InvestorUser> util = new ExcelUtil<InvestorUser>(InvestorUser.class);
        util.exportExcel(response, list, "投资人管理数据");
    }

    /**
     * 获取投资人管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(investorUserService.selectInvestorUserById(id));
    }

    /**
     * 新增投资人管理
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:add')")
    @Log(title = "投资人管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestorUser investorUser)
    {
        return toAjax(investorUserService.insertInvestorUser(investorUser));
    }

    /**
     * 修改投资人管理
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:edit')")
    @Log(title = "投资人管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestorUser investorUser)
    {
        return toAjax(investorUserService.updateInvestorUser(investorUser));
    }

    /**
     * 删除投资人管理
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:remove')")
    @Log(title = "投资人管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(investorUserService.deleteInvestorUserByIds(ids));
    }
}
