package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.constant.UserConstants;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.domain.model.LoginUser;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.system.service.ISysUserService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 个人信息
 */
@RestController
@RequestMapping("/wechat/user/profile")
public class MyProfileController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping
    public AjaxResult profile(HttpServletRequest request)
    {
        SysUser user = tokenUtils.analysis(request);
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(user.getUserName()));
        ajax.put("postGroup", userService.selectUserPostGroup(user.getUserName()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(HttpServletRequest request,@RequestBody SysUser user)
    {
        SysUser analysis = tokenUtils.analysis(request);
        user.setUserName(analysis.getUserName());
        user.setUserId(analysis.getUserId());
        return AjaxResult.success(userService.updateUserProfile(user));
    }
}
