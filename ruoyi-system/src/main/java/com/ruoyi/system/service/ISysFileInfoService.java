package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysFileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件信息Service接口
 * 
 * @author ruoyi
 */
public interface ISysFileInfoService 
{
    /**
     * 查询文件信息
     * 
     * @param fileId 文件信息主键
     * @return 文件信息
     */
    public SysFileInfo selectSysFileInfoByFileId(Long fileId);

    /**
     * 查询文件信息列表
     * 
     * @param sysFileInfo 文件信息
     * @return 文件信息集合
     */
    public List<SysFileInfo> selectSysFileInfoList(SysFileInfo sysFileInfo);

    /**
     * 新增文件信息
     * 
     * @param sysFileInfo 文件信息
     * @return 结果
     */
    public int insertSysFileInfo(SysFileInfo sysFileInfo);

    /**
     * 修改文件信息
     * 
     * @param sysFileInfo 文件信息
     * @return 结果
     */
    public int updateSysFileInfo(SysFileInfo sysFileInfo);

    /**
     * 批量删除文件信息
     * 
     * @param fileIds 需要删除的文件信息主键集合
     * @return 结果
     */
    public int deleteSysFileInfoByFileIds(Long[] fileIds);

    /**
     * 删除文件信息
     * 
     * @param fileId 文件信息主键
     * @return 结果
     */
    public int deleteSysFileInfoByFileId(Long fileId);

    /**
     * 上传文件
     * 
     * @param files 文件
     * @param isPublic 是否公共文件
     * @return 结果
     */
    public List<SysFileInfo> uploadFiles(MultipartFile[] files, String isPublic) throws Exception;

    /**
     * 上传单个私有文件
     * 
     * @param file 文件
     * @return 文件ID
     */
    public Long uploadPrivateFile(MultipartFile file) throws Exception;

    /**
     * 下载文件
     * 
     * @param fileId 文件ID
     * @return 文件信息
     */
    public SysFileInfo downloadFile(Long fileId) throws Exception;

    /**
     * 获取公共文件展示列表（无需权限）
     * 
     * @param fileType 文件类型（image/video，可为null）
     * @return 文件信息集合
     */
    public List<SysFileInfo> getPublicGalleryList(String fileType) throws Exception;
}
