package com.yuepei.controller.wechat.iTunes;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.mapper.CarouselMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 轮播图
 */
@RestController
@RequestMapping("/wechat/user/carousel")
public class MyCarouselController {

    @Autowired
    private CarouselMapper carouselMapper;

    @GetMapping("/selectCarouselList")
    public AjaxResult selectCarouselList(){
        return AjaxResult.success(carouselMapper.selectCarouselList(null));
    }

}
