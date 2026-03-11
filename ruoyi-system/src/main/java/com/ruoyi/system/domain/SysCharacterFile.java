package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 人物文件关联对象 sys_character_file
 * 
 * @author ruoyi
 */
public class SysCharacterFile
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 人物ID */
    private Long characterId;

    /** 文件ID */
    private Long fileId;

    /** 文件类型 */
    private String fileType;

    /** 文件分类 */
    private String fileCategory;

    /** 显示顺序 */
    private Integer sortOrder;

    /** 文件描述 */
    private String description;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 文件信息（非数据库字段） */
    private SysFileInfo fileInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SysFileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(SysFileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
