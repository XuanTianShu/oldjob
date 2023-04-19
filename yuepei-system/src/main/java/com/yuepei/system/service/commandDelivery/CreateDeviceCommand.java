package com.yuepei.system.service.commandDelivery;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yuepei.common.core.domain.AjaxResult;
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

    public static AjaxResult unlocking(Device device) throws Exception {
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
//                String rows = device.getRows();
//                ObjectMapper objectMapper = new ObjectMapper();
//                List<Item> itemList;
//                itemList = objectMapper.readValue(rows, new TypeReference<List<Item>>() {
//                });
//                HashMap<String, Object> map = new HashMap<>();
//                for (int i = Objects.requireNonNull(itemList).size() - 1; i >= 0; i--) {
//                    //0可用1故障2租赁中
//                    if (itemList.get(i).getStatus() == 0){
//                        map.put("value",itemList.get(i).getIndex());
//                        String string = JSONObject.toJSONString(map);
//                        paras_1  = JsonUtil.convertObject2ObjectNode(string);
//                    }
//                }
                paras_1 = JsonUtil.convertObject2ObjectNode("{\"value\":\"0\"}");
            }
        }
//        ObjectNode paras_0 = JsonUtil.convertObject2ObjectNode("{\"value\":\"0\"}");
        Map<String, Object> paramCommand = new HashMap<>();
        if(num.equals("0")){//01 guangdia
            paramCommand.put("serviceId", serviceId_GuangDian);
            paramCommand.put("method", method_CONTROL_STREETLIGHT);
        } else {
            paramCommand.put("serviceId", serviceId_streetLight);
            paramCommand.put("method", method_CLEAR_GETDATA);
        }
//        if(Onoff.equals("1")) {
            paramCommand.put("paras", paras_1);
//        }else {
//            paramCommand.put("paras", paras_0);
//        }

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
        if (responseBody != null){
            /**
             * {"commandId":"0e344774e64f4eefacb7ddf31f29b16c",    设备命令 ID。
             * "appId":"ad1ddd68e3f941b78389ba11584917f9",   第三方应用的身份标识，用于唯一标识一个应用。开发者可通过该标识来指定哪个应用来调用物联网平台的开放 API。
             * "deviceId":"917faaf1-0083-4df9-9186-89f8362277e3",   下发命令的设备 ID，用于唯一标识一个设备
             * "command":{"serviceId":"VehicleDetectorBasic",
             * "method":"SET_DEVICE_LEVEL",
             * "paras":{"value":"3"}},
             * "callbackUrl":"https://www.yp10000.com/prod-api/wechat/user/refund/unlocking",命令状态变化通知地址，当命令状态变化时（执行失败，执行成功，超时，发送，已送达）会通知第三方应用
             * "expireTime":0,  下发命令的超时时间，单位为秒，表示设备命令在创建后 expireTime 秒内有效，超过这个时间范围后命令将不再下发，如果未设置则默认为 48 小时（86400s*2）
             * "status":"SENT",下发命令的状态。
             *  PENDING 表示未下发
             *  EXPIRED 表示命令已经过期
             *  SUCCESSFUL 表示命令已经成功执行
             *  FAILED 表示命令执行失败
             *  TIMEOUT 表示命令下发执行超时
             *  CANCELED 表示命令已经被撤销执行
             *  DELIVERED 表示命令已送达设备
             *  SENT 表示命令正在下发
             * "creationTime":"20230419T030228Z", 命令的创建时间
             * "platformIssuedTime":"20230419T030228Z",  平台发送命令的时间
             * "issuedTimes":0,  平台发送命令的次数
             * "maxRetransmit":3}  命令下发最大重传次数
             */
            JSONObject parse = JSONObject.parse(responseBody);
            String status = String.valueOf(parse.get("status"));
            if (status.equals("SENT")){
                return AjaxResult.success("开锁命令下发中！");
            }else if (status.equals("DELIVERED")){
                return AjaxResult.success("命令已送达！");
            }else if (status.equals("SUCCESSFUL")){
                return AjaxResult.success("命令已执行！");
            }else {
                return AjaxResult.error("开锁命令下发失败！");
            }
        }else {
            return AjaxResult.error("操作失败！");
        }
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
