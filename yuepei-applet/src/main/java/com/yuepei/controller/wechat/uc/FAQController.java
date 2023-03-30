package com.yuepei.controller.wechat.uc;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.service.CommonProblemService;
import com.yuepei.system.service.IHealthProblemService;
import com.yuepei.system.service.IMedicalProblemService;
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
 * @create ：2022/12/22 14:06
 **/
@RestController
@RequestMapping("/wechat/user/faq")
public class FAQController {

    @Autowired
    private CommonProblemService commonProblemService;

    @Autowired
    private IHealthProblemService healthProblemService;

    @Autowired
    private IMedicalProblemService medicalProblemService;

    @GetMapping("/selectCommonProblemList")
    public AjaxResult selectCommonProblemList(){
        return AjaxResult.success(commonProblemService.selectCommonProblemList(null));
    }

    @GetMapping("/selectHealthProblemList")
    public AjaxResult selectHealthProblemList(){
        return AjaxResult.success(healthProblemService.selectHealthProblemList(null));
    }

    @GetMapping("/selectMedicalProblem")
    public AjaxResult selectMedicalProblem(){
        return AjaxResult.success(medicalProblemService.selectMedicalProblemList(null));
    }
}
