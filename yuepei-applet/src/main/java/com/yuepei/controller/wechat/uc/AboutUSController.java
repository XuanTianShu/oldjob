package com.yuepei.controller.wechat.uc;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.service.AboutUsService;
import com.yuepei.system.service.ServiceAgreementService;
import com.yuepei.system.service.ServicePhoneService;
import com.yuepei.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 * @create ：2022/12/21 9:49
 **/
@RestController
@RequestMapping("/wechat/user/us")
public class AboutUSController {

    @Autowired
    private AboutUsService aboutUsService;

    @Autowired
    private ServiceAgreementService serviceAgreementService;

    @Autowired
    private ServicePhoneService servicePhoneService;

    @GetMapping("/aboutUsList")
    public AjaxResult aboutUsList(){
       return AjaxResult.success(aboutUsService.aboutUsList());
    }

    @GetMapping("/compactList")
    public AjaxResult compactList(){
        return AjaxResult.success(serviceAgreementService.compactList());
    }


    @GetMapping("/contactUsList")
    public AjaxResult contactUsList(){
        return AjaxResult.success(servicePhoneService.selectServicePhoneList(null));
    }
}
