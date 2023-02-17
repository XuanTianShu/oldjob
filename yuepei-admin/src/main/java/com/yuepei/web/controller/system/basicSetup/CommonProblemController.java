package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.CommonProblem;
import com.yuepei.system.service.CommonProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
 * @create ：2022/11/4 10:07
 **/
@RestController
@RequestMapping("/system/problem")
public class CommonProblemController extends BaseController {
    @Autowired
    private CommonProblemService commonProblemService;

    /**
     * 查询常见问题列表
     */
    @PreAuthorize("@ss.hasPermi('system:problem:list')")
    @GetMapping("/list")
    public TableDataInfo list(CommonProblem commonProblem)
    {
        startPage();
        List<CommonProblem> list = commonProblemService.selectCommonProblemList(commonProblem);
        return getDataTable(list);
    }

    /**
     * 获取常见问题详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:problem:query')")
    @GetMapping(value = "/{commonProblemId}")
    public AjaxResult getInfo(@PathVariable("commonProblemId") Long commonProblemId)
    {
        return AjaxResult.success(commonProblemService.selectCommonProblemByCommonProblemId(commonProblemId));
    }

    /**
     * 新增常见问题
     */
    @PreAuthorize("@ss.hasPermi('system:problem:add')")
    @Log(title = "常见问题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CommonProblem commonProblem)
    {
        return toAjax(commonProblemService.insertCommonProblem(commonProblem));
    }

    /**
     * 修改常见问题
     */
    @PreAuthorize("@ss.hasPermi('system:problem:edit')")
    @Log(title = "常见问题", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CommonProblem commonProblem)
    {
        return toAjax(commonProblemService.updateCommonProblem(commonProblem));
    }

    /**
     * 删除常见问题
     */
    @PreAuthorize("@ss.hasPermi('system:problem:remove')")
    @Log(title = "常见问题", businessType = BusinessType.DELETE)
    @DeleteMapping("/{commonProblemIds}")
    public AjaxResult remove(@PathVariable Long[] commonProblemIds)
    {
        return toAjax(commonProblemService.deleteCommonProblemByCommonProblemIds(commonProblemIds));
    }
}
