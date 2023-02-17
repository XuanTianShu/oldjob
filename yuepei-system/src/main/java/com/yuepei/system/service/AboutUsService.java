package com.yuepei.system.service;

import com.yuepei.system.domain.AboutUs;

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
 * @create ：2022/11/10 10:48
 **/
public interface AboutUsService {
    /**
     * 查询关于我们
     *
     * @param aboutUsId 关于我们主键
     * @return 关于我们
     */
    public AboutUs selectAboutUsByAboutUsId(Long aboutUsId);

    /**
     * 查询关于我们列表
     *
     * @param aboutUs 关于我们
     * @return 关于我们集合
     */
    public List<AboutUs> selectAboutUsList(AboutUs aboutUs);

    public List<AboutUs> aboutUsList();

    /**
     * 新增关于我们
     *
     * @param aboutUs 关于我们
     * @return 结果
     */
    public int insertAboutUs(AboutUs aboutUs);

    /**
     * 修改关于我们
     *
     * @param aboutUs 关于我们
     * @return 结果
     */
    public int updateAboutUs(AboutUs aboutUs);

    /**
     * 批量删除关于我们
     *
     * @param aboutUsIds 需要删除的关于我们主键集合
     * @return 结果
     */
    public int deleteAboutUsByAboutUsIds(Long[] aboutUsIds);

    /**
     * 删除关于我们信息
     *
     * @param aboutUsId 关于我们主键
     * @return 结果
     */
    public int deleteAboutUsByAboutUsId(Long aboutUsId);
}
