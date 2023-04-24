package com.yuepei.web.controller.peoples.investor;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.constant.UserConstants;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.framework.web.domain.server.Sys;
import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.InvestorUser;
import com.yuepei.system.domain.vo.DeviceInvestorVO;
import com.yuepei.system.domain.vo.TotalProportionVO;
import com.yuepei.system.mapper.SysUserMapper;
import com.yuepei.system.service.DeviceInvestorService;
import com.yuepei.system.service.IInvestorUserService;
import com.yuepei.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.loadtime.Aj;
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
@Slf4j
@RestController
@RequestMapping("/system/investorUser")
public class InvestorUserController extends BaseController
{
    @Autowired
    private IInvestorUserService investorUserService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private DeviceInvestorService deviceInvestorService;

    /**
     * 查询投资人管理列表
     */
//    @PreAuthorize("@ss.hasPermi('system:investorUser:list')")
//    @PreAuthorize("@ss.hasAnyRoles('hospital')")
    @PreAuthorize("@ss.hasPermi('system:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        List<SysUser> list = investorUserService.selectInvestorUserList(user);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:device:list')")
    @GetMapping("/accountList")
    public TableDataInfo accountList(SysUser user)
    {
        startPage();
        List<SysUser> list = investorUserService.selectInvestorAccountUserList(user);
        return getDataTable(list);
    }

    /**
     * 投资设备
     * @param deviceInvestor
     * @return
     */
    @GetMapping("/totalProportion")
    public AjaxResult totalProportion(DeviceInvestor deviceInvestor){
        TotalProportionVO totalProportion = investorUserService.totalProportion(deviceInvestor);
        return AjaxResult.success(totalProportion);
    }

    /**
     * 根据投资人查询投资设备信息
     * @param deviceInvestor
     * @return
     */
    @GetMapping("/deviceProportionList")
    public TableDataInfo deviceProportionList(DeviceInvestor deviceInvestor){
        startPage();
        List<DeviceInvestor> list = deviceInvestorService.deviceProportionList(deviceInvestor);
        return getDataTable(list);
    }

    /**
     * 查询未投资的设备
     * @param investorId
     * @return
     */
    @GetMapping("/deviceByInvestorId/{investorId}")
    public TableDataInfo deviceByInvestorId(@PathVariable("investorId") Long investorId){
        List<DeviceInvestorVO> list = deviceInvestorService.deviceByInvestorId(investorId);
        return getDataTable(list);
    }



    /**
     * 导出投资人管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:export')")
    @Log(title = "投资人管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user)
    {
        List<SysUser> list = investorUserService.selectInvestorUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
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

    @GetMapping("/device/{id}")
    public AjaxResult getDeviceById(@PathVariable("id") Long id){
        return AjaxResult.success(deviceInvestorService.getDeviceById(id));
    }

    /**
     * 新增投资人管理
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:add')")
    @Log(title = "投资人管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUser user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhoneNumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改投资人管理
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:edit')")
    @Log(title = "投资人管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhoneNumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
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

    /**
     * 投资设备比例
     */
    @PostMapping("/addDevice")
    public AjaxResult addDevice(@RequestBody DeviceInvestor deviceInvestor){
        if (Integer.parseInt(deviceInvestor.getProportion()) <= 0){
            return AjaxResult.error("无可分配比例");
        }
        TotalProportionVO totalProportion = investorUserService.totalProportion(deviceInvestor);
        if (totalProportion.getTotalProportion() < Integer.parseInt(deviceInvestor.getProportion())){
            return AjaxResult.error("超过可分配比例");
        }
        return toAjax(investorUserService.addDevice(deviceInvestor));
    }

    /**
     * 修改设备比例
     * @param deviceInvestor
     * @return
     */
    @PutMapping("/updateDevice")
    public AjaxResult updateDevice(@RequestBody DeviceInvestor deviceInvestor){
        if (Integer.parseInt(deviceInvestor.getProportion()) <= 0){
            return AjaxResult.error("无可分配比例");
        }
        TotalProportionVO totalProportion = investorUserService.totalProportion(deviceInvestor);
        if (totalProportion.getTotalProportion() < Integer.parseInt(deviceInvestor.getProportion())){
            return AjaxResult.error("超过可分配比例");
        }
        return toAjax(investorUserService.updateDevice(deviceInvestor));
    }

    /**
     * 删除设备比例
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:remove')")
    @Log(title = "投资人管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delDevices/{ids}")
    public AjaxResult delDevice(@PathVariable Long[] ids)
    {
        return toAjax(investorUserService.deleteDeviceByIds(ids));
    }

    /**
     * 删除设备比例
     */
    @PreAuthorize("@ss.hasPermi('system:investorUser:remove')")
    @Log(title = "投资人管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delDevice/{id}")
    public AjaxResult delDevice(@PathVariable Long id)
    {
        return toAjax(investorUserService.deleteDeviceById(id));
    }

    @GetMapping("/accountProportion")
    public AjaxResult accountProportion(DeviceInvestor deviceInvestor){
        TotalProportionVO totalProportion = investorUserService.accountProportion(deviceInvestor);
        return AjaxResult.success(totalProportion);
    }
}
