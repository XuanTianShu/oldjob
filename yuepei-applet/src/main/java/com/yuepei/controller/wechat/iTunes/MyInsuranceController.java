package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.mapper.UserInsuranceMapper;
import com.yuepei.system.domain.UserInsurance;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/wechat/user/insurance")
public class MyInsuranceController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserInsuranceMapper userInsuranceMapper;

    @PostMapping("/insertUserInsurance")
    public AjaxResult insertUserInsurance(HttpServletRequest request , UserInsurance userInsurance){
        SysUser user = tokenUtils.analysis(request);
        userInsurance.setUserId(user.getUserId());
        if(userInsuranceMapper.selectUserInsuranceByUserId(user.getUserId())==null){
            return AjaxResult.success(userInsuranceMapper.insertUserInsurance(userInsurance));
        }else {
            return AjaxResult.success(userInsuranceMapper.updateUserInsurance(userInsurance));
        }
    }

}
