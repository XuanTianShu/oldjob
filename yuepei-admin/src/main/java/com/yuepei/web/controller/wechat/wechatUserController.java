package com.yuepei.web.controller.wechat;

import com.yuepei.common.constant.Constants;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.domain.model.LoginBody;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.framework.web.service.SysLoginService;
import com.yuepei.service.MyBalanceService;
import com.yuepei.service.MyIntegralService;
import com.yuepei.system.domain.Discount;
import com.yuepei.system.domain.DiscountRecord;
import com.yuepei.system.domain.DiscountThreshold;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.service.*;
import com.yuepei.utils.DictionaryEnum;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    @Autowired
    private IDiscountRecordService discountRecordService;

    @Autowired
    private IDiscountThresholdService discountThresholdService;

    @Autowired
    private IUserDiscountService userDiscountService;

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

    /**
     * 获取用户的积分消费记录
     * @param userIntegralBalanceDepositVo
     * @return
     */
    @GetMapping("/selectIntegralDetailList")
    public TableDataInfo selectIntegralDetailList(UserIntegralBalanceDepositVo userIntegralBalanceDepositVo){
        startPage();
        return getDataTable(integralService.selectIntegralDetailList(userIntegralBalanceDepositVo));
    }

    /**
     * 获取用户的余额消费记录
     * @param userIntegralBalanceDepositVo
     * @return
     */
    @GetMapping("/selectBalanceDetailList")
    public TableDataInfo selectBalanceDetailList(UserIntegralBalanceDepositVo userIntegralBalanceDepositVo){
        startPage();
        return getDataTable(balanceService.selectBalanceDetailList(userIntegralBalanceDepositVo));
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
        //查询该优惠券库存数量
        Discount discount = discountService.selectDiscountById(discountId);
        DiscountThreshold discountThreshold = discountThresholdService.selectDiscountThresholdById(discount.getThresholdId());
        if (discount.getUnbilledNum() >= userId.length){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar instance = Calendar.getInstance();
            String format = simpleDateFormat.format(instance.getTime());
            instance.add(Calendar.DAY_OF_MONTH,Integer.parseInt(discount.getPeriod().toString()));
            String s = simpleDateFormat.format(instance.getTime());
            discountRecordService.sendDiscountRecord(SecurityUtils.getUserId(),userId,discount,discountThreshold,new Date());
            userDiscountService.sendUserDiscount(userId,discountThreshold,simpleDateFormat.parse(format),simpleDateFormat.parse(s),discount.getMoney());
            discount.setSentNum(discount.getSentNum()+userId.length);
            discount.setUnbilledNum(discount.getUnbilledNum()-userId.length);
            discountService.updateDiscount(discount);
            return AjaxResult.success();
        }else {
            return AjaxResult.error(DictionaryEnum.CHECK_DISCOUNT_SUM.getCode(), DictionaryEnum.CHECK_DISCOUNT_SUM.getName()+discount.getUnbilledNum()+"张");
        }
    }
}
