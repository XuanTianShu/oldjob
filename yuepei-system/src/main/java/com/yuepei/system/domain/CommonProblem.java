package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
 * @create ：2022/11/4 10:02
 **/
public class CommonProblem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 常见问题id */
    private Long commonProblemId;

    /** 常见问题 */
    @Excel(name = "常见问题")
    private String commonProblemTitle;

    /** 常见问题解决办法 */
    @Excel(name = "常见问题解决办法")
    private String commonProblemSolution;

    public void setCommonProblemId(Long commonProblemId)
    {
        this.commonProblemId = commonProblemId;
    }

    public Long getCommonProblemId()
    {
        return commonProblemId;
    }
    public void setCommonProblemTitle(String commonProblemTitle)
    {
        this.commonProblemTitle = commonProblemTitle;
    }

    public String getCommonProblemTitle()
    {
        return commonProblemTitle;
    }
    public void setCommonProblemSolution(String commonProblemSolution)
    {
        this.commonProblemSolution = commonProblemSolution;
    }

    public String getCommonProblemSolution()
    {
        return commonProblemSolution;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("commonProblemId", getCommonProblemId())
                .append("commonProblemTitle", getCommonProblemTitle())
                .append("commonProblemSolution", getCommonProblemSolution())
                .toString();
    }
}
