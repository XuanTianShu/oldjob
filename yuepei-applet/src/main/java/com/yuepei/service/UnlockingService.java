package com.yuepei.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.system.domain.Device;

public interface UnlockingService {
    AjaxResult unlocking(Device device);
}
