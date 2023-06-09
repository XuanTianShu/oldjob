package com.yuepei.system.mapper;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.DeviceHospital;
import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.vo.BindingHospitalVO;
import com.yuepei.system.domain.vo.DeviceInvestorVO;
import com.yuepei.system.domain.vo.HospitalVO;
import com.yuepei.system.domain.vo.TotalProportionVO;
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
 * @create ：2022/11/16 10:05
 **/
public interface HospitalMapper {
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
     * 删除医院
     *
     * @param hospitalId 医院主键
     * @return 结果
     */
    public int deleteHospitalByHospitalId(Long hospitalId);

    /**
     * 批量删除医院
     *
     * @param hospitalIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHospitalByHospitalIds(Long[] hospitalIds);

    List<HospitalVO> selectTreeOne(@Param("hospitalId") Long hospitalId);

    List<HospitalVO> selectTree(Long[] sum);
    /**
     * 是否存在该医院
     * @param hospitalName 医院名称
     * @return
     */
    int checkHospitalName(@Param("hospitalName") String hospitalName, @Param("hospitalId") Long hospitalId);

    List<HospitalVO> selectHospitalListVO(HospitalVO hospital);

    List<SysUser> userList(SysUser sysUser);

    Hospital selectHospitalById(@Param("hospitalId") Long hospitalId);

    int selectDeviceByHospitals(Long[] ids);

    int selectUserByHospitals(Long[] ids);

    int selectDeviceByHospital(@Param("ids") Long ids);

    int selectUserByHospital(@Param("ids") Long ids);

    TotalProportionVO totalProportion2(DeviceInvestor deviceInvestor);

    List<DeviceInvestor> deviceProportionList(DeviceHospital deviceHospital);

    List<DeviceInvestorVO> unbound();

    int binding(BindingHospitalVO bindingHospitalVO);

    DeviceInvestor getDetail(@Param("id") Long id);

    int updateDeviceProportionById(BindingHospitalVO bindingHospitalVO);

    int deleteDeviceByIds(Long[] ids);

    int deleteDeviceById(@Param("id") Long id);

    List<String> selectHospitalIdList(Long[] ids);

    DeviceInvestor selectHospital(@Param("id") Long id);

    int selectBindHospitalCount(Long[] deviceIds);

    Hospital selectHospitalName(@Param("hospitalName") String hospitalName);
}
