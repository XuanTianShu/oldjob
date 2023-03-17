package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.VideoManagement;
import com.yuepei.system.service.VideoManagementService;
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
 * @create ：2022/11/7 10:26
 **/
@RestController
@RequestMapping("/system/management")
public class VideoManagementController extends BaseController {
    @Autowired
    private VideoManagementService videoManagementService;

    /**
     * 查询视频管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:management:list')")
    @GetMapping("/list")
    public TableDataInfo list(VideoManagement videoManagement)
    {
        startPage();
        List<VideoManagement> list = videoManagementService.selectVideoManagementList(videoManagement);
        return getDataTable(list);
    }

    /**
     * 获取视频管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:management:query')")
    @GetMapping(value = "/{videoId}")
    public AjaxResult getInfo(@PathVariable("videoId") Long videoId)
    {
        return AjaxResult.success(videoManagementService.selectVideoManagementByVideoId(videoId));
    }

    /**
     * 修改视频管理
     */
    @PreAuthorize("@ss.hasPermi('system:management:edit')")
    @Log(title = "视频管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VideoManagement videoManagement)
    {
        return toAjax(videoManagementService.updateVideoManagement(videoManagement));
    }

    /**
     * 新增视频管理
     */
    @PreAuthorize("@ss.hasPermi('system:management:add')")
    @Log(title = "视频管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VideoManagement videoManagement)
    {
//        String substring = videoManagement.getVideoUrl().substring(0, videoManagement.getVideoUrl().indexOf("//"));

//        videoManagement.getVideoUrl().substring()
        return toAjax(videoManagementService.insertVideoManagement(videoManagement));
    }

    /**
     * 删除视频管理
     */
    @PreAuthorize("@ss.hasPermi('system:management:remove')")
    @Log(title = "视频管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{videoIds}")
    public AjaxResult remove(@PathVariable Long[] videoIds)
    {
        return toAjax(videoManagementService.deleteVideoManagementByVideoIds(videoIds));
    }

}
