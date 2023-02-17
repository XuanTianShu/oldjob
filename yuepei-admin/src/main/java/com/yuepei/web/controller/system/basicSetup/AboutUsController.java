package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.AboutUs;
import com.yuepei.system.service.AboutUsService;
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
 * @create ：2022/11/10 10:49
 **/
@RestController
@RequestMapping("/system/us")
public class AboutUsController extends BaseController {
    @Autowired
    private AboutUsService aboutUsService;

    /**
     * 查询关于我们列表
     */
    @PreAuthorize("@ss.hasPermi('system:us:list')")
    @GetMapping("/list")
    public TableDataInfo list(AboutUs aboutUs)
    {
        startPage();
        List<AboutUs> list = aboutUsService.selectAboutUsList(aboutUs);
        return getDataTable(list);
    }

    /**
     * 获取关于我们详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:us:query')")
    @GetMapping(value = "/{aboutUsId}")
    public AjaxResult getInfo(@PathVariable("aboutUsId") Long aboutUsId)
    {
        return AjaxResult.success(aboutUsService.selectAboutUsByAboutUsId(aboutUsId));
    }

    /**
     * 新增关于我们
     */
    @PreAuthorize("@ss.hasPermi('system:us:add')")
    @Log(title = "关于我们", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AboutUs aboutUs)
    {
        return toAjax(aboutUsService.insertAboutUs(aboutUs));
    }

    /**
     * 修改关于我们
     */
    @PreAuthorize("@ss.hasPermi('system:us:edit')")
    @Log(title = "关于我们", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AboutUs aboutUs)
    {
        return toAjax(aboutUsService.updateAboutUs(aboutUs));
    }

    /**
     * 删除关于我们
     */
    @PreAuthorize("@ss.hasPermi('system:us:remove')")
    @Log(title = "关于我们", businessType = BusinessType.DELETE)
    @DeleteMapping("/{aboutUsIds}")
    public AjaxResult remove(@PathVariable Long[] aboutUsIds)
    {
        return toAjax(aboutUsService.deleteAboutUsByAboutUsIds(aboutUsIds));
    }
}
