package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.MinioUtils;
import com.ruoyi.system.domain.SysCharacterFile;
import com.ruoyi.system.domain.SysFileInfo;
import com.ruoyi.system.mapper.SysCharacterFileMapper;
import com.ruoyi.system.mapper.SysFileInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysCharacterInfoMapper;
import com.ruoyi.system.domain.SysCharacterInfo;
import com.ruoyi.system.service.ISysCharacterInfoService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 人物信息Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysCharacterInfoServiceImpl implements ISysCharacterInfoService 
{
    private static final Logger logger = LoggerFactory.getLogger(SysCharacterInfoServiceImpl.class);

    @Autowired
    private SysCharacterInfoMapper sysCharacterInfoMapper;

    @Autowired
    private SysCharacterFileMapper sysCharacterFileMapper;

    @Autowired
    private SysFileInfoMapper sysFileInfoMapper;

    @Autowired
    private MinioUtils minioUtils;

    @Override
    public SysCharacterInfo selectSysCharacterInfoByCharacterId(Long characterId)
    {
        return sysCharacterInfoMapper.selectSysCharacterInfoByCharacterId(characterId);
    }

    @Override
    public List<SysCharacterInfo> selectSysCharacterInfoList(SysCharacterInfo sysCharacterInfo)
    {
        return sysCharacterInfoMapper.selectSysCharacterInfoList(sysCharacterInfo);
    }

    @Override
    public int insertSysCharacterInfo(SysCharacterInfo sysCharacterInfo)
    {
        sysCharacterInfo.setCreateTime(new Date());
        return sysCharacterInfoMapper.insertSysCharacterInfo(sysCharacterInfo);
    }

    @Override
    public int updateSysCharacterInfo(SysCharacterInfo sysCharacterInfo)
    {
        sysCharacterInfo.setUpdateTime(new Date());
        return sysCharacterInfoMapper.updateSysCharacterInfo(sysCharacterInfo);
    }

    @Override
    @Transactional
    public int deleteSysCharacterInfoByCharacterIds(Long[] characterIds)
    {
        // 删除关联的文件
        for (Long characterId : characterIds)
        {
            sysCharacterFileMapper.deleteCharacterFileByCharacterId(characterId);
        }
        return sysCharacterInfoMapper.deleteSysCharacterInfoByCharacterIds(characterIds);
    }

    @Override
    @Transactional
    public int deleteSysCharacterInfoByCharacterId(Long characterId)
    {
        // 删除关联的文件
        sysCharacterFileMapper.deleteCharacterFileByCharacterId(characterId);
        return sysCharacterInfoMapper.deleteSysCharacterInfoByCharacterId(characterId);
    }

    @Override
    @Transactional
    public int addCharacterFiles(Long characterId, Long[] fileIds, String fileType, String fileCategory)
    {
        List<SysCharacterFile> characterFiles = new ArrayList<>();
        int sortOrder = 0;
        for (Long fileId : fileIds)
        {
            SysCharacterFile characterFile = new SysCharacterFile();
            characterFile.setCharacterId(characterId);
            characterFile.setFileId(fileId);
            characterFile.setFileType(fileType);
            characterFile.setFileCategory(fileCategory);
            characterFile.setSortOrder(sortOrder++);
            characterFile.setCreateTime(new Date());
            characterFiles.add(characterFile);
        }
        
        if (!characterFiles.isEmpty())
        {
            return sysCharacterFileMapper.batchInsertCharacterFile(characterFiles);
        }
        return 0;
    }

    @Override
    public int deleteCharacterFile(Long id)
    {
        return sysCharacterFileMapper.deleteCharacterFileById(id);
    }

    @Override
    public List<SysCharacterFile> getCharacterFiles(Long characterId)
    {
        List<SysCharacterFile> characterFiles = sysCharacterFileMapper.selectFilesByCharacterId(characterId);
        
        // 为每个文件生成URL
        for (SysCharacterFile characterFile : characterFiles)
        {
            if (characterFile.getFileInfo() != null)
            {
                generateFileUrl(characterFile.getFileInfo());
            }
        }
        
        return characterFiles;
    }

    @Override
    public void clearCharacterFiles(Long characterId)
    {
        sysCharacterFileMapper.deleteCharacterFileByCharacterId(characterId);
    }

    @Override
    public List<SysCharacterInfo> getPublicCharacterGallery() throws Exception
    {
        SysCharacterInfo query = new SysCharacterInfo();
        query.setStatus("0"); // 只查询正常状态
        List<SysCharacterInfo> list = sysCharacterInfoMapper.selectSysCharacterInfoList(query);
        
        // 为每个人物生成头像和封面URL
        for (SysCharacterInfo character : list)
        {
            generateFileUrls(character);
        }
        
        return list;
    }

    @Override
    public SysCharacterInfo getCharacterDetail(Long characterId) throws Exception
    {
        SysCharacterInfo character = sysCharacterInfoMapper.selectSysCharacterInfoByCharacterId(characterId);
        if (character == null)
        {
            return null;
        }
        
        // 仅显示正常状态的人物
        if (!"0".equals(character.getStatus()))
        {
            return null;
        }
        
        // 生成头像和封面URL
        generateFileUrls(character);
        
        // 获取关联的图片
        List<SysCharacterFile> imageFiles = sysCharacterFileMapper.selectFilesByCharacterIdAndType(characterId, "image");
        character.setImages(imageFiles.stream()
                .map(cf -> generateFileUrl(cf.getFileInfo()))
                .collect(Collectors.toList()));
        
        // 获取关联的视频
        List<SysCharacterFile> videoFiles = sysCharacterFileMapper.selectFilesByCharacterIdAndType(characterId, "video");
        character.setVideos(videoFiles.stream()
                .map(cf -> generateFileUrl(cf.getFileInfo()))
                .collect(Collectors.toList()));
        
        // 获取关联的文档
        List<SysCharacterFile> docFiles = sysCharacterFileMapper.selectFilesByCharacterIdAndType(characterId, "document");
        character.setDocuments(docFiles.stream()
                .map(cf -> generateFileUrl(cf.getFileInfo()))
                .collect(Collectors.toList()));
        
        return character;
    }

    /**
     * 为人物生成头像和封面URL
     */
    private void generateFileUrls(SysCharacterInfo character)
    {
        logger.info("开始生成文件URL，人物ID: {}, 头像FileId: {}, 封面FileId: {}", 
            character.getCharacterId(), character.getAvatarFileId(), character.getCoverFileId());
        
        try
        {
            if (character.getAvatarFileId() != null)
            {
                SysFileInfo avatarFile = sysFileInfoMapper.selectSysFileInfoByFileId(character.getAvatarFileId());
                logger.info("查询头像文件信息: {}", avatarFile != null ? avatarFile.getFileName() : "null");
                if (avatarFile != null)
                {
                    try
                    {
                        String url = minioUtils.getPresignedObjectUrl(avatarFile.getBucketName(), 
                                avatarFile.getFilePath(), 7, TimeUnit.DAYS);
                        logger.info("生成头像URL成功: {}", url);
                        character.setAvatarUrl(url);
                    }
                    catch (Exception e)
                    {
                        logger.error("生成头像URL失败，文件: bucket={}, path={}", 
                            avatarFile.getBucketName(), avatarFile.getFilePath(), e);
                    }
                }
            }
            
            if (character.getCoverFileId() != null)
            {
                SysFileInfo coverFile = sysFileInfoMapper.selectSysFileInfoByFileId(character.getCoverFileId());
                logger.info("查询封面文件信息: {}", coverFile != null ? coverFile.getFileName() : "null");
                if (coverFile != null)
                {
                    try
                    {
                        String url = minioUtils.getPresignedObjectUrl(coverFile.getBucketName(), 
                                coverFile.getFilePath(), 7, TimeUnit.DAYS);
                        logger.info("生成封面URL成功: {}", url);
                        character.setCoverUrl(url);
                    }
                    catch (Exception e)
                    {
                        logger.error("生成封面URL失败，文件: bucket={}, path={}", 
                            coverFile.getBucketName(), coverFile.getFilePath(), e);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("生成文件URL失败，人物ID: " + character.getCharacterId(), e);
        }
    }

    /**
     * 为文件信息生成URL
     */
    private SysFileInfo generateFileUrl(SysFileInfo fileInfo)
    {
        if (fileInfo != null)
        {
            try
            {
                // 预览使用普通URL
                String url = minioUtils.getPresignedObjectUrl(fileInfo.getBucketName(), 
                        fileInfo.getFilePath(), 7, TimeUnit.DAYS);
                fileInfo.setFileUrl(url);
            }
            catch (Exception e)
            {
                logger.error("生成文件URL失败", e);
            }
        }
        return fileInfo;
    }
}
