package com.yuepei.system.service.impl;

import com.yuepei.system.domain.PayType;
import com.yuepei.system.mapper.PayTypeMapper;
import com.yuepei.system.service.PayTypeService;
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
 * @create ：2022/12/15 11:20
 **/
@Service
public class PayTypeServiceImpl implements PayTypeService {

    @Autowired
    private PayTypeMapper payTypeMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public PayType selectPayTypeById(Long id)
    {
        return payTypeMapper.selectPayTypeById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param payType 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<PayType> selectPayTypeList(PayType payType)
    {
        return payTypeMapper.selectPayTypeList(payType);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param payType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertPayType(PayType payType)
    {
        return payTypeMapper.insertPayType(payType);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param payType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updatePayType(PayType payType)
    {
        return payTypeMapper.updatePayType(payType);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePayTypeByIds(Long[] ids)
    {
        return payTypeMapper.deletePayTypeByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePayTypeById(Long id)
    {
        return payTypeMapper.deletePayTypeById(id);
    }

    @Override
    public List<PayType> selectPayTypeAll() {
        return payTypeMapper.selectPayTypeAll();
    }
}
