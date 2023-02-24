package com.yuepei.web.controller.wechat;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.service.MyBalanceService;
import com.yuepei.service.MyIntegralService;
import com.yuepei.system.domain.Discount;
import com.yuepei.system.domain.DiscountRecord;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.service.IDiscountService;
import com.yuepei.system.service.ISysUserService;
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


    /**
     * 发放给指定的用户
     * @param discountId
     * @param userId
     * @return
     */
//    @Transactional
//    @PostMapping("/assignUser")
//    public AjaxResult assignUser(Long discountId,Long[] userId) throws ParseException {
//        Discount discount = discountService.selectDiscountById(discountId);
//        List<DiscountRecord> list = new ArrayList<>();
//        List<UserDiscount> userDiscountList = new ArrayList<>();
//        for (int i = 0; i < userId.length; i++) {
//            DiscountRecord discountRecord = new DiscountRecord();
//            discountRecord.setUserid(userId[i]);
//            discountRecord.setThreshold(discount.getThresholdName());
//            discountRecord.setPrice(discount.getPrice());
//            discountRecord.setGrantTime(new Date());
//            discountRecord.setDiscountTypeName(discount.getDiscountTypeName());
//            list.add(discountRecord);
//            UserDiscount userDiscount = new UserDiscount();
//            userDiscount.setUserId(userId[i]);
//            com.fttiot.common.utils.bean.BeanUtils.copyProperties(discount,userDiscount);
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar instance = Calendar.getInstance();
//            String format = simpleDateFormat.format(instance.getTime());
//            userDiscount.setCreateTime(simpleDateFormat.parse(format));
//            instance.add(Calendar.DAY_OF_MONTH,Integer.parseInt(discount.getPeriod().toString()));
//            String s = simpleDateFormat.format(instance.getTime());
//            userDiscount.setExpireTime(simpleDateFormat.parse(s));
//            userDiscountList.add(userDiscount);
//        }
//        discountRecordService.saveBatchCustom(list);
//
//        return toAjax(userDiscountService.saveBatchCustom(userDiscountList));
//    }
}
