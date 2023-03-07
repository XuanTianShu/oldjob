package com.yuepei.web.controller.peoples.maintenance;

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
 * 运维用户管理Controller
 *
 * @author ohy
 * @date 2023-02-14
 */
@RestController
@RequestMapping("/system/maintenanceUser")
public class maintenanceUserController extends BaseController {

    @Autowired
    private IInvestorUserService investorUserService;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询运维用户管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:maintenanceUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        user.setUserType("06");
        List<SysUser> list = userService.selectWechatUserList(user);
        return getDataTable(list);
    }

    /**
     * 导出运维用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:maintenanceUser:export')")
    @Log(title = "运维管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InvestorUser investorUser)
    {
        List<InvestorUser> list = investorUserService.selectInvestorUserList(investorUser);
        ExcelUtil<InvestorUser> util = new ExcelUtil<InvestorUser>(InvestorUser.class);
        util.exportExcel(response, list, "运维用户数据");
    }

    /**
     * 获取运维用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:maintenanceUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(investorUserService.selectInvestorUserById(id));
    }

    /**
     * 新增运维用户
     */
    @PreAuthorize("@ss.hasPermi('system:maintenanceUser:add')")
    @Log(title = "运维管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestorUser investorUser)
    {
        return toAjax(investorUserService.insertInvestorUser(investorUser));
    }

    /**
     * 修改运维用户
     */
    @PreAuthorize("@ss.hasPermi('system:maintenanceUser:edit')")
    @Log(title = "运维管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestorUser investorUser)
    {
        return toAjax(investorUserService.updateInvestorUser(investorUser));
    }

    /**
     * 删除运维用户
     */
    @PreAuthorize("@ss.hasPermi('system:maintenanceUser:remove')")
    @Log(title = "运维管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(investorUserService.deleteInvestorUserByIds(ids));
    }
}
