package com.yuepei.web.controller.discount;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.poi.ExcelUtil;
import com.yuepei.system.domain.UserDiscount;
import com.yuepei.system.service.IUserDiscountService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户卡包Controller
 *
 * @author ohy
 * @date 2023-02-27
 */
@RestController
@RequestMapping("/system/userDiscount")
public class UserDiscountController extends BaseController
{
    @Autowired
    private IUserDiscountService userDiscountService;
    @Autowired
    private TokenUtils tokenUtils;


    /**
     * 查询用户卡包列表
     */
    @PreAuthorize("@ss.hasPermi('system:userDiscount:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserDiscount userDiscount)
    {
        startPage();
        List<UserDiscount> list = userDiscountService.selectUserDiscountList(userDiscount);
        return getDataTable(list);
    }

    /**
     * 查询用户的卡包
     * @return
     */
    @PostMapping("/selectMyDiscountByOpenId")
    public TableDataInfo selectMyDiscountByOpenId(HttpServletRequest request, UserDiscount userDiscount){
        startPage();
        System.out.println("========================================================");
        SysUser user = tokenUtils.analysis(request);
        System.out.println(user.getOpenid()+"-----------------");
        return getDataTable(userDiscountService.selectMyDiscountByOpenId(user.getOpenid(),userDiscount));
    }

    /**
     * 导出用户卡包列表
     */
    @PreAuthorize("@ss.hasPermi('system:userDiscount:export')")
    @Log(title = "用户卡包", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserDiscount userDiscount)
    {
        List<UserDiscount> list = userDiscountService.selectUserDiscountList(userDiscount);
        ExcelUtil<UserDiscount> util = new ExcelUtil<UserDiscount>(UserDiscount.class);
        util.exportExcel(response, list, "用户卡包数据");
    }

    /**
     * 获取用户卡包详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userDiscount:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(userDiscountService.selectUserDiscountById(id));
    }

    /**
     * 新增用户卡包
     */
    @PreAuthorize("@ss.hasPermi('system:userDiscount:add')")
    @Log(title = "用户卡包", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserDiscount userDiscount)
    {
        return AjaxResult.success(userDiscountService.insertUserDiscount(userDiscount));
    }

    /**
     * 修改用户卡包
     */
    @PreAuthorize("@ss.hasPermi('system:userDiscount:edit')")
    @Log(title = "用户卡包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserDiscount userDiscount)
    {
        return toAjax(userDiscountService.updateUserDiscount(userDiscount));
    }

    /**
     * 删除用户卡包
     */
    @PreAuthorize("@ss.hasPermi('system:userDiscount:remove')")
    @Log(title = "用户卡包", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userDiscountService.deleteUserDiscountByIds(ids));
    }
}
