package com.yuepei.web.controller.peoples.hospital;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.DeviceHospital;
import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.InvestorUser;
import com.yuepei.system.domain.vo.HospitalVO;
import com.yuepei.system.domain.vo.TotalProportionVO;
import com.yuepei.system.service.HospitalService;
import com.yuepei.system.service.IInvestorUserService;
import com.yuepei.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 医院管理
 *
 * @author ohy
 * @date 2023-02-14
 */
@RestController
@RequestMapping("/system/hospital")
public class HospitalUserController extends BaseController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 查询医院列表
     */
    @PreAuthorize("@ss.hasPermi('system:hospitalUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(HospitalVO hospital)
    {
        startPage();
        List<HospitalVO> list = hospitalService.selectHospitalListVO(hospital);
        return getDataTable(list);
    }

    /**
     * 获取医院详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:hospitalUser:query')")
    @GetMapping(value = "/{hospitalId}")
    public AjaxResult getInfo(@PathVariable("hospitalId") Long hospitalId)
    {
        return AjaxResult.success(hospitalService.selectHospitalById(hospitalId));
    }

    /**
     * 新增医院
     */
    @PreAuthorize("@ss.hasPermi('system:hospitalUser:add')")
    @Log(title = "医院管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Hospital hospital)
    {
        return toAjax(hospitalService.insertHospital(hospital));
    }

    /**
     * 修改医院
     */
    @PreAuthorize("@ss.hasPermi('system:hospitalUser:edit')")
    @Log(title = "医院管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Hospital hospital)
    {
        return toAjax(hospitalService.updateHospital(hospital));
    }

    /**
     * 删除医院
     */
    @PreAuthorize("@ss.hasPermi('system:hospitalUser:remove')")
    @Log(title = "医院管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteHospitalByHospitalIds/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        int i = hospitalService.selectDeviceByHospitals(ids);
        if (i > 0){
            return AjaxResult.error("该医院下存在设备无法删除！");
        }
        int k = hospitalService.selectUserByHospitals(ids);
        if (k > 0){
            return AjaxResult.error("该医院下存在用户无法删除！");
        }
        return toAjax(hospitalService.deleteHospitalByHospitalIds(ids));
    }

    /**
     * 删除医院
     */
    @PreAuthorize("@ss.hasPermi('system:hospitalUser:remove')")
    @Log(title = "医院管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteHospitalByHospitalId/{ids}")
    public AjaxResult remove(@PathVariable Long ids)
    {
        int i = hospitalService.selectDeviceByHospital(ids);
        if (i > 0){
            return AjaxResult.error("该医院下存在设备无法删除！");
        }
        int k = hospitalService.selectUserByHospital(ids);
        if (k > 0){
            return AjaxResult.error("该医院下存在用户无法删除！");
        }
        return toAjax(hospitalService.deleteHospitalByHospitalId(ids));
    }

    /**
     * 用户管理
     * @param sysUser
     * @return
     */
    @GetMapping("/userList")
    public TableDataInfo userList(SysUser sysUser){
        startPage();
        List<SysUser> list = hospitalService.userList(sysUser);
        return getDataTable(list);
    }

    /**
     * 根据医院查询投资设备信息
     * @param deviceHospital
     * @return
     */
    @GetMapping("/deviceProportionList")
    public TableDataInfo deviceProportionList(DeviceHospital deviceHospital){
        startPage();
        List<DeviceInvestor> list = hospitalService.deviceProportionList(deviceHospital);
        return getDataTable(list);
    }

    /**
     * 分成比例
     */
    @GetMapping("/totalProportion/{hospitalId}")
    public AjaxResult updateHospital(@PathVariable("hospitalId") Long hospitalId){
        TotalProportionVO totalProportionVO = hospitalService.totalProportion(hospitalId);
        return AjaxResult.success(totalProportionVO);
    }
}
