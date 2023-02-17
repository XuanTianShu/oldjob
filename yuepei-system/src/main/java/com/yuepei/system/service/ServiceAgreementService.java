package com.yuepei.system.service;

import com.yuepei.system.domain.ServiceAgreement;

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
 * @create ：2022/11/10 14:36
 **/
public interface ServiceAgreementService {
    /**
     * 查询服务协议
     *
     * @param serviceAgreementId 服务协议主键
     * @return 服务协议
     */
    public ServiceAgreement selectServiceAgreementByServiceAgreementId(Long serviceAgreementId);

    /**
     * 查询服务协议列表
     *
     * @param serviceAgreement 服务协议
     * @return 服务协议集合
     */
    public List<ServiceAgreement> selectServiceAgreementList(ServiceAgreement serviceAgreement);

    /**
     * 新增服务协议
     *
     * @param serviceAgreement 服务协议
     * @return 结果
     */
    public int insertServiceAgreement(ServiceAgreement serviceAgreement);

    /**
     * 修改服务协议
     *
     * @param serviceAgreement 服务协议
     * @return 结果
     */
    public int updateServiceAgreement(ServiceAgreement serviceAgreement);

    /**
     * 批量删除服务协议
     *
     * @param serviceAgreementIds 需要删除的服务协议主键集合
     * @return 结果
     */
    public int deleteServiceAgreementByServiceAgreementIds(Long[] serviceAgreementIds);

    /**
     * 删除服务协议信息
     *
     * @param serviceAgreementId 服务协议主键
     * @return 结果
     */
    public int deleteServiceAgreementByServiceAgreementId(Long serviceAgreementId);

    public List<ServiceAgreement> compactList();
}
