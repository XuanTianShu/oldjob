package com.yuepei.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.exception.ServiceException;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.uuid.UUID;
import com.yuepei.domain.Amount;
import com.yuepei.service.UserRefundService;
import com.yuepei.system.domain.UserDepositOrder;
import com.yuepei.system.domain.vo.UserIntegralBalanceDepositVo;
import com.yuepei.system.mapper.UserDepositDetailMapper;
import com.yuepei.system.mapper.UserDepositOrderMapper;
import com.yuepei.utils.RequestUtils;
import com.yuepei.utils.WXCallBackUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

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
 * @create ：2023/1/14 14:08
 **/
@Slf4j
@Service
public class UserRefundServiceImpl implements UserRefundService {

    @Autowired
    private UserDepositOrderMapper userDepositOrderMapper;

    @Autowired
    public CloseableHttpClient wxPayClient;

    @Autowired
    private WXCallBackUtils wxCallBackUtils;

    @Autowired
    private RequestUtils requestUtils;

    @Value("${wechat.mchKey}")
    private String mchKey;

    @Autowired
    private UserDepositDetailMapper userDepositDetailMapper;

    private HashMap<String, String> hashMap = new HashMap<>();

    @Override
    public AjaxResult userRefund(String openid, String orderNumber) {
        //根据订单获取押金价格
        UserDepositOrder userDepositOrder = userDepositOrderMapper.selectUserDepositOrderByOrderNumber(orderNumber);

        String outTradeNo = UUID.randomUUID().toString().replace("-", "");
        HashMap<Object, Object> payMap = new HashMap<>();
        payMap.put("out_trade_no", orderNumber);
        payMap.put("out_refund_no", outTradeNo);
        payMap.put("reason", "押金退款");
        payMap.put("notify_url","https://www.yp10000.com/dev-api/wechat/user/refund/userRefundCallBack");
        Amount amount = new Amount();
        amount.setTotal(userDepositOrder.getDepositNum());
        amount.setRefund(userDepositOrder.getDepositNum());
        amount.setCurrency("CNY");
        payMap.put("amount", amount);

        //请求地址
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
        // 请求数据
        Gson gson = new Gson();
        String json = gson.toJson(payMap);
        //设置请求信息
        StringEntity stringEntity = new StringEntity(json, "utf-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Accept", "application/json");
        // 3.完成签名并执行请求
        CloseableHttpResponse response = null;
        try {
            response = wxPayClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 4.解析response对象
        HashMap<String, String> resultMap = resolverResponse(response);
        if(resultMap != null) {
            //微信退款单号   --  等待后续操作
            String refund_id = resultMap.get("refund_id");

            return AjaxResult.success();
        }
        return null;
    }

    @Override
    public HashMap<String, String> userRefundCallBack(HttpServletRequest request) throws GeneralSecurityException {
        HashMap params = requestUtils.requestParams(request);
        Map resource = (Map) params.get("resource");

//        Object event_type = params.get("event_type");
//        System.out.println(params.get("event_type")+"=================");

        Object stringBuffer = params.get("stringBuffer");
        String associated_data = (String) resource.get("associated_data");
        String nonce = (String) resource.get("nonce");
        String ciphertext = (String) resource.get("ciphertext");

        JSONObject parseObject;
        boolean isTrue = wxCallBackUtils.parseWXCallBack(request, stringBuffer.toString());
        if (isTrue) {
            AesUtil aesUtil = new AesUtil(mchKey.getBytes());
            parseObject = JSONObject.parseObject(
                    aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext)
            );
            //商户订单号
            String out_trade_no = (String) parseObject.get("out_trade_no");
            Object amount = parseObject.get("amount");
            JSONObject jsonObject1 = JSONObject.parseObject(amount.toString());
            Object price = jsonObject1.get("payer_refund");
            //根据商户订单号查询到用户信息
            UserDepositOrder userDepositOrder = userDepositOrderMapper.selectUserDepositOrderByOrderNumber(out_trade_no);
            //新增用户押金详细
            UserIntegralBalanceDepositVo userIntegralBalanceDepositVo = new UserIntegralBalanceDepositVo();
            userIntegralBalanceDepositVo.setOpenid(userDepositOrder.getOpenid());
            userIntegralBalanceDepositVo.setStatus(1);
            userIntegralBalanceDepositVo.setSum(price.toString());
            userIntegralBalanceDepositVo.setCreateTime(DateUtils.getNowDate());
            userDepositDetailMapper.insertUserDepositDetail(userIntegralBalanceDepositVo);

            //修改用户押金详细
            UserDepositOrder userDepositOrderList = new UserDepositOrder();
            userDepositOrderList.setDepositStatus(1);
            userDepositOrderList.setOrderNumber(out_trade_no);
            userDepositOrderList.setUpdateTime(DateUtils.getNowDate());
            userDepositOrderMapper.updateUserDepositOrder(userDepositOrderList);
            //响应接口
            hashMap.put("code", "SUCCESS");
            hashMap.put("message", "成功");
            return hashMap;
        }else {
            hashMap.put("code", "FAIL");
            hashMap.put("message", "失败");
            return hashMap;
        }
    }


    /**
     * 解析响应数据
     *
     * @param response 发送请求成功后，返回的数据
     * @return 微信返回的参数
     */
    private static HashMap<String, String> resolverResponse(CloseableHttpResponse response) {
        try {
            // 1.获取请求码
            int statusCode = response.getStatusLine().getStatusCode();
            // 2.获取返回值 String 格式
            final String bodyAsString = EntityUtils.toString(response.getEntity());
            Gson gson = new Gson();
            if (statusCode == 200) {
                // 3.如果请求成功则解析成Map对象返回
                HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
                return resultMap;
            } else {
                if (StringUtils.isNoneBlank(bodyAsString)) {
                    log.error("退款请求失败，提示信息:{}", bodyAsString);
                    // 4.请求码显示失败，则尝试获取提示信息
                    HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
                    throw new ServiceException(resultMap.get("message"));
                }
                log.error("退款请求失败，未查询到原因，提示信息:{}", response);
                // 其他异常，微信也没有返回数据，这就需要具体排查了
                throw new IOException("request failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
