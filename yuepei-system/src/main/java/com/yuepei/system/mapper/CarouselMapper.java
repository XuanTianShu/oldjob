package com.yuepei.system.mapper;

import com.yuepei.system.domain.Carousel;

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
 * @create ：2023/1/11 14:38
 **/
public interface CarouselMapper {
    /**
     * 查询轮播图
     *
     * @param carouselId 轮播图主键
     * @return 轮播图
     */
    public Carousel selectCarouselByCarouselId(Long carouselId);

    /**
     * 查询轮播图列表
     *
     * @param carousel 轮播图
     * @return 轮播图集合
     */
    public List<Carousel> selectCarouselList(Carousel carousel);

    /**
     * 新增轮播图
     *
     * @param carousel 轮播图
     * @return 结果
     */
    public int insertCarousel(Carousel carousel);

    /**
     * 修改轮播图
     *
     * @param carousel 轮播图
     * @return 结果
     */
    public int updateCarousel(Carousel carousel);

    /**
     * 删除轮播图
     *
     * @param carouselId 轮播图主键
     * @return 结果
     */
    public int deleteCarouselByCarouselId(Long carouselId);

    /**
     * 批量删除轮播图
     *
     * @param carouselIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCarouselByCarouselIds(Long[] carouselIds);
}
