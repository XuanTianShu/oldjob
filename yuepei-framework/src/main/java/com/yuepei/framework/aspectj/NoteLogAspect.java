package com.yuepei.framework.aspectj;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yuepei.common.annotation.NoteLog;
import com.yuepei.common.core.domain.entity.SysRole;
import com.yuepei.common.core.domain.model.LoginUser;
import com.yuepei.common.enums.BusinessStatus;
import com.yuepei.common.enums.HttpMethod;
import com.yuepei.common.filter.PropertyPreExcludeFilter;
import com.yuepei.common.utils.DateUtils;
import com.yuepei.common.utils.SecurityUtils;
import com.yuepei.common.utils.ServletUtils;
import com.yuepei.common.utils.StringUtils;
import com.yuepei.common.utils.ip.IpUtils;
import com.yuepei.framework.manager.AsyncManager;
import com.yuepei.framework.manager.factory.AsyncFactory;
import com.yuepei.system.domain.NoteInfoLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
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
 * @create ：2022/11/21 17:14
 **/
@Aspect
@Component
public class NoteLogAspect {

    private static final Logger log = LoggerFactory.getLogger(NoteLogAspect.class);

    /** 排除敏感属性字段 */
    public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };


    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, NoteLog controllerLog, Object jsonResult)
    {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    protected void handleLog(final JoinPoint joinPoint, NoteLog controllerLog, final Exception e, Object jsonResult)
    {
        try
        {
            // 获取当前的用户
            LoginUser loginUser = SecurityUtils.getLoginUser();

            // *========数据库日志=========*//
            NoteInfoLog noteInfoLog = new NoteInfoLog();

            noteInfoLog.setSendStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            noteInfoLog.setNoteLogIp(ip);
            noteInfoLog.setNoteLogUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            noteInfoLog.setSendTime(DateUtils.getNowDate());
            if (loginUser != null)
            {
                //设置发送短信用户及角色
                noteInfoLog.setNoteLogName(loginUser.getUsername());
                List<SysRole> roles = loginUser.getUser().getRoles();
                noteInfoLog.setNoteLogRole(roles.get(0).getRoleName());
             }

            if (e != null)
            {
                noteInfoLog.setSendStatus(BusinessStatus.FAIL.ordinal());
                noteInfoLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置请求方式
            noteInfoLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, noteInfoLog, jsonResult);
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordNote(noteInfoLog));
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param noteLog 日志
     * @param noteInfoLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, NoteLog noteLog, NoteInfoLog noteInfoLog, Object jsonResult) throws Exception
    {
        // 设置标题
        noteInfoLog.setNoteLogTitle(noteLog.title());
        // 是否需要保存request，参数和值
        if (noteLog.isSaveRequestData())
        {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, noteInfoLog);
        }
        // 是否需要保存response，参数和值
        if (noteLog.isSaveResponseData() && StringUtils.isNotNull(jsonResult))
        {
            JSONObject jsonObject = JSONObject.parseObject(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
            noteInfoLog.setReceiptStatus(jsonObject.getString("code"));
            noteInfoLog.setReceiptTime(DateUtils.getNowDate());
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param noteInfoLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, NoteInfoLog noteInfoLog) throws Exception
    {
        String requestMethod = noteInfoLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))
        {
            JSONObject jsonObject = JSONObject.parseObject(argsArrayToString(joinPoint.getArgs()));
            noteInfoLog.setNoteLogPhone(jsonObject.getString("phoneNumber"));
        }
        else
        {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            noteInfoLog.setNoteLogPhone(ServletUtils.getParameter("phoneNumber"));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (StringUtils.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter());
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }
    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter()
    {
        return new PropertyPreExcludeFilter().addExcludes(EXCLUDE_PROPERTIES);
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o)
    {
        Class<?> clazz = o.getClass();
        if (clazz.isArray())
        {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        else if (Collection.class.isAssignableFrom(clazz))
        {
            Collection collection = (Collection) o;
            for (Object value : collection)
            {
                return value instanceof MultipartFile;
            }
        }
        else if (Map.class.isAssignableFrom(clazz))
        {
            Map map = (Map) o;
            for (Object value : map.entrySet())
            {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
