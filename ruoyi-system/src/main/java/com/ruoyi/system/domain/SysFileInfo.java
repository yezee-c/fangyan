package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文件信息对象 sys_file_info
 * 
 * @author ruoyi
 */
public class SysFileInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件ID */
    private Long fileId;

    /** 上传用户ID */
    @Excel(name = "上传用户ID")
    private Long userId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 原始文件名 */
    @Excel(name = "原始文件名")
    private String originalName;

    /** 文件路径（MinIO中的对象名） */
    @Excel(name = "文件路径")
    private String filePath;

    /** 文件大小（字节） */
    @Excel(name = "文件大小")
    private Long fileSize;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String fileType;

    /** MIME类型 */
    @Excel(name = "MIME类型")
    private String contentType;

    /** 文件扩展名 */
    @Excel(name = "文件扩展名")
    private String fileExt;

    /** 存储桶名称 */
    @Excel(name = "存储桶名称")
    private String bucketName;

    /** 是否公共文件（0私有 1公共） */
    @Excel(name = "是否公共文件", readConverterExp = "0=私有,1=公共")
    private String isPublic;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 用户名称（关联查询） */
    @Excel(name = "上传用户")
    private String userName;

    /** 文件下载URL（临时） */
    private String fileUrl;

    public void setFileId(Long fileId) 
    {
        this.fileId = fileId;
    }

    public Long getFileId() 
    {
        return fileId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setOriginalName(String originalName) 
    {
        this.originalName = originalName;
    }

    public String getOriginalName() 
    {
        return originalName;
    }

    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }

    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setContentType(String contentType) 
    {
        this.contentType = contentType;
    }

    public String getContentType() 
    {
        return contentType;
    }

    public void setFileExt(String fileExt) 
    {
        this.fileExt = fileExt;
    }

    public String getFileExt() 
    {
        return fileExt;
    }

    public void setBucketName(String bucketName) 
    {
        this.bucketName = bucketName;
    }

    public String getBucketName() 
    {
        return bucketName;
    }

    public void setIsPublic(String isPublic) 
    {
        this.isPublic = isPublic;
    }

    public String getIsPublic() 
    {
        return isPublic;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("fileId", getFileId())
            .append("userId", getUserId())
            .append("fileName", getFileName())
            .append("originalName", getOriginalName())
            .append("filePath", getFilePath())
            .append("fileSize", getFileSize())
            .append("fileType", getFileType())
            .append("contentType", getContentType())
            .append("fileExt", getFileExt())
            .append("bucketName", getBucketName())
            .append("isPublic", getIsPublic())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
