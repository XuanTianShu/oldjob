package com.yuepei.system.service.impl;

import com.yuepei.common.annotation.DataScope;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.*;
import com.yuepei.system.domain.vo.BindingHospitalVO;
import com.yuepei.system.domain.vo.DeviceInvestorVO;
import com.yuepei.system.domain.vo.HospitalVO;
import com.yuepei.system.domain.vo.TotalProportionVO;
import com.yuepei.system.mapper.DeviceMapper;
import com.yuepei.system.mapper.HospitalMapper;
import com.yuepei.system.mapper.IAgentUserMapper;
import com.yuepei.system.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private IAgentUserMapper agentUserMapper;

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
    @DataScope(deptAlias = "hospital",userAlias = "hospital")
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
    @Transactional
    @Override
    public int insertHospital(Hospital hospital)
    {
        hospital.setCreateTime(DateUtils.getNowDate());
        int i = hospitalMapper.insertHospital(hospital);
        if (hospital.getType() != null){
            Hospital hospital1 = hospitalMapper.selectHospitalName(hospital.getHospitalName());
            return agentUserMapper.insert(hospital.getUserId(),hospital1.getHospitalId());
        }else {
            return i;
        }
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
    @Transactional
    @Override
    public int deleteHospitalByHospitalIds(Long[] hospitalIds)
    {
        //删除代理商医院绑定关系
        agentUserMapper.deleteHospitalByHospitalIds(hospitalIds);
        return hospitalMapper.deleteHospitalByHospitalIds(hospitalIds);
    }

    /**
     * 删除医院信息
     *
     * @param hospitalId 医院主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteHospitalByHospitalId(Long hospitalId)
    {
        //删除代理商医院绑定关系
        agentUserMapper.deleteHospitalByHospitalId(hospitalId);
        return hospitalMapper.deleteHospitalByHospitalId(hospitalId);
    }

    @Override
    public Map<String, Object> queryTreeByDeviceNumber(Device device) {
        Map<String, Object> objectHashMap = new HashMap<>();
        Long[] one = new Long[50];
        Long[] two = new Long[50];
        Long[] three = new Long[50];
        Long[] four = new Long[50];
        if (device.getDeviceNumber() != null){
            Device deviceNumber = deviceMapper.selectDeviceByDeviceNumber(device.getDeviceNumber());
            System.out.println(deviceNumber.getHospitalId()+"====================");
            if (deviceNumber.getHospitalId() != 0){
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
                for (int m = 0; m < hospitalVOS1.size(); m++) {
                    four[m] = hospitalVOS1.get(m).getHospitalId();
                }
            }else {
                System.out.println("执行这");
                Hospital hospital = new Hospital();
                hospital.setParentId(0L);
                List<Hospital> hospitals1 = hospitalMapper.selectHospitalList(hospital);
                objectHashMap.put("hospital_five",hospitals1);
                for (int i = 0; i < hospitals1.size(); i++) {
                    one[i] = hospitals1.get(i).getHospitalId();
                }
                List<HospitalVO> hospitals = hospitalMapper.selectTree(one);
                objectHashMap.put("hospital_one",hospitals);
                for (int y = 0; y < hospitals.size(); y++) {
                    two[y] = hospitals.get(y).getHospitalId();
                }
                List<HospitalVO> hospitalVOS = hospitalMapper.selectTree(two);
                objectHashMap.put("hospital_two",hospitalVOS);
                for (int l = 0; l < hospitalVOS.size(); l++) {
                    three[l] = hospitalVOS.get(l).getHospitalId();
                }
                List<HospitalVO> hospitalVOS1 = hospitalMapper.selectTree(three);
                objectHashMap.put("hospital_three",hospitalVOS1);
                for (int m = 0; m < hospitalVOS1.size(); m++) {
                    four[m] = hospitalVOS1.get(m).getHospitalId();
                }
                List<HospitalVO> hospitalVOS2 = hospitalMapper.selectTree(four);
                objectHashMap.put("hospital_four",hospitalVOS2);
            }
        }
        return objectHashMap;
    }

    @Override
    public List<HospitalVO> queryTreeByUser(Long hospitalId) {
        List<HospitalVO> arrayList = new ArrayList<>();
//        Map<String, Object> objectHashMap = new HashMap<>();
        Long[] one = new Long[50];
        Long[] two = new Long[50];
        Long[] three = new Long[50];
        Long[] four = new Long[50];
        List<HospitalVO> hospital = hospitalMapper.selectTreeOne(hospitalId);
        for (int i = 0; i < hospital.size(); i++) {
            arrayList.add(hospital.get(i));
            one[i] = hospital.get(i).getHospitalId();
        }
        List<HospitalVO> hospitals = hospitalMapper.selectTree(one);
//        objectHashMap.put("hospital_two",hospitals);
//        arrayList.add(hospitals);
        for (int y = 0; y < hospitals.size(); y++) {
            arrayList.add(hospitals.get(y));
            two[y] = hospitals.get(y).getHospitalId();
        }
        List<HospitalVO> hospitalVOS = hospitalMapper.selectTree(two);
//        objectHashMap.put("hospital_three",hospitalVOS);
//        arrayList.add(hospitalVOS);
        for (int l = 0; l < hospitalVOS.size(); l++) {
            arrayList.add(hospitalVOS.get(l));
            three[l] = hospitalVOS.get(l).getHospitalId();
        }
        List<HospitalVO> hospitalVOS1 = hospitalMapper.selectTree(three);
//        objectHashMap.put("hospital_four",hospitalVOS1);
//        arrayList.add(hospitalVOS1);
        for (int m = 0; m < hospitalVOS1.size(); m++) {
            arrayList.add(hospitalVOS1.get(m));
            four[m] = hospitalVOS1.get(m).getHospitalId();
        }
        return arrayList;
    }

    @Override
    @DataScope(userAlias = "h")
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
    public TotalProportionVO totalProportion2(DeviceInvestor deviceInvestor) {
        return hospitalMapper.totalProportion2(deviceInvestor);
    }

    @Override
    public List<DeviceInvestor> deviceProportionList(DeviceHospital deviceHospital) {
        return hospitalMapper.deviceProportionList(deviceHospital);
    }

    @Override
    public List<DeviceInvestorVO> unbound() {
        return hospitalMapper.unbound();
    }

    @Transactional
    @Override
    public int binding(BindingHospitalVO bindingHospitalVO) {
        Device device = new Device();
        device.setDeviceNumber(bindingHospitalVO.getDeviceNumber());
        device.setHospitalId(Long.parseLong(bindingHospitalVO.getHospitalId()));
        deviceMapper.updateDeviceStatus(device);
        return hospitalMapper.binding(bindingHospitalVO);
    }

    @Override
    public DeviceInvestor getDetail(Long id) {
        return hospitalMapper.getDetail(id);
    }

    @Override
    public int updateDeviceProportionById(BindingHospitalVO bindingHospitalVO) {
        return hospitalMapper.updateDeviceProportionById(bindingHospitalVO);
    }

    @Transactional
    @Override
    public int deleteDeviceByIds(Long[] ids) {
        List<String> list = hospitalMapper.selectHospitalIdList(ids);
        //TODO 批量更新设备
        deviceMapper.updateDeviceByHospitalIds(list);
        return hospitalMapper.deleteDeviceByIds(ids);
    }

    @Transactional
    @Override
    public int deleteDeviceById(Long id) {
        DeviceInvestor deviceInvestor = hospitalMapper.selectHospital(id);
        //TODO 更新设备
        deviceMapper.updateDeviceByHospitalId(deviceInvestor.getInvestorId());
        return hospitalMapper.deleteDeviceById(id);
    }

    @Override
    public int selectBindHospitalCount(Long[] deviceIds) {
        return hospitalMapper.selectBindHospitalCount(deviceIds);
    }
}
