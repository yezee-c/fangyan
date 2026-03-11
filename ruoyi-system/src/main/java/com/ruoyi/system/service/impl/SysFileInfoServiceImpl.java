package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MinioUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserFileConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysFileInfoMapper;
import com.ruoyi.system.domain.SysFileInfo;
import com.ruoyi.system.service.ISysFileInfoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件信息Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysFileInfoServiceImpl implements ISysFileInfoService 
{
    private static final Logger logger = LoggerFactory.getLogger(SysFileInfoServiceImpl.class);

    @Autowired
    private SysFileInfoMapper sysFileInfoMapper;

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private ISysUserFileConfigService userFileConfigService;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 查询文件信息
     * 
     * @param fileId 文件信息主键
     * @return 文件信息
     */
    @Override
    public SysFileInfo selectSysFileInfoByFileId(Long fileId)
    {
        return sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
    }

    /**
     * 查询文件信息列表
     * 
     * @param sysFileInfo 文件信息
     * @return 文件信息
     */
    @Override
    public List<SysFileInfo> selectSysFileInfoList(SysFileInfo sysFileInfo)
    {
        List<SysFileInfo> list = sysFileInfoMapper.selectSysFileInfoList(sysFileInfo);
        // 为每个文件生成下载URL
        for (SysFileInfo fileInfo : list)
        {
            try
            {
                // 使用带文件名的方法，强制下载
                String url = minioUtils.getPresignedObjectUrl(fileInfo.getBucketName(), 
                        fileInfo.getFilePath(), fileInfo.getOriginalName(), 7, TimeUnit.DAYS);
                fileInfo.setFileUrl(url);
            }
            catch (Exception e)
            {
                // 忽略异常，继续处理其他文件
            }
        }
        return list;
    }

    /**
     * 新增文件信息
     * 
     * @param sysFileInfo 文件信息
     * @return 结果
     */
    @Override
    public int insertSysFileInfo(SysFileInfo sysFileInfo)
    {
        sysFileInfo.setCreateTime(new Date());
        return sysFileInfoMapper.insertSysFileInfo(sysFileInfo);
    }

    /**
     * 修改文件信息
     * 
     * @param sysFileInfo 文件信息
     * @return 结果
     */
    @Override
    public int updateSysFileInfo(SysFileInfo sysFileInfo)
    {
        sysFileInfo.setUpdateTime(new Date());
        return sysFileInfoMapper.updateSysFileInfo(sysFileInfo);
    }

    /**
     * 批量删除文件信息
     * 
     * @param fileIds 需要删除的文件信息主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysFileInfoByFileIds(Long[] fileIds)
    {
        int result = 0;
        for (Long fileId : fileIds)
        {
            result += deleteSysFileInfoByFileId(fileId);
        }
        return result;
    }

    /**
     * 删除文件信息
     * 
     * @param fileId 文件信息主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysFileInfoByFileId(Long fileId)
    {
        // 查询文件信息
        SysFileInfo fileInfo = sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
        if (fileInfo == null)
        {
            throw new ServiceException("文件不存在");
        }

        // 检查权限
        Long userId = SecurityUtils.getUserId();
        if (!userId.equals(fileInfo.getUserId()) && !SecurityUtils.isAdmin(userId))
        {
            throw new ServiceException("无权删除该文件");
        }

        try
        {
            // 从MinIO删除文件
            minioUtils.removeObject(fileInfo.getBucketName(), fileInfo.getFilePath());
            
            // 减少用户已使用存储
            userFileConfigService.decreaseUsedStorage(fileInfo.getUserId(), fileInfo.getFileSize());
            
            // 从数据库删除记录
            return sysFileInfoMapper.deleteSysFileInfoByFileId(fileId);
        }
        catch (Exception e)
        {
            throw new ServiceException("删除文件失败：" + e.getMessage());
        }
    }

    /**
     * 上传文件
     * 
     * @param files 文件
     * @param isPublic 是否公共文件
     * @return 结果
     */
    @Override
    @Transactional
    public List<SysFileInfo> uploadFiles(MultipartFile[] files, String isPublic) throws Exception
    {
        if (files == null || files.length == 0)
        {
            throw new ServiceException("请选择要上传的文件");
        }

        if (files.length > 50)
        {
            throw new ServiceException("单次最多上传50个文件");
        }

        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();
        
        // 确保bucket存在
        try
        {
            if (!minioUtils.bucketExists(bucketName))
            {
                minioUtils.makeBucket(bucketName);
            }
        }
        catch (Exception e)
        {
            if (!e.getMessage().contains("you already own it"))
            {
                throw new ServiceException("创建bucket失败：" + e.getMessage());
            }
        }

        List<SysFileInfo> fileInfoList = new ArrayList<>();
        
        for (MultipartFile file : files)
        {
            if (file.isEmpty())
            {
                continue;
            }

            long fileSize = file.getSize();
            
            // 检查存储空间
            if (!userFileConfigService.checkStorage(userId, fileSize))
            {
                throw new ServiceException("文件[" + file.getOriginalFilename() + "]超过存储限制");
            }

            // 生成文件路径
            String originalFilename = file.getOriginalFilename();
            String fileExt = "";
            if (originalFilename != null && originalFilename.contains("."))
            {
                fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String fileName = UUID.randomUUID().toString().replace("-", "") + fileExt;
            String filePath = (isPublic.equals("1") ? "public/" : "private/") + userId + "/" + fileName;

            try
            {
                // 上传到MinIO
                minioUtils.uploadFile(file, bucketName, filePath);

                // 保存文件信息到数据库
                SysFileInfo fileInfo = new SysFileInfo();
                fileInfo.setUserId(userId);
                fileInfo.setFileName(fileName);
                fileInfo.setOriginalName(originalFilename);
                fileInfo.setFilePath(filePath);
                fileInfo.setFileSize(fileSize);
                fileInfo.setContentType(file.getContentType());
                fileInfo.setFileExt(fileExt);
                fileInfo.setBucketName(bucketName);
                fileInfo.setIsPublic(isPublic);
                fileInfo.setStatus("0");
                fileInfo.setCreateBy(username);
                fileInfo.setCreateTime(new Date());

                // 判断文件类型
                String contentType = file.getContentType();
                if (contentType != null)
                {
                    if (contentType.startsWith("image/"))
                    {
                        fileInfo.setFileType("image");
                    }
                    else if (contentType.startsWith("video/"))
                    {
                        fileInfo.setFileType("video");
                    }
                    else if (contentType.startsWith("audio/"))
                    {
                        fileInfo.setFileType("audio");
                    }
                    else if (contentType.contains("pdf"))
                    {
                        fileInfo.setFileType("pdf");
                    }
                    else if (contentType.contains("word") || contentType.contains("document"))
                    {
                        fileInfo.setFileType("word");
                    }
                    else if (contentType.contains("excel") || contentType.contains("spreadsheet"))
                    {
                        fileInfo.setFileType("excel");
                    }
                    else
                    {
                        fileInfo.setFileType("other");
                    }
                }

                sysFileInfoMapper.insertSysFileInfo(fileInfo);
                
                // 增加用户已使用存储
                userFileConfigService.increaseUsedStorage(userId, fileSize);

                // 生成下载URL（带原始文件名，强制下载）
                String url = minioUtils.getPresignedObjectUrl(bucketName, filePath, originalFilename, 7, TimeUnit.DAYS);
                fileInfo.setFileUrl(url);

                fileInfoList.add(fileInfo);
            }
            catch (Exception e)
            {
                logger.error("文件上传失败，文件名：{}，错误信息：{}", originalFilename, e.getMessage(), e);
                throw new ServiceException("文件[" + originalFilename + "]上传失败：" + e.getMessage());
            }
        }

        return fileInfoList;
    }

    /**
     * 上传单个私有文件
     */
    @Override
    public Long uploadPrivateFile(MultipartFile file) throws Exception
    {
        if (file == null || file.isEmpty())
        {
            throw new ServiceException("请选择要上传的文件");
        }

        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();
        
        // 确保bucket存在
        try
        {
            if (!minioUtils.bucketExists(bucketName))
            {
                minioUtils.makeBucket(bucketName);
            }
        }
        catch (Exception e)
        {
            if (!e.getMessage().contains("you already own it"))
            {
                throw new ServiceException("创建bucket失败：" + e.getMessage());
            }
        }

        long fileSize = file.getSize();
        
        // 检查存储空间
        if (!userFileConfigService.checkStorage(userId, fileSize))
        {
            throw new ServiceException("文件[" + file.getOriginalFilename() + "]超过存储限制");
        }

        // 生成文件路径
        String originalFilename = file.getOriginalFilename();
        String fileExt = "";
        if (originalFilename != null && originalFilename.contains("."))
        {
            fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String fileName = UUID.randomUUID().toString().replace("-", "") + fileExt;
        String filePath = "private/" + userId + "/" + fileName;

        try
        {
            // 上传到MinIO
            minioUtils.uploadFile(file, bucketName, filePath);

            // 保存文件信息到数据库
            SysFileInfo fileInfo = new SysFileInfo();
            fileInfo.setUserId(userId);
            fileInfo.setFileName(fileName);
            fileInfo.setOriginalName(originalFilename);
            fileInfo.setFilePath(filePath);
            fileInfo.setFileSize(fileSize);
            fileInfo.setContentType(file.getContentType());
            fileInfo.setFileExt(fileExt);
            fileInfo.setBucketName(bucketName);
            fileInfo.setIsPublic("0"); // 私有文件
            fileInfo.setStatus("0");
            fileInfo.setCreateBy(username);
            fileInfo.setCreateTime(new Date());

            // 判断文件类型
            String contentType = file.getContentType();
            if (contentType != null)
            {
                if (contentType.startsWith("image/"))
                {
                    fileInfo.setFileType("image");
                }
                else if (contentType.startsWith("video/"))
                {
                    fileInfo.setFileType("video");
                }
                else if (contentType.startsWith("audio/"))
                {
                    fileInfo.setFileType("audio");
                }
                else if (contentType.contains("pdf"))
                {
                    fileInfo.setFileType("pdf");
                }
                else if (contentType.contains("word") || contentType.contains("document"))
                {
                    fileInfo.setFileType("word");
                }
                else if (contentType.contains("excel") || contentType.contains("spreadsheet"))
                {
                    fileInfo.setFileType("excel");
                }
                else
                {
                    fileInfo.setFileType("document");
                }
            }

            sysFileInfoMapper.insertSysFileInfo(fileInfo);
            
            // 增加用户已使用存储
            userFileConfigService.increaseUsedStorage(userId, fileSize);

            return fileInfo.getFileId();
        }
        catch (Exception e)
        {
            logger.error("文件上传失败，文件名：{}，错误信息：{}", originalFilename, e.getMessage(), e);
            throw new ServiceException("文件[" + originalFilename + "]上传失败：" + e.getMessage());
        }
    }

    /**
     * 下载文件
     * 
     * @param fileId 文件ID
     * @return 文件信息
     */
    @Override
    public SysFileInfo downloadFile(Long fileId) throws Exception
    {
        SysFileInfo fileInfo = sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
        if (fileInfo == null)
        {
            throw new ServiceException("文件不存在");
        }

        Long userId = SecurityUtils.getUserId();
        
        // 检查权限：公共文件所有人可下载，私有文件仅所有者和管理员可下载
        if (fileInfo.getIsPublic().equals("0"))
        {
            if (!userId.equals(fileInfo.getUserId()) && !SecurityUtils.isAdmin(userId))
            {
                throw new ServiceException("无权下载该文件");
            }
        }

        // 生成下载URL（带原始文件名，强制下载）
        String url = minioUtils.getPresignedObjectUrl(fileInfo.getBucketName(), 
                fileInfo.getFilePath(), fileInfo.getOriginalName(), 1, TimeUnit.HOURS);
        fileInfo.setFileUrl(url);

        return fileInfo;
    }

    /**
     * 获取公共文件展示列表（无需权限）
     * 
     * @param fileType 文件类型
     * @return 文件列表
     */
    @Override
    public List<SysFileInfo> getPublicGalleryList(String fileType) throws Exception
    {
        SysFileInfo query = new SysFileInfo();
        query.setIsPublic("1"); // 只查询公共文件
        query.setStatus("0"); // 只查询正常状态的文件
        
        if (fileType != null && !fileType.isEmpty())
        {
            query.setFileType(fileType);
        }
        
        List<SysFileInfo> list = sysFileInfoMapper.selectSysFileInfoList(query);
        
        // 为每个文件生成预览URL（不强制下载，用于在线预览）
        for (SysFileInfo fileInfo : list)
        {
            try
            {
                // 预览使用普通URL，不带Content-Disposition响应头
                String url = minioUtils.getPresignedObjectUrl(fileInfo.getBucketName(), 
                        fileInfo.getFilePath(), 7, TimeUnit.DAYS);
                fileInfo.setFileUrl(url);
            }
            catch (Exception e)
            {
                logger.error("生成文件URL失败，文件ID：{}", fileInfo.getFileId(), e);
            }
        }
        
        return list;
    }
}
