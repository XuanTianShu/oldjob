package com.yuepei.web.controller.system.basicSetup;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.Carousel;
import com.yuepei.system.service.CarouselService;
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
 * @create ：2023/1/11 14:41
 **/
@RestController
@RequestMapping("/system/carousel")
public class CarouselController extends BaseController {

    @Autowired
    private CarouselService carouselService;

    /**
     * 查询轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('system:carousel:list')")
    @GetMapping("/list")
    public TableDataInfo list(Carousel carousel)
    {
        startPage();
        List<Carousel> list = carouselService.selectCarouselList(carousel);
        return getDataTable(list);
    }

    /**
     * 获取轮播图详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:carousel:query')")
    @GetMapping(value = "/{carouselId}")
    public AjaxResult getInfo(@PathVariable("carouselId") Long carouselId)
    {
        return AjaxResult.success(carouselService.selectCarouselByCarouselId(carouselId));
    }

    /**
     * 新增轮播图
     */
    @PreAuthorize("@ss.hasPermi('system:carousel:add')")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Carousel carousel)
    {
        return toAjax(carouselService.insertCarousel(carousel));
    }

    /**
     * 修改轮播图
     */
    @PreAuthorize("@ss.hasPermi('system:carousel:edit')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Carousel carousel)
    {
        return toAjax(carouselService.updateCarousel(carousel));
    }

    /**
     * 删除轮播图
     */
    @PreAuthorize("@ss.hasPermi('system:carousel:remove')")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
    @DeleteMapping("/{carouselIds}")
    public AjaxResult remove(@PathVariable Long[] carouselIds)
    {
        return toAjax(carouselService.deleteCarouselByCarouselIds(carouselIds));
    }
}
