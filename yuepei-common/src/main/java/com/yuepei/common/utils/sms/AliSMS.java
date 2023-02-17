package com.yuepei.common.utils.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.yuepei.common.core.redis.RedisCache;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
 * @create ：2022/11/22 17:15
 **/
@Component
public class AliSMS {

    @Autowired
    private RedisCache redisCache;

    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    static final String product = "Dysmsapi";
    /**
     * 产品域名,开发者无需替换
     */
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    // TODO 使用阿里云短信需要打开config文件夹中AliSmsConfig中的注释,把Bean注入容器中
    @Value("${ali-sms.accessKeyId}")
    private String accessKeyId;
    @Value("${ali-sms.accessKeySecret}")
    private String accessKeySecret;
    @Value("${ali-sms.signName}")
    private String signName;
    @Value("${ali-sms.templateCode}")
    private  String templateCode;
    @Value("${ali-sms.timeout}")
    private int timeout;


    /**
     * 发送验证码
     * @param PhoneNumbers 手机号码
     * @param phoneCode 验证码
     * @return
     **/
    public  boolean sendSmsCode(String PhoneNumbers, String phoneCode) {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        IAcsClient acsClient = new DefaultAcsClient(profile);

        //短信模板参数
        String templateParam = "{'code':" + phoneCode + "}";

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(PhoneNumbers);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"你正在修改绑定的手机号，验证码为${code}"时,此处的值为
        request.setTemplateParam(templateParam);

        try {
            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            String requestId = sendSmsResponse.getRequestId();
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //随机获取指定长度的验证码
    public String getRandomNum(int max) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < max; ++j) {
            if (j ==0) {
                int b = random.nextInt(9)+1;
                sb.append(b);
            }else{
                int number = random.nextInt(10);
                sb.append(number);
            }
        }
        return sb.toString();
    }


    /**
     * 验证码判断
     *
     * @param phone 手机号码
     * @param code  验证码
     * @return
     */
    public Boolean judge(String phone, String code) {

        //从redis获取验证码
        Object codeId = redisCache.getCacheObject(phone);
        //从redis获取验证码的Key值（手机号码）
        Collection<String> phones = redisCache.keys(phone);
        for (String s : phones) {
            //判断手机号码和验证码是否匹配
            if (s.equals(phone) && codeId.equals(String.valueOf(code))) {
                //删除redis中的Key
                redisCache.deleteObject(phone);
                //返回成功提示
                return true;
            }
        }
        return false;
    }
}
