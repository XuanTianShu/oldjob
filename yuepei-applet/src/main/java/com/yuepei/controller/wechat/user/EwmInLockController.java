package com.yuepei.controller.wechat.user;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.pojo.DevicePo;
import com.yuepei.system.domain.vo.DeviceVO;
import com.yuepei.system.mapper.UserLeaseOrderMapper;
import com.yuepei.system.service.DeviceService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

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
@RestController
@RequestMapping("/wechat/user/lock")
public class EwmInLockController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserLeaseOrderMapper userLeaseOrderMapper;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/ewmInLock")
    public AjaxResult ewmInLock(String deviceNumber, HttpServletRequest request) {
        SysUser analysis = tokenUtils.analysis(request);
        System.out.println(analysis.getOpenid()+"------------------------");
        DeviceVO device = deviceService.selectDeviceInfoByDeviceNumber(deviceNumber);
        int i = userLeaseOrderMapper.checkDeposit(analysis.getOpenid(),deviceNumber);
        System.out.println(i+"=========================");
        device.setDepositStatus(i > 0 ? 1:0);

        System.out.println(device.getDepositStatus()+"-----------------");
        if (StringUtils.isNull(device)) {
            return AjaxResult.error("该设备不存在");
        }
        return AjaxResult.success(device);
    }
}
