package com.yuepei.web.controller.peoples.replenishment;

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
 * 补货用户管理Controller
 *
 * @author ohy
 * @date 2023-02-14
 */
@RestController
@RequestMapping("/system/replenishmentUser")
public class replenishmentUserController extends BaseController {

    @Autowired
    private IInvestorUserService investorUserService;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询补货用户管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:replenishmentUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        user.setUserType("07");
        List<SysUser> list = userService.selectWechatUserList(user);
        return getDataTable(list);
    }

    /**
     * 导出补货用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:replenishmentUser:export')")
    @Log(title = "补货管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser investorUser)
    {
        List<SysUser> list = investorUserService.selectInvestorUserList(investorUser);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "补货用户数据");
    }

    /**
     * 获取补货用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:replenishmentUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(investorUserService.selectInvestorUserById(id));
    }

    /**
     * 新增补货用户
     */
    @PreAuthorize("@ss.hasPermi('system:replenishmentUser:add')")
    @Log(title = "补货管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestorUser investorUser)
    {
        return toAjax(investorUserService.insertInvestorUser(investorUser));
    }

    /**
     * 修改补货用户
     */
    @PreAuthorize("@ss.hasPermi('system:replenishmentUser:edit')")
    @Log(title = "补货管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestorUser investorUser)
    {
        return toAjax(investorUserService.updateInvestorUser(investorUser));
    }

    /**
     * 删除补货用户
     */
    @PreAuthorize("@ss.hasPermi('system:replenishmentUser:remove')")
    @Log(title = "补货管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(investorUserService.deleteInvestorUserByIds(ids));
    }
}
