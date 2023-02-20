package com.yuepei.web.controller.peoples.agent;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.InvestorUser;
import com.yuepei.system.service.IInvestorUserService;
import com.yuepei.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 代理商用户管理Controller
 *
 * @author ohy
 * @date 2023-02-14
 */
@RestController
@RequestMapping("/system/agentUser")
public class AgentUserController extends BaseController {
    @Autowired
    private IInvestorUserService investorUserService;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询医院用户管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        user.setUserType("05");
        List<SysUser> list = userService.selectWechatUserList(user);
        return getDataTable(list);
    }

    /**
     * 导出医院用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:export')")
    @Log(title = "投资人管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InvestorUser investorUser)
    {
        List<InvestorUser> list = investorUserService.selectInvestorUserList(investorUser);
        ExcelUtil<InvestorUser> util = new ExcelUtil<InvestorUser>(InvestorUser.class);
        util.exportExcel(response, list, "医院用户数据");
    }

    /**
     * 获取医院用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(investorUserService.selectInvestorUserById(id));
    }

    /**
     * 新增医院用户
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:add')")
    @Log(title = "投资人管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestorUser investorUser)
    {
        return toAjax(investorUserService.insertInvestorUser(investorUser));
    }

    /**
     * 修改医院用户
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:edit')")
    @Log(title = "投资人管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestorUser investorUser)
    {
        return toAjax(investorUserService.updateInvestorUser(investorUser));
    }

    /**
     * 删除医院用户
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:remove')")
    @Log(title = "投资人管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(investorUserService.deleteInvestorUserByIds(ids));
    }
}
