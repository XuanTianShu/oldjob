package com.yuepei.system.service;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.BindingHospitalVO;
import com.yuepei.system.domain.vo.DeviceInvestorVO;
import com.yuepei.system.domain.vo.HospitalVO;
import com.yuepei.system.domain.vo.TotalProportionVO;

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
 * @create ：2022/11/16 10:06
 **/
public interface HospitalService {
    /**
     * 查询医院
     *
     * @param hospitalId 医院主键
     * @return 医院
     */
    public Hospital selectHospitalByHospitalId(Long hospitalId);

    /**
     * 查询医院列表
     *
     * @param hospital 医院
     * @return 医院集合
     */
    public List<Hospital> selectHospitalList(Hospital hospital);

    /**
     * 新增医院
     *
     * @param hospital 医院
     * @return 结果
     */
    public int insertHospital(Hospital hospital);

    /**
     * 修改医院
     *
     * @param hospital 医院
     * @return 结果
     */
    public int updateHospital(Hospital hospital);

    /**
     * 批量删除医院
     *
     * @param hospitalIds 需要删除的医院主键集合
     * @return 结果
     */
    public int deleteHospitalByHospitalIds(Long[] hospitalIds);

    /**
     * 删除医院信息
     *
     * @param hospitalId 医院主键
     * @return 结果
     */
    public int deleteHospitalByHospitalId(Long hospitalId);

    Map<String,Object> queryTreeByDeviceNumber(Device device);

    List<HospitalVO> selectHospitalListVO(HospitalVO hospital);

    List<SysUser> userList(SysUser sysUser);

    Hospital selectHospitalById(Long hospitalId);

    int selectDeviceByHospitals(Long[] ids);

    int selectUserByHospitals(Long[] ids);

    int selectDeviceByHospital(Long ids);

    int selectUserByHospital(Long ids);

    TotalProportionVO totalProportion2(DeviceInvestor deviceInvestor);

    List<DeviceInvestor> deviceProportionList(DeviceHospital deviceHospital);

    List<DeviceInvestorVO> unbound();

    int binding(BindingHospitalVO bindingHospitalVO);

    DeviceInvestor getDetail(Long id);

    int updateDeviceProportionById(BindingHospitalVO bindingHospitalVO);

    int deleteDeviceByIds(Long[] ids);

    int deleteDeviceById(Long id);

    int selectBindHospitalCount(Long[] deviceIds);

    List<HospitalVO> queryTreeByUser(Long hospitalId);
}
