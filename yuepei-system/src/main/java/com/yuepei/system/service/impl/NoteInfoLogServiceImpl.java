package com.yuepei.system.service.impl;

import com.yuepei.system.domain.NoteInfoLog;
import com.yuepei.system.mapper.NoteInfoLogMapper;
import com.yuepei.system.service.NoteInfoLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @create ：2022/11/22 15:24
 **/
@Service
public class NoteInfoLogServiceImpl implements NoteInfoLogService {

    @Autowired
    private NoteInfoLogMapper noteInfoLogMapper;

    /**
     * 查询短信日志
     *
     * @param noteLogId 短信日志主键
     * @return 短信日志
     */
    @Override
    public NoteInfoLog selectNoteInfoLogByNoteLogId(Long noteLogId)
    {
        return noteInfoLogMapper.selectNoteInfoLogByNoteLogId(noteLogId);
    }

    /**
     * 查询短信日志列表
     *
     * @param noteInfoLog 短信日志
     * @return 短信日志
     */
    @Override
    public List<NoteInfoLog> selectNoteInfoLogList(NoteInfoLog noteInfoLog)
    {
        return noteInfoLogMapper.selectNoteInfoLogList(noteInfoLog);
    }

    /**
     * 新增短信日志
     *
     * @param noteInfoLog 短信日志
     * @return 结果
     */
    @Override
    public int insertNoteInfoLog(NoteInfoLog noteInfoLog)
    {
        return noteInfoLogMapper.insertNoteInfoLog(noteInfoLog);
    }

    /**
     * 批量删除短信日志
     *
     * @param noteLogIds 需要删除的短信日志主键
     * @return 结果
     */
    @Override
    public int deleteNoteInfoLogByNoteLogIds(Long[] noteLogIds)
    {
        return noteInfoLogMapper.deleteNoteInfoLogByNoteLogIds(noteLogIds);
    }

    /**
     * 删除短信日志信息
     *
     * @param noteLogId 短信日志主键
     * @return 结果
     */
    @Override
    public int deleteNoteInfoLogByNoteLogId(Long noteLogId)
    {
        return noteInfoLogMapper.deleteNoteInfoLogByNoteLogId(noteLogId);
    }
}
