package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.mapper.UserInsuranceMapper;
import com.yuepei.system.domain.UserInsurance;
import com.yuepei.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
 * @create ：2023/1/13 11:57
 **/
@Slf4j
@RestController
@RequestMapping("/wechat/user/insurance")
public class MyInsuranceController extends BaseController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserInsuranceMapper userInsuranceMapper;

    @PostMapping("/insertUserInsurance")
    public AjaxResult insertUserInsurance(HttpServletRequest request,@RequestBody UserInsurance userInsurance){
        SysUser user = tokenUtils.analysis(request);
        log.info("{}",userInsurance.getUrlRows());
        userInsurance.setUserId(user.getUserId());
//        if(userInsuranceMapper.selectUserInsuranceByUserId(user.getUserId())==null){
            return AjaxResult.success(userInsuranceMapper.insertUserInsurance(userInsurance));
//        }else {
//            return AjaxResult.success(userInsuranceMapper.updateUserInsurance(userInsurance));
//        }
    }

    @GetMapping("/selectByUserId")
    public AjaxResult selectByUserId(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(userInsuranceMapper.selectUserInsuranceByUserId(user.getUserId()));
    }

}
