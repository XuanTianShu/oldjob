package com.yuepei.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.yuepei.common.exception.ServiceException;
import lombok.Data;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

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
 * @create ：2022/12/23 17:21
 **/
@Configuration
public class WXPayConfig {
    // 商户号
    @Value("${wechat.mchId}")
    private String mchId;

    // 商户API证书序列号
    @Value("${wechat.mchSerialNo}")
    private String mchSerialNo;

    // 商户私钥文件
    @Value("${wechat.privateKeyPath}")
    private String privateKeyPath;

    // APIv3密钥
    @Value("${wechat.mchKey}")
    private String key;

    // APPID
    @Value("${wechat.appid}")
    private String appid;

    // 微信服务器地址
    private String domain;

    // 接收结果通知地址
    private String notifyDomain;


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

    /**
     * 获取签名验证器
     */
    @Bean
    public Verifier getVerifier() {
        // 获取商户私钥
        final PrivateKey privateKey = getPrivateKey(privateKeyPath);

        // 私钥签名对象
        PrivateKeySigner privateKeySigner = new PrivateKeySigner(mchSerialNo, privateKey);

        // 身份认证对象
        WechatPay2Credentials wechatPay2Credentials = new WechatPay2Credentials(mchId, privateKeySigner);

        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();

        try {
            // 向证书管理器增加需要自动更新平台证书的商户信息
            certificatesManager.putMerchant(mchId, wechatPay2Credentials, key.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | GeneralSecurityException | HttpCodeException e) {
            e.printStackTrace();
        }

        try {
            return certificatesManager.getVerifier(mchId);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new ServiceException("获取签名验证器失败");
        }
    }


    /**
     * 获取微信支付的远程请求对象
     *
     * @return Http请求对象
     */
    @Bean
    public CloseableHttpClient getWxPayClient() {

        // 获取签名验证器
        Verifier verifier = getVerifier();

        // 获取商户私钥
        final PrivateKey privateKey = getPrivateKey(privateKeyPath);

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create().withMerchant(mchId, mchSerialNo, privateKey)
                .withValidator(new WechatPay2Validator(verifier));

        return builder.build();
    }
}
