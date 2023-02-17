package com.yuepei.web.controller.goods;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.Goods;
import com.yuepei.system.service.GoodsService;
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
 * @create ：2022/11/21 16:00
 **/
@RestController
@RequestMapping("/goods/goodsList")
public class GoodsController extends BaseController
{
    @Autowired
    private GoodsService goodsService;

    /**
     * 查询商品列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodList:list')")
    @GetMapping("/list")
    public TableDataInfo list(Goods goods)
    {
        startPage();
        List<Goods> list = goodsService.selectGoodsList(goods);
        return getDataTable(list);
    }

    /**
     * 获取商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:goodList:query')")
    @GetMapping(value = "/{goodsId}")
    public AjaxResult getInfo(@PathVariable("goodsId") Long goodsId)
    {
        return AjaxResult.success(goodsService.selectGoodsByGoodsId(goodsId));
    }

    /**
     * 新增商品
     */
    @PreAuthorize("@ss.hasPermi('goods:goodList:add')")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Goods goods)
    {
        return toAjax(goodsService.insertGoods(goods));
    }

    /**
     * 修改商品
     */
    @PreAuthorize("@ss.hasPermi('goods:goodList:edit')")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Goods goods)
    {
        return toAjax(goodsService.updateGoods(goods));
    }

    /**
     * 删除商品
     */
    @PreAuthorize("@ss.hasPermi('goods:goodList:remove')")
    @Log(title = "商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{goodsIds}")
    public AjaxResult remove(@PathVariable Long[] goodsIds)
    {
        return toAjax(goodsService.deleteGoodsByGoodsIds(goodsIds));
    }
}
