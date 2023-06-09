package com.yuepei.config;
import com.yuepei.utils.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


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
 * @create ：2022/12/12 15:55
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor())
                .addPathPatterns("/wechat/**")
                .excludePathPatterns("/wechat/user/login",
                        "/wechat/user/loginHospitalPort",
                        "/wechat/user/order/payCallBack",
                        "/wechat/user/order/depositCallBack",
                        "/wechat/user/order/paymentCallBack",
                        "/wechat/user/refund/userRefundCallBack",
                        "/wechat/user/refund/unlocking",
                        "/wechat/user/order/bluetoothCallback",
                        "/wechat/user/order/PH70Callback",
                        "/wechat/user/order/XG70NBTCallback",
                        "/wechat/user/us/compactList",
                        "/wechat/user/investor/**",
                        "/wechat/user/refund/userRefund",
                        "/wechat/user/refund/userRefundCallBack",
                        "/wechat/user/refund/PH70InstructCallback",
                        "/wechat/user/refund/PH70IncidentCallback",
                        "/wechat/user/order/weChatWithdrawal",
                        "/wechat/user/maintenance/**",
                        "/wechat/user/refund/orderRefundCallBack",
                        "/wechat/user/refund/depositRefundCallback",
                        "/wechat/user/refund/depositRefund",
                        "/wechat/user/discount/list");
    }

    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }
}
