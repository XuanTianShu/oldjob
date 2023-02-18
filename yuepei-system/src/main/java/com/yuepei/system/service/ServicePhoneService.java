package com.yuepei.system.service;
import com.yuepei.system.domain.ServicePhone;

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
 * @create ：2022/11/7 15:24
 **/
public interface ServicePhoneService {
    /**
     * 查询客服
     *
     * @param serviceId 客服主键
     * @return 客服
     */
    public ServicePhone selectServicePhoneByServiceId(Long serviceId);

    /**
     * 查询客服列表
     *
     * @param servicePhone 客服
     * @return 客服集合
     */
    public List<ServicePhone> selectServicePhoneList(ServicePhone servicePhone);

    /**
     * 新增客服
     *
     * @param servicePhone 客服
     * @return 结果
     */
    public int insertServicePhone(ServicePhone servicePhone);

    /**
     * 修改客服
     *
     * @param servicePhone 客服
     * @return 结果
     */
    public int updateServicePhone(ServicePhone servicePhone);

    /**
     * 批量删除客服
     *
     * @param serviceIds 需要删除的客服主键集合
     * @return 结果
     */
    public int deleteServicePhoneByServiceIds(Long[] serviceIds);

    /**
     * 删除客服信息
     *
     * @param serviceId 客服主键
     * @return 结果
     */
    public int deleteServicePhoneByServiceId(Long serviceId);
}
