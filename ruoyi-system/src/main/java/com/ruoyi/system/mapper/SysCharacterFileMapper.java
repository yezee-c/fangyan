package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.SysCharacterFile;

/**
 * 人物文件关联Mapper接口
 * 
 * @author ruoyi
 */
public interface SysCharacterFileMapper 
{
    /**
     * 查询人物的文件列表
     * 
     * @param characterId 人物ID
     * @return 文件关联列表
     */
    public List<SysCharacterFile> selectFilesByCharacterId(@Param("characterId") Long characterId);

    /**
     * 查询人物指定类型的文件
     * 
     * @param characterId 人物ID
     * @param fileType 文件类型
     * @return 文件关联列表
     */
    public List<SysCharacterFile> selectFilesByCharacterIdAndType(@Param("characterId") Long characterId, 
                                                                   @Param("fileType") String fileType);

    /**
     * 新增人物文件关联
     * 
     * @param characterFile 人物文件关联
     * @return 结果
     */
    public int insertCharacterFile(SysCharacterFile characterFile);

    /**
     * 批量新增人物文件关联
     * 
     * @param characterFiles 人物文件关联列表
     * @return 结果
     */
    public int batchInsertCharacterFile(List<SysCharacterFile> characterFiles);

    /**
     * 删除人物文件关联
     * 
     * @param id 关联ID
     * @return 结果
     */
    public int deleteCharacterFileById(Long id);

    /**
     * 删除人物的所有文件关联
     * 
     * @param characterId 人物ID
     * @return 结果
     */
    public int deleteCharacterFileByCharacterId(Long characterId);

    /**
     * 删除指定文件的关联
     * 
     * @param fileId 文件ID
     * @return 结果
     */
    public int deleteCharacterFileByFileId(Long fileId);
}
