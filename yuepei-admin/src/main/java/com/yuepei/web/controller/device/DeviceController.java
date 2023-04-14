package com.yuepei.web.controller.device;

import com.alibaba.druid.support.json.JSONUtils;
import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.service.UnlockingService;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.vo.TotalProportionVO;
import com.yuepei.system.mapper.CarouselMapper;
import com.yuepei.system.mapper.InstructionsMapper;
import com.yuepei.system.service.*;
import com.yuepei.utils.DictionaryEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * @create ：2022/11/14 14:18
 **/
@Slf4j
@RestController
@RequestMapping("/system/device")
public class DeviceController extends BaseController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private HospitalDeviceService hospitalDeviceService;

    @Autowired
    private ServicePhoneService servicePhoneService;

    @Autowired
    private InstructionsMapper instructionsMapper;

    @Autowired
    private CarouselMapper carouselMapper;

    @Autowired
    private VideoManagementService videoManagementService;

    @Autowired
    private UnlockingService unlockingService;

    @Autowired
    private HospitalService hospitalService;


    //修改接口路径

    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(Device device)
    {
        startPage();
        List<Device> list = deviceService.selectDeviceList(device);
        return getDataTable(list);
    }

    @GetMapping("/selectDeviceType/{userId}")
    public AjaxResult selectHospital(@PathVariable("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceType(userId));
//        return AjaxResult.success(hospitalDeviceService.selectDeviceType(151L));
    }


    /**
     * 查询该医院设备详情
     * */
    @GetMapping("/selectDeviceTypeDetails")
    public AjaxResult selectDeviceTypeDetails(@RequestParam(value = "deviceTypeId",required = false) Long deviceTypeId,
                                              @RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                              @RequestParam("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceTypeDetails(deviceTypeId, userId, deviceDepartment));
    }

    /**根据医院查询详细地址*/
    @GetMapping("/selectDeviceAddress/{hospitalId}")
    public AjaxResult selectDeviceAddress(@PathVariable("hospitalId")Long hospitalId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceAddress(hospitalId));
    }

    @GetMapping("/selectDeviceAddress1/{hospitalId}")
    public AjaxResult selectDeviceAddress1(@PathVariable("hospitalId")Long hospitalId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceAddress1(hospitalId));
    }

    /**
     * 医院端设备详情编辑信息
     * */
    @GetMapping("/updateDeviceDetails")
    public AjaxResult updateDeviceDetails(@RequestParam(value = "floorId",required = false,defaultValue = "")Long floorId,
                                          @RequestParam(value = "departmentId",required = false,defaultValue = "")Long departmentId,
                                          @RequestParam(value = "roomId",required = false,defaultValue = "")Long roomId,
                                          @RequestParam(value = "bedId",required = false,defaultValue = "")Long bedId,
                                          @RequestParam(value = "deviceNumber",required = false,defaultValue = "")String deviceNumber
    ){
        hospitalDeviceService.updateDeviceDetails(floorId,departmentId,roomId,bedId,deviceNumber);
        return AjaxResult.success();
    }

    /**
     * 查询商品订单
     * */
    @GetMapping("/selectGoodsOrder/{userId}")
    public AjaxResult selectGoodsOrder(@PathVariable("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectGoodsOrder(userId));
    }

    /**查询商品详细信息*/
    @GetMapping("/selectGoodsOrderDetails")
    public AjaxResult selectGoodsOrderDetails(@RequestParam("orderId")Long orderId){
        return AjaxResult.success(hospitalDeviceService.selectOrderByOrderId(orderId));
    }

    /**设备类型下拉框*/
    @GetMapping("/selectDeviceTypeName/{userId}")
    public AjaxResult selectDeviceTypeName(@PathVariable("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectDeviceTypeName(userId));
    }

    /**选择科室下拉框*/
    @GetMapping("/selectDepartment/{userId}")
    public AjaxResult selectDepartment(@PathVariable("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectDepartment(userId));
    }

    /**
     * 陪护床租借订单
     * */
    @GetMapping("/selectLeaseOrder")
    public AjaxResult selectLeaseOrder(@RequestParam(value = "deviceDepartment",required = false,defaultValue = "") String deviceDepartment,
                                       @RequestParam(value = "deviceTypeName",required = false,defaultValue = "") String deviceTypeName,
                                       @RequestParam(value = "orderNumber",required = false,defaultValue = "") String orderNumber,
                                       @RequestParam("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrder(userId,deviceDepartment,deviceTypeName,orderNumber));
    }

    /**
     * 陪护床租借详情
     * */
    @GetMapping("/selectLeaseOrderDetails")
    public AjaxResult selectLeaseOrderDetails(@RequestParam(value = "orderNumber",required = false) String orderNumber,
                                              @RequestParam("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.selectLeaseOrderDetails(orderNumber,userId));
    }

    /**医院营收统计*/
    @GetMapping("/selectRevenueStatistics")
    public AjaxResult selectRevenueStatistics(@RequestParam("statistics")int statistics,
                                              @RequestParam("userName") String userName){
        return AjaxResult.success(hospitalDeviceService.selectRevenueStatistics(userName, statistics));
    }

    /**首页*/
    @GetMapping("/indexPage/{userId}")
    public AjaxResult indexPage(@PathVariable("userId") Long userId){
        return AjaxResult.success(hospitalDeviceService.indexPage(userId));
    }

    /**个人资料*/
    @GetMapping("/selectPersonalData/{userId}")
    public AjaxResult selectPersonalData(@PathVariable("userId")Long userId){
        return AjaxResult.success(hospitalDeviceService.selectPersonalData(userId));
    }

    /**个人中心*/
    @GetMapping("/selectPersonalCenter/{userId}")
    public AjaxResult selectPersonalCenter(@PathVariable("userId")Long userId){
        return AjaxResult.success(hospitalDeviceService.selectPersonalCenter(userId));
    }

    /**客服热线*/
    @GetMapping("/contactUsList")
    public AjaxResult contactUsList(){
        return AjaxResult.success(servicePhoneService.selectServicePhoneList(null));
    }

    /**使用说明*/
    @GetMapping("/selectInstructionsList")
    public AjaxResult selectInstructions(){
        return AjaxResult.success(videoManagementService.selectVideoManagementList(null));
    }

    /**轮播图*/
    @GetMapping("/selectCarouselList")
    public AjaxResult selectCarouselList(){
        return AjaxResult.success(carouselMapper.selectCarouselList(null));
    }
    //修改接口路径

    /**
     * 导入设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:device:import')")
    @Log(title = "设备管理", businessType = BusinessType.IMPORT)
    @PostMapping("/imports")
    public AjaxResult imports (MultipartFile file) throws Exception {
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        List<Device> devices = util.importExcel(file.getInputStream());
        String message = deviceService.importDevice(devices);
        return AjaxResult.success(message);
    }

    /**
     * 下载 设备 模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        util.importTemplateExcel(response, "设备数据");
    }
    /**
     * 获取设备详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:device:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return AjaxResult.success(deviceService.selectDeviceByDeviceId(deviceId));
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('system:device:add')")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Device device)
    {
        boolean b = deviceService.checkDeviceNumber(device);
        if (b){
            return AjaxResult.error(DictionaryEnum.CHECK_DEVICE_NUMBER.getName());
        }
        if (Double.parseDouble(device.getInvestorProportion())+Double.parseDouble(device.getAgentProportion()) > 100){
            return AjaxResult.error("该设备分成比例超过100%");
        }
        return deviceService.insertDevice(device);
    }

    /**
     * 修改设备
     */
    @PreAuthorize("@ss.hasPermi('system:device:edit')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Device device)
    {
        boolean b = deviceService.checkDeviceNumber(device);
        if (b){
            return AjaxResult.error(DictionaryEnum.CHECK_DEVICE_NUMBER.getName());
        }
        return toAjax(deviceService.updateDevice(device));
    }

    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('system:device:remove')")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(deviceService.deleteDeviceByDeviceIds(deviceIds));
    }

    /**
     * 绑定平台医院可分成比例
     * @param device
     * @return
     */
    @GetMapping("/totalProportion")
    public AjaxResult totalProportion(Device device){
        TotalProportionVO totalProportionVO = deviceService.totalProportion(device);
        return AjaxResult.success(totalProportionVO);
    }

    /**
     * 绑定代理商医院可分成比例
     * @param device
     * @return
     */
    @GetMapping("/totalProportion2")
    public AjaxResult totalProportion2(Device device){
        TotalProportionVO totalProportionVO = deviceService.totalProportion2(device);
        return AjaxResult.success(totalProportionVO);
    }

    /**
     * 获取设备人员详情
     * @param deviceNumber
     * @return
     */
    @GetMapping("/getPersonnelDetails")
    public AjaxResult getPersonnelDetails(@RequestParam("deviceNumber") String deviceNumber){
        Map<String,Object> map = new HashMap<>();
        map.put("agentPersonnel",deviceService.agentPersonnel(deviceNumber));
        map.put("hospitalPersonnel",deviceService.hospitalPersonnel(deviceNumber));
        map.put("investorPersonnel",deviceService.investorPersonnel(deviceNumber));
        return AjaxResult.success(map);
    }

    /**
     * 获取设备剩余分成比例(绑定医院)
     * @param deviceNumber
     * @return
     */
    @GetMapping("/getDeviceProportion")
    public AjaxResult getDeviceProportion(@RequestParam("deviceNumber") String deviceNumber){
        TotalProportionVO totalProportionVO = deviceService.getDeviceProportion(deviceNumber);
        return AjaxResult.success(totalProportionVO);
    }

    /**
     * 获取设备分成比例(代理商)
     * @param userId
     * @return
     */
    @GetMapping("/getAgentProportion/{userId}")
    public AjaxResult getAgentProportion(@PathVariable("userId") Long userId){
        TotalProportionVO totalProportionVO = deviceService.getAgentProportion(userId);
        return AjaxResult.success(totalProportionVO);
    }

    /**
     * 设备开锁
     * @param device 设备信息
     * @return
     */
    @PostMapping("/unlocking")
    public AjaxResult unlocking(@RequestBody Device device){
        Device device1 = deviceService.selectDeviceByDeviceId(device.getDeviceId());
        device1.setLock(device.getLock());
        return unlockingService.unlocking(device1);
    }
}
