package com.ruoyi.pth.service;

import java.util.List;
import com.ruoyi.pth.domain.PthClass;

/**
 * 普通话班级Service接口
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public interface IPthClassService 
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
     * 批量删除普通话班级
     * 
     * @param classIds 需要删除的普通话班级主键集合
     * @return 结果
     */
    public int deletePthClassByClassIds(Long[] classIds);

    /**
     * 删除普通话班级信息
     * 
     * @param classId 普通话班级主键
     * @return 结果
     */
    public int deletePthClassByClassId(Long classId);

    /**
     * 生成唯一邀请码
     * 
     * @return 邀请码
     */
    public String generateInvitationCode();

    /**
     * 学生加入班级
     * 
     * @param invitationCode 邀请码
     * @param studentId 学生ID
     * @param studentName 学生姓名
     * @return 结果
     */
    public int joinClass(String invitationCode, Long studentId, String studentName);
}
