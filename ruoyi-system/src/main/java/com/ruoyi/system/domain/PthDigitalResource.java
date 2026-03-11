package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数字馆资源对象 pth_digital_resource
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
public class PthDigitalResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 资源名称 */
    @Excel(name = "资源名称")
    private String name;

    /** 资源类型（image/video/audio/document） */
    @Excel(name = "资源类型", readConverterExp = "image=图片,video=视频,audio=音频,document=文档")
    private String type;

    /** 资源URL */
    @Excel(name = "资源URL")
    private String url;

    /** 资源大小（字节） */
    @Excel(name = "资源大小")
    private Long size;

    /** 文件扩展名 */
    @Excel(name = "文件扩展名")
    private String extension;

    /** 描述信息 */
    @Excel(name = "描述信息")
    private String description;

    /** 创建者ID */
    private Long createById;

    /** 创建者姓名 */
    @Excel(name = "创建者姓名")
    private String createByName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setSize(Long size) 
    {
        this.size = size;
    }

    public Long getSize() 
    {
        return size;
    }
    public void setExtension(String extension) 
    {
        this.extension = extension;
    }

    public String getExtension() 
    {
        return extension;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setCreateById(Long createById) 
    {
        this.createById = createById;
    }

    public Long getCreateById() 
    {
        return createById;
    }
    public void setCreateByName(String createByName) 
    {
        this.createByName = createByName;
    }

    public String getCreateByName() 
    {
        return createByName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("url", getUrl())
            .append("size", getSize())
            .append("extension", getExtension())
            .append("description", getDescription())
            .append("createById", getCreateById())
            .append("createByName", getCreateByName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}