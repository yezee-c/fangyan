package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysCharacterInfo;
import com.ruoyi.system.domain.SysCharacterFile;

/**
 * 人物信息Service接口
 * 
 * @author ruoyi
 */
public interface ISysCharacterInfoService 
{
    /**
     * 查询人物信息
     * 
     * @param characterId 人物ID
     * @return 人物信息
     */
    public SysCharacterInfo selectSysCharacterInfoByCharacterId(Long characterId);

    /**
     * 查询人物信息列表
     * 
     * @param sysCharacterInfo 人物信息
     * @return 人物信息集合
     */
    public List<SysCharacterInfo> selectSysCharacterInfoList(SysCharacterInfo sysCharacterInfo);

    /**
     * 新增人物信息
     * 
     * @param sysCharacterInfo 人物信息
     * @return 结果
     */
    public int insertSysCharacterInfo(SysCharacterInfo sysCharacterInfo);

    /**
     * 修改人物信息
     * 
     * @param sysCharacterInfo 人物信息
     * @return 结果
     */
    public int updateSysCharacterInfo(SysCharacterInfo sysCharacterInfo);

    /**
     * 批量删除人物信息
     * 
     * @param characterIds 需要删除的人物ID
     * @return 结果
     */
    public int deleteSysCharacterInfoByCharacterIds(Long[] characterIds);

    /**
     * 删除人物信息
     * 
     * @param characterId 人物ID
     * @return 结果
     */
    public int deleteSysCharacterInfoByCharacterId(Long characterId);

    /**
     * 为人物添加文件关联
     * 
     * @param characterId 人物ID
     * @param fileIds 文件ID列表
     * @param fileType 文件类型
     * @param fileCategory 文件分类
     * @return 结果
     */
    public int addCharacterFiles(Long characterId, Long[] fileIds, String fileType, String fileCategory);

    /**
     * 获取人物关联文件列表
     * 
     * @param characterId 人物ID
     * @return 文件列表
     */
    public List<SysCharacterFile> getCharacterFiles(Long characterId);

    /**
     * 删除人物文件关联
     * 
     * @param id 关联ID
     * @return 结果
     */
    public int deleteCharacterFile(Long id);

    /**
     * 清空人物的所有文件关联
     * 
     * @param characterId 人物ID
     */
    public void clearCharacterFiles(Long characterId);

    /**
     * 获取公开人物画廊列表（无需权限）
     * 
     * @return 人物列表
     */
    public List<SysCharacterInfo> getPublicCharacterGallery() throws Exception;

    /**
     * 获取人物详情（含文件，无需权限）
     * 
     * @param characterId 人物ID
     * @return 人物详情
     */
    public SysCharacterInfo getCharacterDetail(Long characterId) throws Exception;
}
