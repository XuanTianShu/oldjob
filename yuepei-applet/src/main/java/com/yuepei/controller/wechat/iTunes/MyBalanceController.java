package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.service.MyBalanceService;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 我的余额
 */
@RestController
@RequestMapping("/wechat/user/balance")
public class MyBalanceController {


    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private MyBalanceService myBalanceService;

    @GetMapping("/selectUserBalanceList")
    public AjaxResult selectUserBalanceList(HttpServletRequest request, UserIntegralBalanceDepositVo userIntegralBalanceDepositVo){

        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(myBalanceService.selectUserBalanceByOpenid(user.getOpenid(),userIntegralBalanceDepositVo));
//        return AjaxResult.success(myBalanceService.selectUserBalanceByOpenid("oc0od5efCnYrfiuqEmMj3gfNXdPg",status));

    }

}
