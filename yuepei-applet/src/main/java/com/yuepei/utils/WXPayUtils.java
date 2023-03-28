package com.yuepei.utils;

import com.google.gson.Gson;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.yuepei.common.exception.ServiceException;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.uuid.UUID;
import com.yuepei.domain.Amount;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

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
 * @create ：2023/1/5 13:50
 **/
@Slf4j
@Component
public class WXPayUtils {

    // 商户私钥文件
    @Value("${wechat.privateKeyPath}")
    private String privateKeyPath;

    @Value("${wechat.mchId}")
    private String mchKey;

    @Value("${wechat.appId}")
    private String appId;

    @Autowired
    public CloseableHttpClient wxPayClient;

    public HashMap<String, String> sendPay(String openid, BigDecimal price,String outTradeNo,String notifyUrl){

        HashMap<Object, Object> payMap = new HashMap<>();
        payMap.put("appid", appId);
        payMap.put("mchid", mchKey);
        payMap.put("description", "测试");
        payMap.put("out_trade_no", outTradeNo);
        payMap.put("notify_url",notifyUrl);
        Amount amount = new Amount();
//        new BigDecimal(price).multiply(BigDecimal.valueOf(100)).longValue()
//        amount.setTotal(new BigDecimal(price).multiply(BigDecimal.valueOf(100)).longValue());
//        BigDecimal multiply = new BigDecimal(price).multiply(new BigDecimal(100));
        amount.setTotal(price.multiply(new BigDecimal(100)).longValue());
        amount.setCurrency("CNY");
        payMap.put("amount", amount);
        HashMap<Object, Object> payerMap = new HashMap<>();
        payerMap.put("openid",openid);
        payMap.put("payer",payerMap);

        //请求地址
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
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
        if(resultMap != null){
            if(resultMap.get("prepay_id") != null){
                // native请求返回的是二维码链接，前端将链接转换成二维码即可
                ArrayList<String> list = new ArrayList<>();
                String timeStamp = DateUtils.getNowDate().getTime() + "";
                String nonceStr = UUID.randomUUID().toString().replace("-", "");
                String packageStr = "prepay_id=" + resultMap.get("prepay_id");
                list.add(appId);
                list.add(timeStamp);
                list.add(nonceStr);
                list.add(packageStr);
                PrivateKey privateKey = getPrivateKey(privateKeyPath);
                String sign = sign(buildSignMessage(list).getBytes(), privateKey);
                resultMap.put("timeStamp", timeStamp);
                resultMap.put("nonceStr", nonceStr);
                resultMap.put("packageStr", packageStr);
                resultMap.put("paySign", sign);
                return resultMap;
            }
        }
        return resultMap;
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
                    log.error("微信支付请求失败，提示信息:{}", bodyAsString);
                    // 4.请求码显示失败，则尝试获取提示信息
                    HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
                    throw new ServiceException(resultMap.get("message"));
                }
                log.error("微信支付请求失败，未查询到原因，提示信息:{}", response);
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

    /**
     * 构造签名串
     *
     * @param signMessage 待签名的参数
     * @return 构造后带待签名串
     */
    static String buildSignMessage(ArrayList<String> signMessage) {
        if (signMessage == null || signMessage.size() <= 0) {
            return null;
        }
        StringBuilder sbf = new StringBuilder();
        for (String str : signMessage) {
            sbf.append(str).append("\n");
        }
        return sbf.toString();
    }



    static String sign(byte[] message,PrivateKey key) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(key);
            sign.update(message);
            return Base64.getEncoder().encodeToString(sign.sign());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持SHA256withRSA", e);
        } catch (SignatureException e) {
            throw new RuntimeException("签名计算失败", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("无效的私钥", e);
        }
    }

    /**
     * 获取商户的私钥文件
     *
     * @param filename 证书地址
     * @return 私钥文件
     */
    public PrivateKey getPrivateKey(String filename) {
        try {
            return PemUtil.loadPrivateKey(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            throw new ServiceException("私钥文件不存在");
        }
    }
}
