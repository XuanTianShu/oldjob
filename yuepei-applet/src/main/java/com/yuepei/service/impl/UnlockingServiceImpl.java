package com.yuepei.service.impl;

import com.google.gson.Gson;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.mapper.UnlockingMapper;
import com.yuepei.service.UnlockingService;
import com.yuepei.system.domain.Device;
import com.yuepei.system.service.commandDelivery.CreateDeviceCommand;
import jdk.nashorn.internal.ir.ObjectNode;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class UnlockingServiceImpl implements UnlockingService {

    @Value("${bluetooth.appId}")
    private String appId;

    @Value("{bluetooth.secrect}")
    private String secrect;

//    @Autowired
//    private UnlockingMapper unlockingMapper;

    @Autowired
    private CloseableHttpClient client;

    @Transactional
    @Override
    public AjaxResult unlocking(Device device) {
        log.info("设备信息：{}",device.getTelecomId());
        log.info("锁号：{}",device.getLock());
        try {
            return CreateDeviceCommand.unlocking(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult AllUnlocking(Device device) {
        log.info("设备信息：{}",device.getTelecomId());
        try {
            return CreateDeviceCommand.unlocking(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success();
    }
}
