package com.yuepei.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.exception.ServiceException;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.bean.BeanValidators;
import com.yuepei.common.utils.qrCode.QrCodeUtil;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.pojo.DevicePo;
import com.yuepei.system.domain.vo.*;
import com.yuepei.system.mapper.*;
import com.yuepei.system.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @create ：2022/11/14 14:13
 **/
@Service
public class DeviceServiceImpl implements DeviceService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;


    @Value("${yuepei.profile}")
    private String profile;

    @Autowired
    protected Validator validator;

    @Autowired
    private DeviceInvestorMapper deviceInvestorMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private HospitalMapper hospitalMapper;

    @Autowired
    private DeviceHospitalMapper deviceHospitalMapper;

    /**
     * 查询设备
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    @Override
    public Device selectDeviceByDeviceId(Long deviceId)
    {
        return deviceMapper.selectDeviceByDeviceId(deviceId);
    }

    /**
     * 查询设备列表
     *
     * @param device 设备
     * @return 设备
     */
    @Override
    public List<Device> selectDeviceList(Device device)
    {
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 新增设备
     *
     * @param device 设备
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult insertDevice(Device device)
    {
        Long[] longs = new Long[]{};
        JSONArray objects = JSON.parseArray(device.getInvestorId());
        List<Long> list = objects.toJavaList(Long.class);
        if (list.size() > 0){
            deviceInvestorMapper.delByInvestorId(device.getDeviceNumber());
            deviceInvestorMapper.insert(list.toArray(longs),device.getDeviceNumber());
        }
        //TODO 代理商分成比例
        //TODO 医院分成比例
        try{
            //二维码是否跳转小程序暂定
            String enCode = QrCodeUtil.enCode(device.getDeviceNumber(),"https://www.yp10000.com/"+"?deviceNumber="+device.getDeviceNumber(), profile, true);
            device.setDeviceQrCode("/profile/" + enCode);
            device.setCreateTime(DateUtils.getNowDate());
            ArrayList<Object> deviceList = new ArrayList<>();
            if(device.getDeviceTypeId()==1){
                // 新增 格子柜 设备柜口
                for (int i = 1; i < 41; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("index",i);
                    jsonObject.put("goodsId","");
                    jsonObject.put("status",1);
                    deviceList.add(i-1,jsonObject);
                }
                device.setRows(deviceList.toString());
            }else if(device.getDeviceTypeId()==4){
                // 新增 格子柜 设备柜口
                for (int i = 1; i < 6; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("index",i);
                    jsonObject.put("status",0);
                    deviceList.add(i-1,jsonObject);
                }
                device.setRows(deviceList.toString());
            }else if (device.getDeviceTypeId()==5){
                // 新增 格子柜 设备柜口
                for (int i = 1; i < 4; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("index",i);
                    jsonObject.put("status",0);
                    deviceList.add(i-1,jsonObject);
                }
                device.setRows(deviceList.toString());
            }
            deviceMapper.insertDevice(device);
        }catch (SQLException e){
            e.printStackTrace();
            return AjaxResult.error("设备已存在");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return AjaxResult.error("生成二维码异常 操作失败！");
        }
        return AjaxResult.success();
    }

    /**
     * 导入设备数据
     *
     * @param devices 设备数据列表
     * @return 结果
     */
    @Override
    public String importDevice(List<Device> devices)
    {
        if (StringUtils.isNull(devices) || devices.size() == 0)
        {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Device device : devices)
        {
            try
            {
                // 验证是否存在这台设备
                Device u = deviceMapper.selectDeviceByDeviceNumber(device.getDeviceNumber());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, device);
                    AjaxResult ajaxResult = this.insertDevice(device);
                    if (ajaxResult.get("code").equals(500)) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、账号 " + "设备二维码生成有误！ 请检查设备编号："+device.getDeviceNumber());
                    } else {
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、账号 " + device.getDeviceNumber() + " 导入成功");
                    }
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + device.getDeviceNumber() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + device.getDeviceNumber() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
    /**
     * 修改设备
     *
     * @param device 设备
     * @return 结果
     */
    @Transactional
    @Override
    public int updateDevice(Device device)
    {
        log.info("type:{}",device.getType());
        Long[] longs = new Long[]{};
        JSONArray objects = JSON.parseArray(device.getInvestorId());
        List<Long> list = objects.toJavaList(Long.class);
        log.info("{}",device.getInvestorId());
        if (list.size() > 0){
            System.out.println("执行");
            int i = deviceInvestorMapper.delByInvestorId(device.getDeviceNumber());
            deviceInvestorMapper.insert(list.toArray(longs), device.getDeviceNumber());
            log.info(Arrays.toString(list.toArray(longs)) +"----"+device.getDeviceNumber());
        }
        //TODO 代理商分成比例
        if (device.getUserId() != null){
            SysUser user = new SysUser();
            user.setUserId(device.getUserId());
            user.setProportion(Long.parseLong(device.getAgentProportion()));
            sysUserMapper.updateUser(user);
        }
        if (device.getHospitalId() != 0 && device.getHospitalId() != null){
            Device device1 = deviceMapper.selectDeviceByDeviceId(device.getDeviceId());
            deviceInvestorMapper.deleteByInvestorId(device1.getDeviceNumber());
            deviceHospitalMapper.del(String.valueOf(device.getDeviceNumber()));
            DeviceHospital deviceHospital = new DeviceHospital();
            deviceHospital.setDeviceNumber(String.valueOf(device1.getDeviceNumber()));
            deviceHospital.setHospitalId(String.valueOf(device.getHospitalId()));
            deviceHospital.setProportion(device.getHospitalProportion());
            deviceHospitalMapper.insert(deviceHospital);
        }else {
            deviceHospitalMapper.del(String.valueOf(device.getDeviceNumber()));
        }
        deviceInvestorMapper.updateProportion(device.getDeviceNumber());
        return deviceMapper.updateDevice(device);
    }

    /**
     * 批量删除设备
     *
     * @param deviceIds 需要删除的设备主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteDeviceByDeviceIds(Long[] deviceIds)
    {
        List<String> list = deviceMapper.selectDeviceByDeviceIds(deviceIds);
        deviceInvestorMapper.deleteByInvestorIds(list);
        return deviceMapper.deleteDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除设备信息
     *
     * @param deviceId 设备主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteDeviceByDeviceId(Long deviceId)
    {
        Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
        deviceInvestorMapper.deleteByInvestorId(device.getDeviceNumber());
        return deviceMapper.deleteDeviceByDeviceId(deviceId);
    }

    /**
     * 根据设备编号获取设备信息
     * @param deviceNumber 设备编号
     * @return 结果
     */
    @Override
    public DeviceVO selectDeviceInfoByDeviceNumber(String deviceNumber) {
        DeviceVO device = deviceMapper.selectDeviceInfoByDeviceNumber(deviceNumber);
        if (device.getExists() > 0){
            device.setExists(1);
        }else {
            device.setExists(0);
        }
        if (device.getRule() != null){
            List<HospitalRuleVO> deviceRules = JSON.parseArray(device.getRule(), HospitalRuleVO.class);
            device.setDeviceRules(deviceRules);
        }
        return device;
    }

    /**
     * 设备类型是否绑定
     * @param deviceTypeIds
     * @return
     */
    @Override
    public boolean checkDeviceByType(Long[] deviceTypeIds) {
        int i = deviceMapper.checkDeviceByType(deviceTypeIds);
        if (i > 0){
            return true;
        }
        return false;
    }

    /**
     * 该医院是否绑定
     * @param hospitalIds
     * @return
     */
    @Override
    public boolean checkDeviceByHospitalId(Long[] hospitalIds) {
        int i = deviceMapper.checkDeviceByHospitalId(hospitalIds);
        if (i > 0){
            return true;
        }
        return false;
    }

    /**
     * 该设备是否存在
     * @param device
     * @return
     */
    @Override
    public boolean checkDeviceNumber(Device device) {
        int i = deviceMapper.checkDeviceNumber(device);
        if (i > 0){
            return true;
        }
        return false;
    }

    @Override
    public TotalProportionVO totalProportion(Device device) {
        return deviceMapper.totalProportion(device);
    }

    @Override
    public List<AgentPersonnelVO> agentPersonnel(String deviceNumber) {
        return deviceMapper.agentPersonnel(deviceNumber);
    }

    @Override
    public List<HospitalPersonnelVO> hospitalPersonnel(String deviceNumber) {
        return deviceMapper.hospitalPersonnel(deviceNumber);
    }

    @Override
    public List<InvestorPersonnelVO> investorPersonnel(String deviceNumber) {
        return deviceMapper.investorPersonnel(deviceNumber);
    }

    @Override
    public TotalProportionVO getDeviceProportion(String deviceNumber) {
        return deviceMapper.getDeviceProportion(deviceNumber);
    }

    @Override
    public TotalProportionVO getAgentProportion(Long userId) {
        System.out.println(userId);
        return deviceMapper.getAgentProportion(userId);
    }

    @Override
    public Device checkDevice(Long userId, String deviceNumber) {
        return deviceMapper.checkDevice(userId,deviceNumber);
    }

    @Override
    public Device selectDeviceByDeviceNumber(String deviceNumber) {
        return deviceMapper.selectDeviceByDeviceNumbers(deviceNumber);
    }

    @Override
    public TotalProportionVO totalProportion2(Device device) {
        return deviceMapper.totalProportion2(device);
    }

}
