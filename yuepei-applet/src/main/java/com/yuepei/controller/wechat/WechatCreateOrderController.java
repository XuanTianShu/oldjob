package com.yuepei.controller.wechat;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.service.CallBackService;
import com.yuepei.service.WechatCreateOrderService;
import com.yuepei.system.domain.UserLeaseOrder;
import com.yuepei.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.*;

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
 * @create ：2022/12/19 10:48
 **/
@Slf4j
@RestController
@RequestMapping("/wechat/user/order")
public class WechatCreateOrderController {

    @Autowired
    private WechatCreateOrderService createOrderService;

    @Autowired
    private CallBackService callBackService;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * 充值
     * @param request
     * @param price
     * @return
     */
    @PostMapping("/payPrepaymentOrder")
    public AjaxResult payPrepaymentOrder(HttpServletRequest request ,Long price){
        SysUser user = tokenUtils.analysis(request);
        return createOrderService.payPrepaymentOrder(user.getUserId(),price);
    }

    /**
     * 充值回调
     * @param request
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws ParseException
     */
    @PostMapping("/payCallBack")
    public HashMap<String, String> payCallBack(HttpServletRequest request) throws IOException, GeneralSecurityException, ParseException {
        return callBackService.payCallBack(request);
    }

    /**
     * 押金
     * @param request
     * @param price
     * @param deviceNumber
     * @return
     */
    @PostMapping("/depositPrepaymentOrder")
    public AjaxResult depositPrepaymentOrder(HttpServletRequest request,Long price,String deviceNumber){
        SysUser user = tokenUtils.analysis(request);
        System.out.println("支付押金");
        return createOrderService.depositPrepaymentOrder(user.getUserId(),price,deviceNumber);
    }

    /**
     * 押金回调
     * @param request
     * @return
     * @throws GeneralSecurityException
     */
    @PostMapping("/depositCallBack")
    public HashMap<String, String> depositCallBack(HttpServletRequest request) throws GeneralSecurityException {
        System.out.println("押金回调");
        return callBackService.depositCallBack(request);
    }


    /**
     * 商品购买及租赁
     * @param request
     * @param userLeaseOrder
     * @param couponId
     * @return
     */
    @PostMapping("/paymentPrepaymentOrder")
    public AjaxResult paymentPrepaymentOrder(HttpServletRequest request,@RequestBody UserLeaseOrder userLeaseOrder, Integer couponId){
        SysUser user = tokenUtils.analysis(request);
        System.out.println(couponId+"----------------------------------"+userLeaseOrder.getPrice());
        return createOrderService.paymentPrepaymentOrder(user.getOpenid(),userLeaseOrder,couponId);
    }

    /**
     * 租赁回调
     * @param request
     * @return
     * @throws GeneralSecurityException
     */
    @PostMapping("/paymentCallBack")
    public HashMap<String, String> paymentCallBack(HttpServletRequest request) throws GeneralSecurityException {
        return callBackService.paymentCallBack(request);
    }


    /**
     * 余额支付
     * @param request
     * @param userLeaseOrder
     * @param couponId
     * @return
     */
    @PostMapping("/balancePrepaymentOrder")
    public AjaxResult balancePrepaymentOrder(HttpServletRequest request,UserLeaseOrder userLeaseOrder,long couponId){
        SysUser user = tokenUtils.analysis(request);
        return callBackService.balancePrepaymentOrder(user.getOpenid(),couponId,userLeaseOrder);
    }


    @PostMapping("/bluetoothCallback")
    public AjaxResult bluetoothCallback(HttpServletRequest request) throws IOException {
//        BufferedReader bufferedReader = request.getReader();
//        String str,wholeStr = "";
//        while ((str = bufferedReader.readLine()) != null) {
//            wholeStr += str;
//        }
//        System.out.println(wholeStr+"-------------------------");


//        Map<String, String> result = new HashMap<>(16);
//        log.info("请求方式GET/POST等:============={}", request.getMethod());
//        log.info("getRequestURL:============={}", request.getRequestURL());
//         获取URL的传参
//        Enumeration<String> params = request.getParameterNames();
//        while (params.hasMoreElements()) {
//            String paraName = params.nextElement();
//            log.info("paraName:============={}", paraName);
//            String[] paraValues = request.getParameterValues(paraName);
//            log.info("paraValues:============={}", paraValues);
//            result.put(paraName, paraValues.length == 1 ? paraValues[0] : paraValues[paraValues.length - 1]);
//        }
        // 获取body的传参
//        Map<String, String> map = new HashMap<>(16);

//        result.putAll(map);
//        log.info("数据上报:============={}", result);
//        SortedMap<String, String> sort = new TreeMap<>(result);
//        log.info("sort:============={}", sort);

        return callBackService.bluetoothCallback(request);
    }


}
