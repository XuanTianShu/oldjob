package com.yuepei.system.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.pojo.DevicePo;
import com.yuepei.system.domain.vo.*;
import org.apache.ibatis.annotations.Param;

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
public interface DeviceService {
    /**
     * 查询设备
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    public Device selectDeviceByDeviceId(Long deviceId);

    /**
     * 查询设备列表
     *
     * @param device 设备
     * @return 设备集合
     */
    public List<Device> selectDeviceList(Device device);

    /**
     * 新增设备
     *
     * @param device 设备
     * @return 结果
     */
    public AjaxResult insertDevice(Device device);

    /**
     * 导入用户数据
     *
     * @param devices 设备数据列表
     * @return 结果
     */
    String importDevice(List<Device> devices);

    /**
     * 修改设备
     *
     * @param device 设备
     * @return 结果
     */
    public int updateDevice(Device device);

    /**
     * 批量删除设备
     *
     * @param deviceIds 需要删除的设备主键集合
     * @return 结果
     */
    public int deleteDeviceByDeviceIds(Long[] deviceIds);

    /**
     * 删除设备信息
     *
     * @param deviceId 设备主键
     * @return 结果
     */
    public int deleteDeviceByDeviceId(Long deviceId);


    /**
     * 根据设备编号查询改设备信息
     * @param deviceNumber 设备编号
     * @return 结果
     */
    DeviceVO selectDeviceInfoByDeviceNumber(String deviceNumber);

    /**
     * 设备类型是否绑定
     * @param deviceTypeIds
     * @return
     */
    boolean checkDeviceByType(Long[] deviceTypeIds);

    /**
     * 该医院是否绑定
     * @param hospitalIds
     * @return
     */
    boolean checkDeviceByHospitalId(Long[] hospitalIds);

    /**
     * 该设备是否存在
     * @param device
     * @return
     */
    boolean checkDeviceNumber(Device device);

    TotalProportionVO totalProportion(Device device);

    List<AgentPersonnelVO> agentPersonnel(String deviceNumber);

    List<HospitalPersonnelVO> hospitalPersonnel(String deviceNumber);

    List<InvestorPersonnelVO> investorPersonnel(String deviceNumber);

    TotalProportionVO getDeviceProportion(String deviceNumber);

    TotalProportionVO getAgentProportion(Long userId);

    Device checkDevice(Long userId, String deviceNumber);

    Device selectDeviceByDeviceNumber(String deviceNumber);

    TotalProportionVO totalProportion2(Device device);

    int updateProportion(Device device);

    List<HospitalVO> selectHospital(Long[] longs1);
}

