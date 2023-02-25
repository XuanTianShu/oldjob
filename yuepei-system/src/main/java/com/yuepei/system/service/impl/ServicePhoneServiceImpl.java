package com.yuepei.system.service.impl;

import com.yuepei.system.domain.ServicePhone;
import com.yuepei.system.mapper.ServicePhoneMapper;
import com.yuepei.system.service.ServicePhoneService;
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
 * @create ：2022/11/7 15:48
 **/
@Service
public class ServicePhoneServiceImpl implements ServicePhoneService {

    @Autowired
    private ServicePhoneMapper servicePhoneMapper;

    /**
     * 查询客服
     *
     * @param serviceId 客服主键
     * @return 客服
     */
    @Override
    public ServicePhone selectServicePhoneByServiceId(Long serviceId)
    {
        return servicePhoneMapper.selectServicePhoneByServiceId(serviceId);
    }

    /**
     * 查询客服列表
     *
     * @param servicePhone 客服
     * @return 客服
     */
    @Override
    public List<ServicePhone> selectServicePhoneList(ServicePhone servicePhone)
    {
        return servicePhoneMapper.selectServicePhoneList(servicePhone);
    }

    /**
     * 新增客服
     *
     * @param servicePhone 客服
     * @return 结果
     */
    @Override
    public int insertServicePhone(ServicePhone servicePhone)
    {
        return servicePhoneMapper.insertServicePhone(servicePhone);
    }

    /**
     * 修改客服
     *
     * @param servicePhone 客服
     * @return 结果
     */
    @Override
    public int updateServicePhone(ServicePhone servicePhone)
    {
        return servicePhoneMapper.updateServicePhone(servicePhone);
    }

    /**
     * 批量删除客服
     *
     * @param serviceIds 需要删除的客服主键
     * @return 结果
     */
    @Override
    public int deleteServicePhoneByServiceIds(Long[] serviceIds)
    {
        return servicePhoneMapper.deleteServicePhoneByServiceIds(serviceIds);
    }

    /**
     * 删除客服信息
     *
     * @param serviceId 客服主键
     * @return 结果
     */
    @Override
    public int deleteServicePhoneByServiceId(Long serviceId)
    {
        return servicePhoneMapper.deleteServicePhoneByServiceId(serviceId);
    }
}
