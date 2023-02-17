package com.yuepei.system.service;

import com.yuepei.system.domain.VideoManagement;

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
 * @create ：2022/11/7 10:23
 **/
public interface VideoManagementService {
    /**
     * 查询视频管理
     *
     * @param videoId 视频管理主键
     * @return 视频管理
     */
    public VideoManagement selectVideoManagementByVideoId(Long videoId);

    /**
     * 查询视频管理列表
     *
     * @param videoManagement 视频管理
     * @return 视频管理集合
     */
    public List<VideoManagement> selectVideoManagementList(VideoManagement videoManagement);

    /**
     * 修改视频管理
     *
     * @param videoManagement 视频管理
     * @return 结果
     */
    public int updateVideoManagement(VideoManagement videoManagement);

    /**
     * 新增视频管理
     *
     * @param videoManagement 视频管理
     * @return 结果
     */
    public int insertVideoManagement(VideoManagement videoManagement);

    /**
     * 批量删除视频管理
     *
     * @param videoIds 需要删除的视频管理主键集合
     * @return 结果
     */
    public int deleteVideoManagementByVideoIds(Long[] videoIds);

    /**
     * 删除视频管理信息
     *
     * @param videoId 视频管理主键
     * @return 结果
     */
    public int deleteVideoManagementByVideoId(Long videoId);
}
