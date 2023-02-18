package com.yuepei.system.mapper;

import com.yuepei.system.domain.NoteInfoLog;

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
 * @create ：2022/11/22 15:23
 **/
public interface NoteInfoLogMapper {
    /**
     * 查询短信日志
     *
     * @param noteLogId 短信日志主键
     * @return 短信日志
     */
    public NoteInfoLog selectNoteInfoLogByNoteLogId(Long noteLogId);

    /**
     * 查询短信日志列表
     *
     * @param noteInfoLog 短信日志
     * @return 短信日志集合
     */
    public List<NoteInfoLog> selectNoteInfoLogList(NoteInfoLog noteInfoLog);

    /**
     * 新增短信日志
     *
     * @param noteInfoLog 短信日志
     * @return 结果
     */
    public int insertNoteInfoLog(NoteInfoLog noteInfoLog);

    /**
     * 删除短信日志
     *
     * @param noteLogId 短信日志主键
     * @return 结果
     */
    public int deleteNoteInfoLogByNoteLogId(Long noteLogId);

    /**
     * 批量删除短信日志
     *
     * @param noteLogIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNoteInfoLogByNoteLogIds(Long[] noteLogIds);
}
