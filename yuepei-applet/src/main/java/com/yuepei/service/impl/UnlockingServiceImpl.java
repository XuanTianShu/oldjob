package com.yuepei.service.impl;

import com.google.gson.Gson;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.mapper.UnlockingMapper;
import com.yuepei.service.UnlockingService;
import jdk.nashorn.internal.ir.ObjectNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UnlockingServiceImpl implements UnlockingService {

    @Value("{bluetooth.appId}")
    private String appId;

    @Autowired
    private UnlockingMapper unlockingMapper;

    @Autowired
    private CloseableHttpClient client;

    @Transactional
    @Override
    public AjaxResult unlocking() {
        String notifyUrl = "https://www.yp10000.com/prod-api/wechat/user/order/bluetoothCallback";
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("deviceId","78:36:16:EE:A4:08");
        map1.put("callbackUrl",notifyUrl);
        map.put("body",map1);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("serviceId","VehicleDetectorBasic");
        map2.put("method","SET_DEVICE_LEVEL");
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("value",2);
        map2.put("paras",map3);
        //请求地址
        HttpPost httpPost = new HttpPost("https://device.api.ct10649.com:8743/iocm/app/cmd/v1.4.0/deviceCommands");
        // 请求数据
        Gson gson = new Gson();
        String json = gson.toJson(map);
        //设置请求信息
        StringEntity stringEntity = new StringEntity(json, "utf-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        HashMap<String, String> map4 = new HashMap<>();
        map4.put("Accept", "application/json");
        map4.put("Authorization",null);
        map4.put("app_key","ad1ddd68e3f941b78389ba11584917f9");
        if (!map4.isEmpty()){
            for (Map.Entry<String,String> vo : map4.entrySet()){
                httpPost.setHeader(vo.getKey(),vo.getValue());
            }
        }
        // 3.完成签名并执行请求
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
