package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysCharacterInfo;

/**
 * 人物信息Mapper接口
 * 
 * @author ruoyi
 */
public interface SysCharacterInfoMapper 
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
     * 删除人物信息
     * 
     * @param characterId 人物ID
     * @return 结果
     */
    public int deleteSysCharacterInfoByCharacterId(Long characterId);

    /**
     * 批量删除人物信息
     * 
     * @param characterIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysCharacterInfoByCharacterIds(Long[] characterIds);
}
