package com.ruoyi.pth.mapper;

import java.util.List;
import com.ruoyi.pth.domain.PthClass;
import org.apache.ibatis.annotations.Param;

/**
 * 普通话班级Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public interface PthClassMapper 
{
    /**
     * 查询普通话班级
     * 
     * @param classId 普通话班级主键
     * @return 普通话班级
     */
    public PthClass selectPthClassByClassId(Long classId);

    /**
     * 查询普通话班级列表
     * 
     * @param pthClass 普通话班级
     * @return 普通话班级集合
     */
    public List<PthClass> selectPthClassList(PthClass pthClass);

    /**
     * 根据教师ID查询班级列表
     * 
     * @param teacherId 教师ID
     * @return 普通话班级集合
     */
    public List<PthClass> selectPthClassListByTeacherId(Long teacherId);

    /**
     * 根据邀请码查询班级
     * 
     * @param invitationCode 邀请码
     * @return 普通话班级
     */
    public PthClass selectPthClassByInvitationCode(String invitationCode);

    /**
     * 检查邀请码是否存在
     * 
     * @param invitationCode 邀请码
     * @return 结果
     */
    public int checkInvitationCodeUnique(String invitationCode);

    /**
     * 新增普通话班级
     * 
     * @param pthClass 普通话班级
     * @return 结果
     */
    public int insertPthClass(PthClass pthClass);

    /**
     * 修改普通话班级
     * 
     * @param pthClass 普通话班级
     * @return 结果
     */
    public int updatePthClass(PthClass pthClass);

    /**
     * 更新班级学生数量
     * 
     * @param classId 班级ID
     * @param count 学生数量
     * @return 结果
     */
    public int updateStudentCount(@Param("classId") Long classId, @Param("count") Integer count);

    /**
     * 删除普通话班级
     * 
     * @param classId 普通话班级主键
     * @return 结果
     */
    public int deletePthClassByClassId(Long classId);

    /**
     * 批量删除普通话班级
     * 
     * @param classIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePthClassByClassIds(Long[] classIds);
}
