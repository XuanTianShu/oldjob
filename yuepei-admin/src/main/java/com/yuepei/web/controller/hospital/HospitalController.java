package com.yuepei.web.controller.hospital;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.vo.BindingHospitalVO;
import com.yuepei.system.domain.vo.TotalProportionVO;
import com.yuepei.system.service.DeviceService;
import com.yuepei.system.service.HospitalService;
import com.yuepei.utils.DictionaryEnum;
import com.yuepei.utils.ResultEnum;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
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
 * @create ：2022/11/16 10:08
 **/
@RestController
@RequestMapping("/device/hospital")
public class HospitalController extends BaseController {
//    @Autowired
//    private HospitalService hospitalService;
//
    @Autowired
    private DeviceService deviceService;
//
//    /**
//     * 查询医院列表
//     */
//    @PreAuthorize("@ss.hasPermi('device:hospital:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(Hospital hospital)
//    {
//        startPage();
//        List<Hospital> list = hospitalService.selectHospitalList(hospital);
//        return getDataTable(list);
//    }
//
//    /**
//     * 获取医院详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('device:hospital:query')")
//    @GetMapping(value = "/{hospitalId}")
//    public AjaxResult getInfo(@PathVariable("hospitalId") Long hospitalId)
//    {
//        return AjaxResult.success(hospitalService.selectHospitalByHospitalId(hospitalId));
//    }
//
//    /**
//     * 新增医院
//     */
//    @PreAuthorize("@ss.hasPermi('device:hospital:add')")
//    @Log(title = "医院", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody Hospital hospital)
//    {
//        if (hospital.getHospitalName() != null){
//            boolean b = hospitalService.checkHospitalName(hospital.getHospitalName(),null);
//            if (b){
//                return AjaxResult.error(ResultEnum.CODE_58.getMsg());
//            }
//        }
//        return toAjax(hospitalService.insertHospital(hospital));
//    }
//
//    /**
//     * 修改医院
//     */
//    @PreAuthorize("@ss.hasPermi('device:hospital:edit')")
//    @Log(title = "医院", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody Hospital hospital)
//    {
//        if (hospital.getHospitalName() != null){
//            boolean b = hospitalService.checkHospitalName(hospital.getHospitalName(),hospital.getHospitalId());
//            if (b){
//                return AjaxResult.error(ResultEnum.CODE_61.getMsg());
//            }
//        }
//        return toAjax(hospitalService.updateHospital(hospital));
//    }
//
//    /**
//     * 删除医院
//     */
//    @PreAuthorize("@ss.hasPermi('device:hospital:remove')")
//    @Log(title = "医院", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{hospitalIds}")
//    public AjaxResult remove(@PathVariable Long[] hospitalIds)
//    {
//        boolean b = deviceService.checkDeviceByHospitalId(hospitalIds);
//        if (b){
//            return AjaxResult.error(DictionaryEnum.CHECK_TYPE.getName());
//        }
//        return toAjax(hospitalService.deleteHospitalByHospitalIds(hospitalIds));
//    }

    @Autowired
    private HospitalService hospitalService;

    /**
     * 查询医院列表
     */
//    @PreAuthorize("@ss.hasPermi('device:hospital:list')")
    @PreAuthorize("@ss.hasAnyRoles('hospital,agent,investor')")
    @GetMapping("/list")
    public AjaxResult list(Hospital hospital)
    {
        List<Hospital> hospitals = hospitalService.selectHospitalList(hospital);
        return AjaxResult.success(hospitals);
    }

    /**
     * 导出医院列表
     */
    @PreAuthorize("@ss.hasPermi('device:hospital:export')")
    @Log(title = "医院", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Hospital hospital)
    {
        List<Hospital> list = hospitalService.selectHospitalList(hospital);
        ExcelUtil<Hospital> util = new ExcelUtil<Hospital>(Hospital.class);
        util.exportExcel(response, list, "医院数据");
    }

    /**
     * 获取医院详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:hospital:query')")
    @GetMapping(value = "/{hospitalId}")
    public AjaxResult getInfo(@PathVariable("hospitalId") Long hospitalId)
    {
        return AjaxResult.success(hospitalService.selectHospitalByHospitalId(hospitalId));
    }

    /**
     * 新增医院
     */
    @PreAuthorize("@ss.hasPermi('device:hospital:add')")
    @Log(title = "医院", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Hospital hospital)
    {

        return toAjax(hospitalService.insertHospital(hospital));
    }


