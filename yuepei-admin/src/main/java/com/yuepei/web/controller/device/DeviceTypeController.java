package com.yuepei.web.controller.device;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.DeviceType;
import com.yuepei.system.service.DeviceService;
import com.yuepei.system.service.DeviceTypeService;
import com.yuepei.utils.DictionaryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
 * 设备类型Controller
 * @author AK
 * @create ：2022/10/31 15:07
 **/
@RestController
@RequestMapping("/device/deviceType")
public class DeviceTypeController extends BaseController {

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 查询设备类型列表
     */
//    @PreAuthorize("@ss.hasPermi('device:deviceType:list')")
    @PreAuthorize("@ss.hasAnyRoles('hospital,agent')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceType deviceType)
    {
        startPage();
        List<DeviceType> list = deviceTypeService.selectDeviceTypeList(deviceType);
        return getDataTable(list);
    }

    /**
     * 导出设备类型列表
     */
    @PreAuthorize("@ss.hasPermi('device:deviceType:export')")
    @Log(title = "设备类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceType deviceType)
    {
        List<DeviceType> list = deviceTypeService.selectDeviceTypeList(deviceType);
        ExcelUtil<DeviceType> util = new ExcelUtil<DeviceType>(DeviceType.class);
        util.exportExcel(response, list, "设备类型数据");
    }

    /**
     * 获取设备类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:deviceType:query')")
    @GetMapping(value = "/{deviceTypeId}")
    public AjaxResult getInfo(@PathVariable("deviceTypeId") Long deviceTypeId)
    {
        return AjaxResult.success(deviceTypeService.selectDeviceTypeByDeviceTypeId(deviceTypeId));
    }

    /**
     * 新增设备类型
     */
    @PreAuthorize("@ss.hasPermi('device:deviceType:add')")
    @Log(title = "设备类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceType deviceType)
    {
        return toAjax(deviceTypeService.insertDeviceType(deviceType));
    }

    /**
     * 修改设备类型
     */
    @PreAuthorize("@ss.hasPermi('device:deviceType:edit')")
    @Log(title = "设备类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceType deviceType)
    {
        return toAjax(deviceTypeService.updateDeviceType(deviceType));
    }

    /**
     * 删除设备类型
     */
    @PreAuthorize("@ss.hasPermi('device:deviceType:remove')")
    @Log(title = "设备类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceTypeIds}")
    public AjaxResult remove(@PathVariable Long[] deviceTypeIds)
    {
        boolean b = deviceService.checkDeviceByType(deviceTypeIds);
        if (b){
            return AjaxResult.error(DictionaryEnum.CHECK_TYPE.getName());
        }

        return toAjax(deviceTypeService.deleteDeviceTypeByDeviceTypeIds(deviceTypeIds));
    }
}
