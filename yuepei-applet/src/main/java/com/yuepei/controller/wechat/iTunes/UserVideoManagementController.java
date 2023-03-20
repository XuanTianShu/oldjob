package com.yuepei.controller.wechat.iTunes;

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
 * 使用说明视频
 */
@RestController
@RequestMapping("/wechat/user/management")
public class UserVideoManagementController extends BaseController {
    @Autowired
    private VideoManagementService videoManagementService;

    /**
     * 查询视频管理列表
     */
//    @PreAuthorize("@ss.hasPermi('system:management:list')")
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
//    @PreAuthorize("@ss.hasPermi('system:management:query')")
    @GetMapping(value = "/{videoId}")
    public AjaxResult getInfo(@PathVariable("videoId") Long videoId)
    {
        return AjaxResult.success(videoManagementService.selectVideoManagementByVideoId(videoId));
    }
}
