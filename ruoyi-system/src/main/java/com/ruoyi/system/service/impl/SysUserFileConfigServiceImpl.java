package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysUserFileConfigMapper;
import com.ruoyi.system.domain.SysUserFileConfig;
import com.ruoyi.system.service.ISysUserFileConfigService;

/**
 * 用户文件配置Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserFileConfigServiceImpl implements ISysUserFileConfigService 
{
    @Autowired
    private SysUserFileConfigMapper sysUserFileConfigMapper;

    /**
     * 查询用户文件配置
     * 
     * @param userId 用户ID
     * @return 用户文件配置
     */
    @Override
    public SysUserFileConfig selectSysUserFileConfigByUserId(Long userId)
    {
        SysUserFileConfig config = sysUserFileConfigMapper.selectSysUserFileConfigByUserId(userId);
        if (config == null)
        {
            // 如果用户没有配置，创建默认配置
            config = new SysUserFileConfig();
            config.setUserId(userId);
            config.setMaxFileSize(10485760L); // 默认10MB
            config.setMaxStorage(1073741824L); // 默认1GB
            config.setUsedStorage(0L);
            config.setCreateTime(new Date());
            sysUserFileConfigMapper.insertSysUserFileConfig(config);
        }
        return config;
    }

    /**
     * 查询用户文件配置列表
     * 
     * @param sysUserFileConfig 用户文件配置
     * @return 用户文件配置
     */
    @Override
    public List<SysUserFileConfig> selectSysUserFileConfigList(SysUserFileConfig sysUserFileConfig)
    {
        return sysUserFileConfigMapper.selectSysUserFileConfigList(sysUserFileConfig);
    }

    /**
     * 新增用户文件配置
     * 
     * @param sysUserFileConfig 用户文件配置
     * @return 结果
     */
    @Override
    public int insertSysUserFileConfig(SysUserFileConfig sysUserFileConfig)
    {
        sysUserFileConfig.setCreateTime(new Date());
        if (sysUserFileConfig.getUsedStorage() == null)
        {
            sysUserFileConfig.setUsedStorage(0L);
        }
        return sysUserFileConfigMapper.insertSysUserFileConfig(sysUserFileConfig);
    }

    /**
     * 修改用户文件配置
     * 
     * @param sysUserFileConfig 用户文件配置
     * @return 结果
     */
    @Override
    public int updateSysUserFileConfig(SysUserFileConfig sysUserFileConfig)
    {
        sysUserFileConfig.setUpdateTime(new Date());
        return sysUserFileConfigMapper.updateSysUserFileConfig(sysUserFileConfig);
    }

    /**
     * 批量删除用户文件配置
     * 
     * @param userIds 需要删除的用户文件配置主键
     * @return 结果
     */
    @Override
    public int deleteSysUserFileConfigByUserIds(Long[] userIds)
    {
        return sysUserFileConfigMapper.deleteSysUserFileConfigByUserIds(userIds);
    }

    /**
     * 删除用户文件配置信息
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteSysUserFileConfigByUserId(Long userId)
    {
        return sysUserFileConfigMapper.deleteSysUserFileConfigByUserId(userId);
    }

    /**
     * 检查用户存储空间是否足够
     * 
     * @param userId 用户ID
     * @param fileSize 文件大小
     * @return 是否足够
     */
    @Override
    public boolean checkStorage(Long userId, Long fileSize)
    {
        SysUserFileConfig config = selectSysUserFileConfigByUserId(userId);
        if (config == null)
        {
            return false;
        }
        
        // 检查单个文件大小限制
        if (fileSize > config.getMaxFileSize())
        {
            return false;
        }
        
        // 检查总存储空间限制
        long newUsedStorage = config.getUsedStorage() + fileSize;
        if (newUsedStorage > config.getMaxStorage())
        {
            return false;
        }
        
        return true;
    }

    /**
     * 增加用户已使用存储
     * 
     * @param userId 用户ID
     * @param size 文件大小
     * @return 结果
     */
    @Override
    public int increaseUsedStorage(Long userId, Long size)
    {
        return sysUserFileConfigMapper.increaseUsedStorage(userId, size);
    }

    /**
     * 减少用户已使用存储
     * 
     * @param userId 用户ID
     * @param size 文件大小
     * @return 结果
     */
    @Override
    public int decreaseUsedStorage(Long userId, Long size)
    {
        return sysUserFileConfigMapper.decreaseUsedStorage(userId, size);
    }
}
