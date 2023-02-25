package com.yuepei.web.controller.wechat;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.service.MyBalanceService;
import com.yuepei.service.MyIntegralService;
import com.yuepei.system.domain.Discount;
import com.yuepei.system.domain.DiscountRecord;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.service.IDiscountService;
import com.yuepei.system.service.ISysUserService;
import com.yuepei.utils.DictionaryEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Autowired
    private IDiscountService discountService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:wechatUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
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

    /**
     * 发放给指定的用户
     * @param discountId
     * @param userId
     * @return
     */
    @Transactional
    @PostMapping("/assignUser")
    public AjaxResult assignUser(Long discountId,Long[] userId) throws ParseException {
        //获取当前登录用户的id
        Long userId1 = SecurityUtils.getUserId();
        //查询该优惠券库存数量
        int b = discountService.checkDiscountSum(discountId);
        if (b > userId.length){
//            userService.selectHospitalIdByUserId(userId1);
            return AjaxResult.success();
        }else {
            return AjaxResult.error(DictionaryEnum.CHECK_DISCOUNT_SUM.getCode(), DictionaryEnum.CHECK_DISCOUNT_SUM.getName()+b+"张");
        }
    }
}
