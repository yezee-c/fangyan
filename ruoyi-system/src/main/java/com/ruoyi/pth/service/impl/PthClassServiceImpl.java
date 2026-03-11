package com.ruoyi.pth.service.impl;

import java.util.List;
import java.util.UUID;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.pth.mapper.PthClassMapper;
import com.ruoyi.pth.domain.PthClass;
import com.ruoyi.pth.service.IPthClassService;

/**
 * 普通话班级Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
@Service
public class PthClassServiceImpl implements IPthClassService 
{
    @Autowired
    private PthClassMapper pthClassMapper;

    /**
     * 查询普通话班级
     * 
     * @param classId 普通话班级主键
     * @return 普通话班级
     */
    @Override
    public PthClass selectPthClassByClassId(Long classId)
    {
        return pthClassMapper.selectPthClassByClassId(classId);
    }

    /**
     * 查询普通话班级列表
     * 
     * @param pthClass 普通话班级
     * @return 普通话班级
     */
    @Override
    public List<PthClass> selectPthClassList(PthClass pthClass)
    {
        return pthClassMapper.selectPthClassList(pthClass);
    }

    /**
     * 根据教师ID查询班级列表
     * 
     * @param teacherId 教师ID
     * @return 普通话班级集合
     */
    @Override
    public List<PthClass> selectPthClassListByTeacherId(Long teacherId)
    {
        return pthClassMapper.selectPthClassListByTeacherId(teacherId);
    }

    /**
     * 根据邀请码查询班级
     * 
     * @param invitationCode 邀请码
     * @return 普通话班级
     */
    @Override
    public PthClass selectPthClassByInvitationCode(String invitationCode)
    {
        return pthClassMapper.selectPthClassByInvitationCode(invitationCode);
    }

    /**
     * 新增普通话班级
     * 
     * @param pthClass 普通话班级
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPthClass(PthClass pthClass)
    {
        // 生成唯一邀请码
        if (pthClass.getInvitationCode() == null || pthClass.getInvitationCode().isEmpty()) {
            pthClass.setInvitationCode(generateInvitationCode());
        }
        
        // 初始化学生数量为0
        pthClass.setStudentCount(0);
        pthClass.setCreateTime(DateUtils.getNowDate());
        
        return pthClassMapper.insertPthClass(pthClass);
    }

    /**
     * 修改普通话班级
     * 
     * @param pthClass 普通话班级
     * @return 结果
     */
    @Override
    public int updatePthClass(PthClass pthClass)
    {
        pthClass.setUpdateTime(DateUtils.getNowDate());
        return pthClassMapper.updatePthClass(pthClass);
    }

    /**
     * 批量删除普通话班级
     * 
     * @param classIds 需要删除的普通话班级主键
     * @return 结果
     */
    @Override
    public int deletePthClassByClassIds(Long[] classIds)
    {
        return pthClassMapper.deletePthClassByClassIds(classIds);
    }

    /**
     * 删除普通话班级信息
     * 
     * @param classId 普通话班级主键
     * @return 结果
     */
    @Override
    public int deletePthClassByClassId(Long classId)
    {
        return pthClassMapper.deletePthClassByClassId(classId);
    }

    /**
     * 生成唯一邀请码
     * 
     * @return 邀请码
     */
    @Override
    public String generateInvitationCode()
    {
        String code;
        int attempts = 0;
        int maxAttempts = 10;
        
        do {
            // 生成6位随机邀请码（大写字母+数字）
            code = generateRandomCode(6);
            attempts++;
            
            if (attempts >= maxAttempts) {
                throw new ServiceException("生成邀请码失败，请重试");
            }
        } while (pthClassMapper.checkInvitationCodeUnique(code) > 0);
        
        return code;
    }

    /**
     * 生成随机码
     * 
     * @param length 长度
     * @return 随机码
     */
    private String generateRandomCode(int length)
    {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 去除易混淆字符
        StringBuilder code = new StringBuilder();
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return code.toString();
    }

    /**
     * 学生加入班级
     * 
     * @param invitationCode 邀请码
     * @param studentId 学生ID
     * @param studentName 学生姓名
     * @return 结果
     */
    @Override
    @Transactional
    public int joinClass(String invitationCode, Long studentId, String studentName)
    {
        // 查询班级是否存在
        PthClass pthClass = pthClassMapper.selectPthClassByInvitationCode(invitationCode);
        if (pthClass == null) {
            throw new ServiceException("邀请码不存在或已失效");
        }
        
        if (!"0".equals(pthClass.getStatus())) {
            throw new ServiceException("该班级已停用");
        }
        
        // TODO: 检查学生是否已加入其他班级（需要班级学生关联表）
        // TODO: 添加学生到班级（需要班级学生关联表）
        // TODO: 更新班级学生数量
        
        return 1;
    }
}
