package com.yuepei.utils;

import com.alibaba.fastjson2.JSON;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.PrivateKey;
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
 * @create ：2023/1/6 14:56
 **/
@Component
public class WXCallBackUtils {


    // 商户私钥文件
    @Value("${wechat.privateKeyPath}")
    private String privateKeyPath;

    @Value("${wechat.mchId}")
    private String mchId;

    @Value("${wechat.mchKey}")
    private String mchKey;

    @Value("${wechat.mchSerialNo}")
    private String mchSerialNo;

    private boolean verify;

    public boolean parseWXCallBack(HttpServletRequest request,String stringBuffer){

        request.getCharacterEncoding();

        //从请求头获取验签字段
        //时间戳
        String Timestamp = request.getHeader("Wechatpay-Timestamp");
        //随机字符串
        String Nonce = request.getHeader("Wechatpay-Nonce");
        //签名
        String Signature = request.getHeader("Wechatpay-Signature");
        //串行接口
        String Serial = request.getHeader("Wechatpay-Serial");

        try {
            PrivateKey privateKey = PemUtil.loadPrivateKey(new FileInputStream(privateKeyPath));
                AutoUpdateCertificatesVerifier verifier  =
                        new AutoUpdateCertificatesVerifier(
                                new WechatPay2Credentials(
                                        mchId, new PrivateKeySigner(mchSerialNo, privateKey)), mchKey.getBytes("UTF-8"));

            String VerifySignature = Timestamp + "\n" + Nonce + "\n" + stringBuffer + "\n";

            verify = verifier.verify(Serial, VerifySignature.getBytes(), Signature);

        } catch (Exception e ) {
            e.printStackTrace();
        }
        return verify;
    }
}
