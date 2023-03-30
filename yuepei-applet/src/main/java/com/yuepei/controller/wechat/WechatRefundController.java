package com.yuepei.controller.wechat;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.service.UserRefundService;
import com.yuepei.system.domain.Device;
import com.yuepei.system.service.DeviceService;
import com.yuepei.utils.TokenUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.util.HashMap;
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
 * @create ：2023/1/14 13:41
 **/
@RestController
@RequestMapping("/wechat/user/refund")
public class WechatRefundController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserRefundService userRefundService;

    /**
     * 后台手动退款
     * @param request
     * @param orderNumber
     * @return
     */
    @GetMapping("/userRefund")
    public AjaxResult userRefund(HttpServletRequest request ,String orderNumber){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(userRefundService.userRefund(user.getOpenid(),orderNumber));
    }

    /**
     * 退款回调
     * @param request
     * @return
     * @throws GeneralSecurityException
     */
    @PostMapping("/userRefundCallBack")
    public HashMap<String, String> userRefundCallBack(HttpServletRequest request) throws GeneralSecurityException {
        return userRefundService.userRefundCallBack(request);
    }


    /**
     * 用户押金退款
     * @param request
     * @return
     */
    @PostMapping("/userDepositRefund")
    public AjaxResult userDepositRefund(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(userRefundService.userDepositRefund(user.getOpenid()));
//        return AjaxResult.success(userRefundService.userDepositRefund("oc0od5efCnYrfiuqEmMj3gfNXdPg"));
    }

    //TODO 后台处理用户退款申请
}
