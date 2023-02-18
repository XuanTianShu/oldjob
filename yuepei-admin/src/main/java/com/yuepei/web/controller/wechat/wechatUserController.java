package com.yuepei.web.controller.wechat;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.service.MyBalanceService;
import com.yuepei.service.MyIntegralService;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 * @create ：2022/12/16 15:46
 **/
@RestController
@RequestMapping("/system/wechatUser")
public class wechatUserController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private MyIntegralService integralService;

    @Autowired
    private MyBalanceService balanceService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:wechatUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        user.setUserType("01");
        List<SysUser> list = userService.selectWechatUserList(user);
        return getDataTable(list);
    }

    @GetMapping("/selectIntegralDetailList")
    public AjaxResult selectIntegralDetailList(UserIntegralBalanceDepositVo userIntegralBalanceDepositVo){
        return AjaxResult.success(integralService.selectIntegralDetailList(userIntegralBalanceDepositVo));
    }

    @GetMapping("/selectBalanceDetailList")
    public AjaxResult selectBalanceDetailList(UserIntegralBalanceDepositVo userIntegralBalanceDepositVo){
        return AjaxResult.success(balanceService.selectBalanceDetailList(userIntegralBalanceDepositVo));
    }


}
