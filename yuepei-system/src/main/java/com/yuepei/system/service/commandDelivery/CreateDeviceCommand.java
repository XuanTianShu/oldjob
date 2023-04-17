package com.yuepei.system.service.commandDelivery;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yuepei.system.domain.Device;
import com.yuepei.system.domain.Item;
import com.yuepei.system.utils.Constant;
import com.yuepei.system.utils.HttpsUtil;
import com.yuepei.system.utils.JsonUtil;
import com.yuepei.system.utils.StreamClosedHttpResponse;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Create Device Command :
 *
 * This interface is used to send command to device.
 * If a device is not online,
 * the IoT platform buffers the command and delivers the message to the device after the device is online.
 * The NA can set the maximum buffering time.
 */


/**
 * 创建设备下发命令
 */
public class CreateDeviceCommand {

    public static void unlocking(Device device) throws Exception {
        String num="1";
        String Onoff= "1";
        // Two-Way Authentication
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        // Authentication.get token
        String accessToken = login(httpsUtil);

        //Please make sure that the following parameter values have been modified in the Constant file.
        String urlCreateDeviceCommand = Constant.CREATE_DEVICE_CMD;
        String appId = Constant.APPID;
        String callbackUrl = Constant.REPORT_CMD_EXEC_RESULT_CALLBACK_URL;

        //由电信平台提供deviceID
//        String deviceId = "5efe3d25-3ecf-4540-87dc-89300d0147d9";
        //please replace the following parameter values as required, when you call this interface.
        Integer expireTime = 0;
        Integer maxRetransmit = 3;

        //please replace the following parameter values, when you call this interface.
        //And those parameter values must be consistent with the content of profile that have been preset to IoT platform.
        //The following parameter values of this demo are use the watermeter profile that already initialized to IoT platform.
        String serviceId_GuangDian = "GuangDian";
        String method_CLEAR_GETDATA = "SET_DEVICE_LEVEL";
        String serviceId_streetLight = "VehicleDetectorBasic";
        String method_CONTROL_STREETLIGHT = "CONTROL_STREETLIGHT";
        ObjectNode paras_1 = null;
        if (device.getLock() != null){
            paras_1  = JsonUtil.convertObject2ObjectNode("{\"value\":\""+device.getLock()+"\"}");
        }else {
            //TODO 打开所有锁（一拖五专用）
            if (device.getDeviceTypeId() == 5 || device.getDeviceTypeId() == 4){
                String rows = device.getRows();
                ObjectMapper objectMapper = new ObjectMapper();
                List<Item> itemList;
                itemList = objectMapper.readValue(rows, new TypeReference<List<Item>>() {
                });
                for (int i = Objects.requireNonNull(itemList).size() - 1; i >= 0; i--) {
                    //0可用1故障2租赁中
                    if (itemList.get(i).getStatus() == 0){

                    }
                }
            }
        }
        ObjectNode paras_0 = JsonUtil.convertObject2ObjectNode("{\"value\":\"0\"}");

        Map<String, Object> paramCommand = new HashMap<>();
        if(num.equals("0")){//01 guangdia
            paramCommand.put("serviceId", serviceId_GuangDian);
            paramCommand.put("method", method_CONTROL_STREETLIGHT);
        } else {
            paramCommand.put("serviceId", serviceId_streetLight);
            paramCommand.put("method", method_CLEAR_GETDATA);
        }
        if(Onoff.equals("1")) {
            paramCommand.put("paras", paras_1);
        }else {
            paramCommand.put("paras", paras_0);
        }

        Map<String, Object> paramCreateDeviceCommand = new HashMap<>();
        paramCreateDeviceCommand.put("deviceId", device.getTelecomId());
        paramCreateDeviceCommand.put("appId",appId);
        paramCreateDeviceCommand.put("command", paramCommand);
        paramCreateDeviceCommand.put("callbackUrl", callbackUrl);
        paramCreateDeviceCommand.put("expireTime", expireTime);
        paramCreateDeviceCommand.put("maxRetransmit", maxRetransmit);

        String jsonRequest = JsonUtil.jsonObj2Sting(paramCreateDeviceCommand);
        Map<String, String> header = new HashMap<>();
        header.put(Constant.HEADER_APP_KEY, appId);
        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

        HttpResponse responseCreateDeviceCommand = httpsUtil.doPostJson(urlCreateDeviceCommand, header, jsonRequest);

        String responseBody = httpsUtil.getHttpResponseBody(responseCreateDeviceCommand);

        System.out.println("CreateDeviceCommand, response content:");
        System.out.println(responseCreateDeviceCommand.getStatusLine());
        System.out.println(responseBody);
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