    /**
     * 修改医院
     */
    @PreAuthorize("@ss.hasPermi('device:hospital:edit')")
    @Log(title = "医院", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Hospital hospital)
    {

        return toAjax(hospitalService.updateHospital(hospital));
    }

    /**
     * 删除医院
     */
    @PreAuthorize("@ss.hasPermi('device:hospital:remove')")
    @Log(title = "医院", businessType = BusinessType.DELETE)
    @DeleteMapping("/{hospitalIds}")
    public AjaxResult remove(@PathVariable Long[] hospitalIds)
    {
        return toAjax(hospitalService.deleteHospitalByHospitalIds(hospitalIds));
    }

    @GetMapping("/queryTreeByDeviceNumber")
    public AjaxResult queryTreeByDeviceNumber(Device device){
        return AjaxResult.success(hospitalService.queryTreeByDeviceNumber(device));
    }

    @GetMapping("/queryTreeByUser/{hospitalId}")
    public AjaxResult queryTreeByUser(@PathVariable("hospitalId") Long hospitalId){
        return AjaxResult.success(hospitalService.queryTreeByUser(hospitalId));
    }


    /**
     * 查询未绑定医院的设备
     * @return
     */
    @GetMapping("/unbound")
    public AjaxResult unbound(){
        return AjaxResult.success(hospitalService.unbound());
    }

    /**
     * 绑定设备的分成比例
     * @param deviceInvestor
     * @return
     */
    @GetMapping("/totalProportion")
    public AjaxResult totalProportion(DeviceInvestor deviceInvestor){
        TotalProportionVO totalProportion = hospitalService.totalProportion2(deviceInvestor);
        return AjaxResult.success(totalProportion);
    }

    /**
     * 添加未绑定医院的设备
     * @param bindingHospitalVO
     * @return
     */
    @PostMapping("/binding")
    public AjaxResult binding(@RequestBody BindingHospitalVO bindingHospitalVO){
        if (Integer.parseInt(bindingHospitalVO.getProportion()) <= 0){
            return AjaxResult.error("低于最低分配比例！");
        }
        if (bindingHospitalVO.getType().equals("0")){
            DeviceInvestor deviceInvestor = new DeviceInvestor();
            deviceInvestor.setDeviceNumber(bindingHospitalVO.getDeviceNumber());
            TotalProportionVO totalProportion = hospitalService.totalProportion2(deviceInvestor);
            if (totalProportion.getTotalProportion() < Integer.parseInt(bindingHospitalVO.getProportion())){
                return AjaxResult.error("超过最大分配比例");
            }
        }else {
            if (Integer.parseInt(bindingHospitalVO.getProportion()) > 50){
                return AjaxResult.error("超过最大分配比例");
            }
        }
        return AjaxResult.success(hospitalService.binding(bindingHospitalVO));
    }

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    @GetMapping("/getDetail/{id}")
    public AjaxResult getDetail(@PathVariable("id") Long id){
        return AjaxResult.success(hospitalService.getDetail(id));
    }


    /**
     * 修改分成比例
     * @param bindingHospitalVO
     * @return
     */
    @PostMapping("/updateDeviceProportionById")
    public AjaxResult updateDeviceProportionById(@RequestBody BindingHospitalVO bindingHospitalVO){
        if (Integer.parseInt(bindingHospitalVO.getProportion()) <= 0){
            return AjaxResult.error("低于最低分配比例！");
        }
        if (bindingHospitalVO.getType().equals("0")){
            DeviceInvestor deviceInvestor = new DeviceInvestor();
            deviceInvestor.setDeviceNumber(bindingHospitalVO.getDeviceNumber());
            TotalProportionVO totalProportion = hospitalService.totalProportion2(deviceInvestor);
            if (totalProportion.getTotalProportion() < Integer.parseInt(bindingHospitalVO.getProportion())){
                return AjaxResult.error("超过最大分配比例");
            }
        }else {
            if (Integer.parseInt(bindingHospitalVO.getProportion()) > 50){
                return AjaxResult.error("超过最大分配比例");
            }
        }
        return AjaxResult.success(hospitalService.updateDeviceProportionById(bindingHospitalVO));
    }

    /**
     * 删除设备比例
     */
    @DeleteMapping("/delDevices/{ids}")
    public AjaxResult delDevice(@PathVariable Long[] ids)
    {
        return toAjax(hospitalService.deleteDeviceByIds(ids));
    }

    /**
     * 删除设备比例
     */
    @DeleteMapping("/delDevice/{id}")
    public AjaxResult delDevice(@PathVariable Long id)
    {
        return toAjax(hospitalService.deleteDeviceById(id));
    }
}
