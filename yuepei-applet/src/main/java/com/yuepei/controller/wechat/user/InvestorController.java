package com.yuepei.controller.wechat.user;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.service.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/us/investor")
public class InvestorController {

//    @Autowired
//    private InvestorService investorService;


    @PostMapping("/home")
    public AjaxResult home(){
        AjaxResult success = AjaxResult.success();

        return success;
    }
}
