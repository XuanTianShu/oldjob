package com.yuepei.web.controller.monitor;

import com.yuepei.common.annotation.Log;
import com.yuepei.common.annotation.NoteLog;
import com.yuepei.common.core.controller.BaseController;
import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.page.TableDataInfo;
import com.yuepei.common.enums.BusinessType;
import com.yuepei.common.utils.sms.AliSMS;
import com.yuepei.system.domain.NoteInfoLog;
import com.yuepei.system.service.NoteInfoLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
 * @create ：2022/11/22 15:25
 **/
@RestController
@RequestMapping("/monitor/noteLog")
public class NoteInfoLogController extends BaseController {
    @Autowired
    private NoteInfoLogService noteInfoLogService;

    @Autowired
    private AliSMS aliSMS;

    /**
     * 查询短信日志列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:noteLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(NoteInfoLog noteInfoLog)
    {
        startPage();
        List<NoteInfoLog> list = noteInfoLogService.selectNoteInfoLogList(noteInfoLog);
        return getDataTable(list);
    }

    /**
     * 获取短信日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('monitor:noteLog:query')")
    @GetMapping(value = "/{noteLogId}")
    public AjaxResult getInfo(@PathVariable("noteLogId") Long noteLogId)
    {
        return AjaxResult.success(noteInfoLogService.selectNoteInfoLogByNoteLogId(noteLogId));
    }

    /**
     * 删除短信日志
     */
    @PreAuthorize("@ss.hasPermi('monitor:noteLog:remove')")
    @Log(title = "短信日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noteLogIds}")
    public AjaxResult remove(@PathVariable Long[] noteLogIds)
    {
        return toAjax(noteInfoLogService.deleteNoteInfoLogByNoteLogIds(noteLogIds));
    }

    /**
     * 删除短信日志
     */
    @NoteLog(title = "新用户注册")
    @GetMapping("/send")
    public AjaxResult send(String phoneNumber)
    {
        return AjaxResult.success(aliSMS.sendSmsCode(phoneNumber,"326589"));
    }
}
