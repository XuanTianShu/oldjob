package com.yuepei.system.service.impl;

import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.DeviceHospital;
import com.yuepei.system.domain.DeviceInvestor;
import com.yuepei.system.domain.Hospital;
import com.yuepei.system.domain.vo.HospitalVO;
import com.yuepei.system.domain.vo.TotalProportionVO;
import com.yuepei.system.mapper.DeviceMapper;
import com.yuepei.system.mapper.HospitalMapper;
import com.yuepei.system.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
 * @create ：2022/11/16 10:07
 **/
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalMapper hospitalMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 查询医院
     *
     * @param hospitalId 医院主键
     * @return 医院
     */
    @Override
    public Hospital selectHospitalByHospitalId(Long hospitalId)
    {
        return hospitalMapper.selectHospitalByHospitalId(hospitalId);
    }

    /**
     * 查询医院列表
     *
     * @param hospital 医院
     * @return 医院
     */
    @Override
    public List<Hospital> selectHospitalList(Hospital hospital)
    {
        return hospitalMapper.selectHospitalList(hospital);
    }

    /**
     * 新增医院
     *
     * @param hospital 医院
     * @return 结果
     */
    @Override
    public int insertHospital(Hospital hospital)
    {
        hospital.setCreateTime(DateUtils.getNowDate());
        return hospitalMapper.insertHospital(hospital);
    }

    /**
     * 修改医院
     *
     * @param hospital 医院
     * @return 结果
     */
    @Override
    public int updateHospital(Hospital hospital)
    {
        hospital.setUpdateTime(DateUtils.getNowDate());
        return hospitalMapper.updateHospital(hospital);
    }

    /**
     * 批量删除医院
     *
     * @param hospitalIds 需要删除的医院主键
     * @return 结果
     */
    @Override
    public int deleteHospitalByHospitalIds(Long[] hospitalIds)
    {
        return hospitalMapper.deleteHospitalByHospitalIds(hospitalIds);
    }

    /**
     * 删除医院信息
     *
     * @param hospitalId 医院主键
     * @return 结果
     */
    @Override
    public int deleteHospitalByHospitalId(Long hospitalId)
    {
        return hospitalMapper.deleteHospitalByHospitalId(hospitalId);
    }

    @Override
    public Map<String, Object> queryTreeByDeviceNumber(Device device) {
        Map<String, Object> objectHashMap = new HashMap<>();
        Long[] one = new Long[50];
        Long[] two = new Long[50];
        Long[] three = new Long[50];
        if (device.getDeviceNumber() != null){
            Device deviceNumber = deviceMapper.selectDeviceByDeviceNumber(device.getDeviceNumber());
            List<HospitalVO> hospital = hospitalMapper.selectTreeOne(deviceNumber.getHospitalId());
            objectHashMap.put("hospital_one",hospital);
            for (int i = 0; i < hospital.size(); i++) {
                one[i] = hospital.get(i).getHospitalId();
            }
            List<HospitalVO> hospitals = hospitalMapper.selectTree(one);
            objectHashMap.put("hospital_two",hospitals);
            for (int y = 0; y < hospitals.size(); y++) {
                two[y] = hospitals.get(y).getHospitalId();
            }
            List<HospitalVO> hospitalVOS = hospitalMapper.selectTree(two);
            objectHashMap.put("hospital_three",hospitalVOS);
            for (int l = 0; l < hospitalVOS.size(); l++) {
                three[l] = hospitalVOS.get(l).getHospitalId();
            }
            List<HospitalVO> hospitalVOS1 = hospitalMapper.selectTree(three);
            objectHashMap.put("hospital_four",hospitalVOS1);
        }else {
            List<HospitalVO> hospital = hospitalMapper.selectTreeOne(device.getHospitalId());
            objectHashMap.put("hospital_one",hospital);
            for (int i = 0; i < hospital.size(); i++) {
                one[i] = hospital.get(i).getHospitalId();
            }
            List<HospitalVO> hospitals = hospitalMapper.selectTree(one);
            objectHashMap.put("hospital_two",hospitals);
            for (int y = 0; y < hospitals.size(); y++) {
                two[y] = hospitals.get(y).getHospitalId();
            }
            List<HospitalVO> hospitalVOS = hospitalMapper.selectTree(two);
            objectHashMap.put("hospital_three",hospitalVOS);
            for (int l = 0; l < hospitalVOS.size(); l++) {
                three[l] = hospitalVOS.get(l).getHospitalId();
            }
            List<HospitalVO> hospitalVOS1 = hospitalMapper.selectTree(three);
            objectHashMap.put("hospital_four",hospitalVOS1);
        }
        return objectHashMap;
    }

    @Override
    public List<HospitalVO> selectHospitalListVO(HospitalVO hospital) {
        return hospitalMapper.selectHospitalListVO(hospital);
    }

    @Override
    public List<SysUser> userList(SysUser sysUser) {
        return hospitalMapper.userList(sysUser);
    }

    @Override
    public Hospital selectHospitalById(Long hospitalId) {
        return hospitalMapper.selectHospitalById(hospitalId);
    }

    @Override
    public int selectDeviceByHospitals(Long[] ids) {
        return hospitalMapper.selectDeviceByHospitals(ids);
    }

    @Override
    public int selectUserByHospitals(Long[] ids) {
        return hospitalMapper.selectUserByHospitals(ids);
    }

    @Override
    public int selectDeviceByHospital(Long ids) {
        return hospitalMapper.selectDeviceByHospital(ids);
    }

    @Override
    public int selectUserByHospital(Long ids) {
        return hospitalMapper.selectUserByHospital(ids);
    }

    @Override
    public TotalProportionVO totalProportion(Long hospitalId) {
        return hospitalMapper.totalProportion(hospitalId);
    }

    @Override
    public List<DeviceInvestor> deviceProportionList(DeviceHospital deviceHospital) {
        return hospitalMapper.deviceProportionList(deviceHospital);
    }
}
