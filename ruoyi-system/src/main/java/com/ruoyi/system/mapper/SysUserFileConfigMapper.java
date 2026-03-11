package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysUserFileConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户文件配置Mapper接口
 * 
 * @author ruoyi
 */
@Mapper
public interface SysUserFileConfigMapper 
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
     * 删除用户文件配置
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteSysUserFileConfigByUserId(Long userId);

    /**
     * 批量删除用户文件配置
     * 
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUserFileConfigByUserIds(Long[] userIds);

    /**
     * 增加用户已使用存储
     * 
     * @param userId 用户ID
     * @param size 文件大小
     * @return 结果
     */
    public int increaseUsedStorage(@Param("userId") Long userId, @Param("size") Long size);

    /**
     * 减少用户已使用存储
     * 
     * @param userId 用户ID
     * @param size 文件大小
     * @return 结果
     */
    public int decreaseUsedStorage(@Param("userId") Long userId, @Param("size") Long size);
}
