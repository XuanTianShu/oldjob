package com.yuepei.web.controller.goods;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.system.domain.GoodsType;
import com.yuepei.system.service.GoodsTypeService;
import com.yuepei.utils.DictionaryEnum;
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
 * @create ：2022/11/19 15:00
 **/
@RestController
@RequestMapping("/goods/type")
public class GoodsTypeController extends BaseController
{
    @Autowired
    private GoodsTypeService goodsTypeService;

    /**
     * 查询商品类型列表
     */
    @PreAuthorize("@ss.hasPermi('goods:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(GoodsType goodsType)
    {
        startPage();
        List<GoodsType> list = goodsTypeService.selectGoodsTypeList(goodsType);
        return getDataTable(list);
    }

    /**
     * 获取商品类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:type:query')")
    @GetMapping(value = "/{goodsTypeId}")
    public AjaxResult getInfo(@PathVariable("goodsTypeId") Long goodsTypeId)
    {
        return AjaxResult.success(goodsTypeService.selectGoodsTypeByGoodsTypeId(goodsTypeId));
    }

    /**
     * 新增商品类型
     */
    @PreAuthorize("@ss.hasPermi('goods:type:add')")
    @Log(title = "商品类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoodsType goodsType)
    {
        boolean b = goodsTypeService.checkGoodsType(goodsType);
        if (b){
            return AjaxResult.error(DictionaryEnum.CHECK_GOODS_TYPE.getName());
        }
        return toAjax(goodsTypeService.insertGoodsType(goodsType));
    }

    /**
     * 修改商品类型
     */
    @PreAuthorize("@ss.hasPermi('goods:type:edit')")
    @Log(title = "商品类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoodsType goodsType)
    {
        boolean b = goodsTypeService.checkGoodsType(goodsType);
        if (b){
            return AjaxResult.error(DictionaryEnum.CHECK_GOODS_TYPE.getName());
        }
        return toAjax(goodsTypeService.updateGoodsType(goodsType));
    }

    /**
     * 删除商品类型
     */
    @PreAuthorize("@ss.hasPermi('goods:type:remove')")
    @Log(title = "商品类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{goodsTypeIds}")
    public AjaxResult remove(@PathVariable Long[] goodsTypeIds)
    {
        boolean b = goodsTypeService.checkGoodsTypeByIds(goodsTypeIds);
        if (b){
            return AjaxResult.error(DictionaryEnum.CHECK_GOODS_TYPE_ID.getName());
        }
        return toAjax(goodsTypeService.deleteGoodsTypeByGoodsTypeIds(goodsTypeIds));
    }
}
