package com.yuepei.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.exception.ServiceException;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.bean.BeanValidators;
import com.yuepei.common.utils.qrCode.QrCodeUtil;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.DeviceRule;
import com.yuepei.system.domain.pojo.DevicePo;
import com.yuepei.system.domain.vo.DeviceVO;
import com.yuepei.system.domain.vo.HospitalRuleVO;
import com.yuepei.system.mapper.DeviceMapper;
import com.yuepei.system.mapper.DeviceTypeMapper;
import com.yuepei.system.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.validation.Validator;
import java.sql.SQLException;
import java.util.ArrayList;
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


    @Value("${yuepei.profile}")
    private String profile;

    @Autowired
    protected Validator validator;

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
    @Override
    public AjaxResult insertDevice(Device device)
    {
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
            }else if(device.getDeviceTypeId()==4 || device.getDeviceTypeId()==5){
                // 新增 格子柜 设备柜口
                for (int i = 1; i < 6; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("index",i);
                    jsonObject.put("status",0);
                    deviceList.add(i-1,jsonObject);
                }
                device.setRows(deviceList.toString());
            }
            deviceMapper.insertDevice(device);
        }catch (SQLException e){
            return AjaxResult.error("设备已存在");
        }
        catch (Exception ex) {
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
    @Override
    public int updateDevice(Device device)
    {
        return deviceMapper.updateDevice(device);
    }

    /**
     * 批量删除设备
     *
     * @param deviceIds 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByDeviceIds(Long[] deviceIds)
    {
        return deviceMapper.deleteDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除设备信息
     *
     * @param deviceId 设备主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByDeviceId(Long deviceId)
    {
        return deviceMapper.deleteDeviceByDeviceId(deviceId);
    }

    /**
     * 根据设备编号获取设备信息
     * @param deviceNumber 设备编号
     * @return 结果
     */
    @Override
    public DeviceVO selectDeviceInfoByDeviceNumber(String deviceNumber) {
        DeviceVO deviceRules = deviceMapper.selectDeviceInfoByDeviceNumber(deviceNumber);
        List<HospitalRuleVO> hospitalRuleVOS = JSON.parseArray(deviceRules.getHospitalRule(), HospitalRuleVO.class);
        deviceRules.setDeviceRules(hospitalRuleVOS);
        return deviceRules;
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

}
