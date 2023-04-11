package com.yuepei.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.UserLeaseOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
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
 * @create ：2023/1/5 16:31
 **/
public interface CallBackService {
    HashMap<String, String> payCallBack(HttpServletRequest request) throws IOException, GeneralSecurityException, ParseException;

    HashMap<String, String> depositCallBack(HttpServletRequest request) throws GeneralSecurityException;

    HashMap<String, String> paymentCallBack(HttpServletRequest request) throws GeneralSecurityException;

    AjaxResult balancePrepaymentOrder(String openid, Long couponId, UserLeaseOrder userLeaseOrder);

    AjaxResult bluetoothCallback(HttpServletRequest request);

    AjaxResult PH70Callback(HttpServletRequest request);

    AjaxResult XG70NBTCallback(HttpServletRequest request);

    AjaxResult PH70InstructCallback(HttpServletRequest request);

    AjaxResult PH70IncidentCallback(HttpServletRequest request);
}
