package com.yuepei.system.domain;

import com.yuepei.common.annotation.Excel;
import com.yuepei.common.core.domain.BaseEntity;
import com.yuepei.common.core.domain.TreeEntity;
import lombok.Data;
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
 * @create ：2022/11/8 10:00
 **/
public class Hospital extends TreeEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long hospitalId;

    /** 名称 */
    @Excel(name = "名称")
    private String hospitalName;

    /**  */
    @Excel(name = "")
    private String hospitalRule;

    private Long proportion;

    private Long type;

    public Long getType(){
        return type;
    }

    public void setType(Long type){
        this.type = type;
    }

    public void setProportion(Long proportion){
        this.proportion = proportion;
    }

    public Long getProportion(){
        return proportion;
    }

    public void setHospitalId(Long hospitalId)
    {
        this.hospitalId = hospitalId;
    }

    public Long getHospitalId()
    {
        return hospitalId;
    }
    public void setHospitalName(String hospitalName)
    {
        this.hospitalName = hospitalName;
    }

    public String getHospitalName()
    {
        return hospitalName;
    }
    public void setHospitalRule(String hospitalRule)
    {
        this.hospitalRule = hospitalRule;
    }

    public String getHospitalRule()
    {
        return hospitalRule;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("hospitalId", getHospitalId())
                .append("hospitalName", getHospitalName())
                .append("parentId", getParentId())
                .append("hospitalRule", getHospitalRule())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("proportion",getProportion())
                .append("type",getType())
                .toString();
    }
}
