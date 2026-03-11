package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 人物信息对象 sys_character_info
 * 
 * @author ruoyi
 */
public class SysCharacterInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 人物ID */
    private Long characterId;

    /** 人物姓名 */
    private String characterName;

    /** 人物头衔/职位 */
    private String characterTitle;

    /** 人物简介 */
    private String characterIntro;

    /** 详细介绍 */
    private String characterDescription;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    /** 头像文件ID */
    private Long avatarFileId;

    /** 封面图片文件ID */
    private Long coverFileId;

    /** 显示顺序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    /** 头像URL（非数据库字段） */
    private String avatarUrl;

    /** 封面URL（非数据库字段） */
    private String coverUrl;

    /** 关联的图片列表（非数据库字段） */
    private List<SysFileInfo> images;

    /** 关联的视频列表（非数据库字段） */
    private List<SysFileInfo> videos;

    /** 关联的文档列表（非数据库字段） */
    private List<SysFileInfo> documents;

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getCharacterTitle() {
        return characterTitle;
    }

    public void setCharacterTitle(String characterTitle) {
        this.characterTitle = characterTitle;
    }

    public String getCharacterIntro() {
        return characterIntro;
    }

    public void setCharacterIntro(String characterIntro) {
        this.characterIntro = characterIntro;
    }

    public String getCharacterDescription() {
        return characterDescription;
    }

    public void setCharacterDescription(String characterDescription) {
        this.characterDescription = characterDescription;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Long getAvatarFileId() {
        return avatarFileId;
    }

    public void setAvatarFileId(Long avatarFileId) {
        this.avatarFileId = avatarFileId;
    }

    public Long getCoverFileId() {
        return coverFileId;
    }

    public void setCoverFileId(Long coverFileId) {
        this.coverFileId = coverFileId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<SysFileInfo> getImages() {
        return images;
    }

    public void setImages(List<SysFileInfo> images) {
        this.images = images;
    }

    public List<SysFileInfo> getVideos() {
        return videos;
    }

    public void setVideos(List<SysFileInfo> videos) {
        this.videos = videos;
    }

    public List<SysFileInfo> getDocuments() {
        return documents;
    }

    public void setDocuments(List<SysFileInfo> documents) {
        this.documents = documents;
    }
}
