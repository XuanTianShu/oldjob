package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.AboutUs;
import com.yuepei.system.mapper.AboutUsMapper;
import com.yuepei.system.service.AboutUsService;
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
 * @create ：2022/11/10 10:48
 **/
@Service
public class AboutUsServiceImpl implements AboutUsService {

    @Autowired
    private AboutUsMapper aboutUsMapper;

    /**
     * 查询关于我们
     *
     * @param aboutUsId 关于我们主键
     * @return 关于我们
     */
    @Override
    public AboutUs selectAboutUsByAboutUsId(Long aboutUsId)
    {
        return aboutUsMapper.selectAboutUsByAboutUsId(aboutUsId);
    }

    /**
     * 查询关于我们列表
     *
     * @param aboutUs 关于我们
     * @return 关于我们
     */
    @Override
    public List<AboutUs> selectAboutUsList(AboutUs aboutUs)
    {
        return aboutUsMapper.selectAboutUsList(aboutUs);
    }
    public List<AboutUs> aboutUsList()
    {
        return aboutUsMapper.aboutUsList();
    }

    /**
     * 新增关于我们
     *
     * @param aboutUs 关于我们
     * @return 结果
     */
    @Override
    public int insertAboutUs(AboutUs aboutUs)
    {
        aboutUs.setCreateTime(DateUtils.getNowDate());
        return aboutUsMapper.insertAboutUs(aboutUs);
    }

    /**
     * 修改关于我们
     *
     * @param aboutUs 关于我们
     * @return 结果
     */
    @Override
    public int updateAboutUs(AboutUs aboutUs)
    {
        return aboutUsMapper.updateAboutUs(aboutUs);
    }

    /**
     * 批量删除关于我们
     *
     * @param aboutUsIds 需要删除的关于我们主键
     * @return 结果
     */
    @Override
    public int deleteAboutUsByAboutUsIds(Long[] aboutUsIds)
    {
        return aboutUsMapper.deleteAboutUsByAboutUsIds(aboutUsIds);
    }

    /**
     * 删除关于我们信息
     *
     * @param aboutUsId 关于我们主键
     * @return 结果
     */
    @Override
    public int deleteAboutUsByAboutUsId(Long aboutUsId)
    {
        return aboutUsMapper.deleteAboutUsByAboutUsId(aboutUsId);
    }
}
