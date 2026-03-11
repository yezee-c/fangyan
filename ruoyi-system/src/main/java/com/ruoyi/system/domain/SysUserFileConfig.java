package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户文件配置对象 sys_user_file_config
 * 
 * @author ruoyi
 */
public class SysUserFileConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 单个文件最大大小（字节），默认10MB */
    @Excel(name = "单个文件最大大小", readConverterExp = "字节")
    private Long maxFileSize;

    /** 最大存储容量（字节），默认1GB */
    @Excel(name = "最大存储容量", readConverterExp = "字节")
    private Long maxStorage;

    /** 已使用存储（字节） */
    @Excel(name = "已使用存储", readConverterExp = "字节")
    private Long usedStorage;

    /** 用户名称（关联查询） */
    @Excel(name = "用户名称")
    private String userName;

    /** 用户昵称（关联查询） */
    @Excel(name = "用户昵称")
    private String nickName;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setMaxFileSize(Long maxFileSize) 
    {
        this.maxFileSize = maxFileSize;
    }

    public Long getMaxFileSize() 
    {
        return maxFileSize;
    }

    public void setMaxStorage(Long maxStorage) 
    {
        this.maxStorage = maxStorage;
    }

    public Long getMaxStorage() 
    {
        return maxStorage;
    }

    public void setUsedStorage(Long usedStorage) 
    {
        this.usedStorage = usedStorage;
    }

    public Long getUsedStorage() 
    {
        return usedStorage;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("maxFileSize", getMaxFileSize())
            .append("maxStorage", getMaxStorage())
            .append("usedStorage", getUsedStorage())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
