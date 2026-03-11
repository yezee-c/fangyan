package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysUserFileConfig;

/**
 * 用户文件配置Service接口
 * 
 * @author ruoyi
 */
public interface ISysUserFileConfigService 
{
    /**
     * 查询用户文件配置
     * 
     * @param userId 用户ID
     * @return 用户文件配置
     */
    public SysUserFileConfig selectSysUserFileConfigByUserId(Long userId);

    /**
     * 查询用户文件配置列表
     * 
     * @param sysUserFileConfig 用户文件配置
     * @return 用户文件配置集合
     */
    public List<SysUserFileConfig> selectSysUserFileConfigList(SysUserFileConfig sysUserFileConfig);

    /**
     * 新增用户文件配置
     * 
     * @param sysUserFileConfig 用户文件配置
     * @return 结果
     */
    public int insertSysUserFileConfig(SysUserFileConfig sysUserFileConfig);

    /**
     * 修改用户文件配置
     * 
     * @param sysUserFileConfig 用户文件配置
     * @return 结果
     */
    public int updateSysUserFileConfig(SysUserFileConfig sysUserFileConfig);

    /**
     * 批量删除用户文件配置
     * 
     * @param userIds 需要删除的用户文件配置主键集合
     * @return 结果
     */
    public int deleteSysUserFileConfigByUserIds(Long[] userIds);

    /**
     * 删除用户文件配置信息
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteSysUserFileConfigByUserId(Long userId);

    /**
     * 检查用户存储空间是否足够
     * 
     * @param userId 用户ID
     * @param fileSize 文件大小
     * @return 是否足够
     */
    public boolean checkStorage(Long userId, Long fileSize);

    /**
     * 增加用户已使用存储
     * 
     * @param userId 用户ID
     * @param size 文件大小
     * @return 结果
     */
    public int increaseUsedStorage(Long userId, Long size);

    /**
     * 减少用户已使用存储
     * 
     * @param userId 用户ID
     * @param size 文件大小
     * @return 结果
     */
    public int decreaseUsedStorage(Long userId, Long size);
}
