package com.yuepei.controller.wechat.user;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.service.UnlockingService;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.pojo.DevicePo;
import com.yuepei.system.domain.vo.DeviceVO;
import com.yuepei.system.domain.vo.HospitalVO;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.system.service.DeviceService;
import com.yuepei.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
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
 * @create ：2022/12/6 15:24
 **/
@Slf4j
@RestController
@RequestMapping("/wechat/user/lock")
public class EwmInLockController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private UnlockingService unlockingService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/ewmInLock")
    public AjaxResult ewmInLock(String deviceNumber, HttpServletRequest request) {
        SysUser analysis = tokenUtils.analysis(request);
        DeviceVO device = deviceService.selectDeviceInfoByDeviceNumber(deviceNumber);
        if (StringUtils.isNull(device)) {
            return AjaxResult.error("该设备不存在");
        }
        if (device.getHospitalId() == 0){
            return AjaxResult.error("未绑定医院");
        }
        //2023888020903
        List<String> depositList = userLeaseOrderMapper.selectUserDepositList(analysis.getOpenid(),deviceNumber);
        List<String> uSerLeaseOrderDeposit = userLeaseOrderMapper.selectUSerLeaseOrderDeposit(analysis.getOpenid(),deviceNumber);
        if (depositList.size() != 0 && uSerLeaseOrderDeposit.size() != 0){
            for (String s : uSerLeaseOrderDeposit) {
                for (int k = depositList.size() - 1; k >= 0; k--) {
                    if (s.equals(depositList.get(k))) {
                        depositList.remove(k);
                    }
                }
            }
            device.setDepositList(depositList);
        }else  {
            device.setDepositList(depositList);
        }
        log.info("{}",device.getDeviceFullAddress());
        Long[] longs = new Long[]{};
        JSONArray objects = JSON.parseArray(device.getDeviceFullAddress());
        List<Long> list = objects.toJavaList(Long.class);
        Long[] longs1 = list.toArray(longs);
        List<HospitalVO> voList = deviceService.selectHospital(longs1);
        for (int i = 0; i < voList.size(); i++) {
            String hospitalName = voList.get(i).getHospitalName();
            if (i == 1){
                device.setIstrative(hospitalName);
            }
            if (i == 3){
                device.setBed(hospitalName);
            }
        }
        return AjaxResult.success(device);
    }


    /**
     * 创建设备开锁指令
     * @return
     */
    @PostMapping("/unlocking")
    public AjaxResult unlocking(@RequestBody Device device){
        return unlockingService.unlocking(device);
    }
}
