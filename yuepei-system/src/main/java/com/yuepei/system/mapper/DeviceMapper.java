package com.yuepei.system.mapper;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.DeviceRule;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.pojo.DevicePo;
import com.yuepei.system.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

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
 * @create ：2022/11/14 14:12
 **/
public interface DeviceMapper {
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
    public int insertDevice(Device device);

    /**
     * 修改设备
     *
     * @param device 设备
     * @return 结果
     */
    public int updateDevice(Device device);

    /**
     * 删除设备
     *
     * @param deviceId 设备主键
     * @return 结果
     */
    public int deleteDeviceByDeviceId(Long deviceId);

    /**
     * 批量删除设备
     *
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceByDeviceIds(Long[] deviceIds);

    public Device selectDeviceByDeviceNumber(String deviceNumber);

    public int updateDeviceByDeviceNumber(@Param("rows") String rows, @Param("deviceNumber") String deviceNumber, @Param("status") Integer status);

    public DeviceVO selectDeviceInfoByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    int checkDeviceByType(Long[] deviceTypeIds);

    int checkDeviceByHospitalId(Long[] hospitalIds);

    /**
     * 该设备是否存在
     * @param device
     * @return
     */
    int checkDeviceNumber(Device device);

    List<Device> selectDeviceByHospitalId(Long hospitalId);

    List<Device> selectDeviceByDeviceNumberList(List<String> device);

    List<Device> selectDeviceByUserId(@Param("userId") Long userId);

    void updateDeviceList(@Param("deviceNumber") List<String> deviceNumber,@Param("hospitalAddress") String hospitalAddress,@Param("hospitalId") Long hospitalId,@Param("userId") Long userId);

    List<String> selectDeviceByDeviceIds(Long[] deviceIds);

    List<DeviceInvestorVO> selectInvestorDeviceByIds(Long[] ids);

    List<DeviceInvestorVO> selectInvestorDeviceById(@Param("id") Long id);

    void updateInvestorDevice(@Param("list") List<DeviceInvestorVO> list);

    TotalProportionVO selectDeviceProportionDetail(@Param("deviceNumber") String deviceNumber);

    List<String> selectDeviceByHospitalIds(@Param("hospitals") List<Long> hospitals);

    TotalProportionVO totalProportion(Device device);

    List<AgentPersonnelVO> agentPersonnel(@Param("deviceNumber") String deviceNumber);

    List<HospitalPersonnelVO> hospitalPersonnel(@Param("deviceNumber") String deviceNumber);

    List<InvestorPersonnelVO> investorPersonnel(@Param("deviceNumber") String deviceNumber);

    TotalProportionVO getDeviceProportion(@Param("deviceNumber") String deviceNumber);

    TotalProportionVO getAgentProportion(@Param("userId") Long userId);

    void updateDeviceStatus(Device device);

    Device checkDevice(@Param("userId") Long userId, @Param("deviceNumber") String deviceNumber);

    Device selectDeviceByDeviceNumbers(@Param("deviceNumber") String deviceNumber);

    Device selectInvestorProportionByDeviceNumber(DeviceInvestor deviceInvestor);

    TotalProportionVO totalProportion2(Device device);

    void updateDeviceByHospitalIds(List<String> list);

    void updateDeviceByHospitalId(@Param("hospitalId") Long hospitalId);

    int updateProportion(Device device);

    List<HospitalVO> selectHospital(Long[] longs1);

    String investorProportion(@Param("deviceNumber") String deviceNumber);

    String selectAgentProportion(@Param("deviceNumber") String deviceNumber);

    void updateInvestor(PersonnelProportionVO personnelProportionVO);
}
