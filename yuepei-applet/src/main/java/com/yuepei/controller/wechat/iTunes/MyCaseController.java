package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.UserCase;
import com.yuepei.mapper.UserCaseMapper;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 我的病例
 */
@RestController
@RequestMapping("/wechat/user/case")
public class MyCaseController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserCaseMapper userCaseMapper;

    @PostMapping("/insertUserCase")
    public AjaxResult insertUserCase(HttpServletRequest request,@RequestBody UserCase userCase){
        SysUser user = tokenUtils.analysis(request);
        userCase.setUserId(user.getUserId());
//        if(userCaseMapper.selectUserCaseByUserId(user.getUserId())==null){
            return AjaxResult.success(userCaseMapper.insertUserCase(userCase));
//        }else {
//            return AjaxResult.success(userCaseMapper.updateUserCase(userCase));
//        }
    }

    @GetMapping("/selectUserCaseByUserId")
    public AjaxResult selectUserCaseByUserId(HttpServletRequest request){
        SysUser user = tokenUtils.analysis(request);
        return AjaxResult.success(userCaseMapper.selectUserCaseByUserId(user.getUserId()));
    }
}
