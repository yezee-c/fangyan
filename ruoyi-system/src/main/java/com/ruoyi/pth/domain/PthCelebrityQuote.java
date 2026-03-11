package com.ruoyi.pth.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 大别山名人名言对象 pth_celebrity_quote
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public class PthCelebrityQuote extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 名言ID */
    private Long quoteId;

    /** 名人姓名 */
    @Excel(name = "名人姓名")
    private String celebrityName;

    /** 名人照片URL */
    @Excel(name = "名人照片URL")
    private String photoUrl;

    /** 名言内容 */
    @Excel(name = "名言内容")
    private String quoteContent;

    /** 名人简介 */
    @Excel(name = "名人简介")
    private String biography;

    /** 主要成就 */
    @Excel(name = "主要成就")
    private String achievements;

    /** 与大别山的关联 */
    @Excel(name = "与大别山的关联")
    private String connection;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer displayOrder;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setQuoteId(Long quoteId) 
    {
        this.quoteId = quoteId;
    }

    public Long getQuoteId() 
    {
        return quoteId;
    }

    public void setCelebrityName(String celebrityName) 
    {
        this.celebrityName = celebrityName;
    }

    public String getCelebrityName() 
    {
        return celebrityName;
    }

    public void setPhotoUrl(String photoUrl) 
    {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() 
    {
        return photoUrl;
    }

    public void setQuoteContent(String quoteContent) 
    {
        this.quoteContent = quoteContent;
    }

    public String getQuoteContent() 
    {
        return quoteContent;
    }

    public void setBiography(String biography) 
    {
        this.biography = biography;
    }

    public String getBiography() 
    {
        return biography;
    }

    public void setAchievements(String achievements) 
    {
        this.achievements = achievements;
    }

    public String getAchievements() 
    {
        return achievements;
    }

    public void setConnection(String connection) 
    {
        this.connection = connection;
    }

    public String getConnection() 
    {
        return connection;
    }

    public void setDisplayOrder(Integer displayOrder) 
    {
        this.displayOrder = displayOrder;
    }

    public Integer getDisplayOrder() 
    {
        return displayOrder;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("quoteId", getQuoteId())
            .append("celebrityName", getCelebrityName())
            .append("photoUrl", getPhotoUrl())
            .append("quoteContent", getQuoteContent())
            .append("biography", getBiography())
            .append("achievements", getAchievements())
            .append("connection", getConnection())
            .append("displayOrder", getDisplayOrder())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
