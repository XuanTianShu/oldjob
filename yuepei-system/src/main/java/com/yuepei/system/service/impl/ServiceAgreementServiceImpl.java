package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.ServiceAgreement;
import com.yuepei.system.mapper.ServiceAgreementMapper;
import com.yuepei.system.service.ServiceAgreementService;
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
 * @create ：2022/11/10 14:37
 **/
@Service
public class ServiceAgreementServiceImpl implements ServiceAgreementService {
    @Autowired
    private ServiceAgreementMapper serviceAgreementMapper;

    /**
     * 查询服务协议
     *
     * @param serviceAgreementId 服务协议主键
     * @return 服务协议
     */
    @Override
    public ServiceAgreement selectServiceAgreementByServiceAgreementId(Long serviceAgreementId)
    {
        return serviceAgreementMapper.selectServiceAgreementByServiceAgreementId(serviceAgreementId);
    }

    /**
     * 查询服务协议列表
     *
     * @param serviceAgreement 服务协议
     * @return 服务协议
     */
    @Override
    public List<ServiceAgreement> selectServiceAgreementList(ServiceAgreement serviceAgreement)
    {
        return serviceAgreementMapper.selectServiceAgreementList(serviceAgreement);
    }

    @Override
    public List<ServiceAgreement> compactList()
    {
        return serviceAgreementMapper.compactList();
    }

    /**
     * 新增服务协议
     *
     * @param serviceAgreement 服务协议
     * @return 结果
     */
    @Override
    public int insertServiceAgreement(ServiceAgreement serviceAgreement)
    {
        serviceAgreement.setCreateTime(DateUtils.getNowDate());
        return serviceAgreementMapper.insertServiceAgreement(serviceAgreement);
    }

    /**
     * 修改服务协议
     *
     * @param serviceAgreement 服务协议
     * @return 结果
     */
    @Override
    public int updateServiceAgreement(ServiceAgreement serviceAgreement)
    {
        return serviceAgreementMapper.updateServiceAgreement(serviceAgreement);
    }

    /**
     * 批量删除服务协议
     *
     * @param serviceAgreementIds 需要删除的服务协议主键
     * @return 结果
     */
    @Override
    public int deleteServiceAgreementByServiceAgreementIds(Long[] serviceAgreementIds)
    {
        return serviceAgreementMapper.deleteServiceAgreementByServiceAgreementIds(serviceAgreementIds);
    }

    /**
     * 删除服务协议信息
     *
     * @param serviceAgreementId 服务协议主键
     * @return 结果
     */
    @Override
    public int deleteServiceAgreementByServiceAgreementId(Long serviceAgreementId)
    {
        return serviceAgreementMapper.deleteServiceAgreementByServiceAgreementId(serviceAgreementId);
    }


}
