package com.yuepei.system.service.deviceManagement;

import com.yuepei.system.utils.Constant;
import com.yuepei.system.utils.HttpsUtil;
import com.yuepei.system.utils.JsonUtil;
import com.yuepei.system.utils.StreamClosedHttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Query Device Activation Status :
 * This interface is used to query the activation status of the device to check whether the device has connected to the IoT platform.
 * After a device logs in to the IoT platform successfully, the IoT platform sets the device state to active.
 */
public class QueryDeviceActivationStatus {

	public static void main(String args[]) throws Exception {

        // Two-Way Authentication
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        // Authentication.get token
        String accessToken = login(httpsUtil);

        //Please make sure that the following parameter values have been modified in the Constant file.
		String appId = Constant.APPID;

        //please replace the deviceId, when you call this interface.
        String deviceId = "5efe3d25-3ecf-4540-87dc-89300d0147d9";
        String urlDeviceActivationStatus = Constant.QUERY_DEVICE_ACTIVATION_STATUS + "/" + deviceId;

        System.out.println(urlDeviceActivationStatus+"============");
        Map<String, String> header = new HashMap<>();
        header.put(Constant.HEADER_APP_KEY, appId);
        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

        StreamClosedHttpResponse bodyDeviceActivationStatus = httpsUtil.doGetWithParasGetStatusLine(
                urlDeviceActivationStatus, null, header);

        System.out.println("QueryDeviceActivationStatus, response content:");
        System.out.println(bodyDeviceActivationStatus.getStatusLine());
        System.out.println(bodyDeviceActivationStatus.getContent());
        System.out.println();
    }

    /**
     * Authentication.get token
     */
    @SuppressWarnings("unchecked")
    public static String login(HttpsUtil httpsUtil) throws Exception {

        String appId = Constant.APPID;
        String secret = Constant.SECRET;
        String urlLogin = Constant.APP_AUTH;

        Map<String, String> paramLogin = new HashMap<>();
        paramLogin.put("appId", appId);
        paramLogin.put("secret", secret);

        StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, paramLogin);

        System.out.println("app auth success,return accessToken:");
        System.out.println(responseLogin.getStatusLine());
        System.out.println(responseLogin.getContent());
        System.out.println();

        Map<String, String> data = new HashMap<>();
        data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
        return data.get("accessToken");
    }

}
