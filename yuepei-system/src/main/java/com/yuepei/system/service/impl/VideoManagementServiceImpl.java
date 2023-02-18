package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.VideoManagement;
import com.yuepei.system.mapper.VideoManagementMapper;
import com.yuepei.system.service.VideoManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @create ：2022/11/7 10:24
 **/
@Service
public class VideoManagementServiceImpl  implements VideoManagementService {

    @Autowired
    private VideoManagementMapper videoManagementMapper;

    /**
     * 查询视频管理
     *
     * @param videoId 视频管理主键
     * @return 视频管理
     */
    @Override
    public VideoManagement selectVideoManagementByVideoId(Long videoId)
    {
        return videoManagementMapper.selectVideoManagementByVideoId(videoId);
    }

    /**
     * 查询视频管理列表
     *
     * @param videoManagement 视频管理
     * @return 视频管理
     */
    @Override
    public List<VideoManagement> selectVideoManagementList(VideoManagement videoManagement)
    {
        return videoManagementMapper.selectVideoManagementList(videoManagement);
    }

    /**
     * 修改视频管理
     *
     * @param videoManagement 视频管理
     * @return 结果
     */
    @Override
    public int updateVideoManagement(VideoManagement videoManagement)
    {
        return videoManagementMapper.updateVideoManagement(videoManagement);
    }

    /**
     * 新增视频管理
     *
     * @param videoManagement 视频管理
     * @return 结果
     */
    @Override
    public int insertVideoManagement(VideoManagement videoManagement)
    {
        videoManagement.setCreateTime(DateUtils.getNowDate());
        return videoManagementMapper.insertVideoManagement(videoManagement);
    }

    /**
     * 批量删除视频管理
     *
     * @param videoIds 需要删除的视频管理主键
     * @return 结果
     */
    @Override
    public int deleteVideoManagementByVideoIds(Long[] videoIds)
    {
        return videoManagementMapper.deleteVideoManagementByVideoIds(videoIds);
    }

    /**
     * 删除视频管理信息
     *
     * @param videoId 视频管理主键
     * @return 结果
     */
    @Override
    public int deleteVideoManagementByVideoId(Long videoId)
    {
        return videoManagementMapper.deleteVideoManagementByVideoId(videoId);
    }
}
