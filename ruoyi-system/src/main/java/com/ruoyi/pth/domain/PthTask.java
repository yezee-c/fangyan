package com.ruoyi.pth.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务对象 pth_task
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public class PthTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long taskId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 任务类型 */
    @Excel(name = "任务类型", readConverterExp = "assessment=测评任务,dialect=方言识别,game=游戏任务,custom=自定义")
    private String taskType;

    /** 任务描述 */
    private String taskDesc;

    /** 任务内容 */
    private String taskContent;

    /** 发布教师ID */
    private Long teacherId;

    /** 教师姓名 */
    @Excel(name = "教师姓名")
    private String teacherName;

    /** 班级ID列表 */
    private String classIds;

    /** 截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "截止时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    /** 完成要求 */
    private String requirements;

    /** 评分标准 */
    private String scoringCriteria;

    /** 总学生数 */
    @Excel(name = "总学生数")
    private Integer totalStudents;

    /** 已完成学生数 */
    @Excel(name = "已完成学生数")
    private Integer completedStudents;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=进行中,1=已结束,2=已撤回")
    private String status;

    /** 删除标志 */
    private String delFlag;

    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
    }

    public void setTaskName(String taskName) 
    {
        this.taskName = taskName;
    }

    public String getTaskName() 
    {
        return taskName;
    }

    public void setTaskType(String taskType) 
    {
        this.taskType = taskType;
    }

    public String getTaskType() 
    {
        return taskType;
    }

    public void setTaskDesc(String taskDesc) 
    {
        this.taskDesc = taskDesc;
    }

    public String getTaskDesc() 
    {
        return taskDesc;
    }

    public void setTaskContent(String taskContent) 
    {
        this.taskContent = taskContent;
    }

    public String getTaskContent() 
    {
        return taskContent;
    }

    public void setTeacherId(Long teacherId) 
    {
        this.teacherId = teacherId;
    }

    public Long getTeacherId() 
    {
        return teacherId;
    }

    public void setTeacherName(String teacherName) 
    {
        this.teacherName = teacherName;
    }

    public String getTeacherName() 
    {
        return teacherName;
    }

    public void setClassIds(String classIds) 
    {
        this.classIds = classIds;
    }

    public String getClassIds() 
    {
        return classIds;
    }

    public void setDeadline(Date deadline) 
    {
        this.deadline = deadline;
    }

    public Date getDeadline() 
    {
        return deadline;
    }

    public void setRequirements(String requirements) 
    {
        this.requirements = requirements;
    }

    public String getRequirements() 
    {
        return requirements;
    }

    public void setScoringCriteria(String scoringCriteria) 
    {
        this.scoringCriteria = scoringCriteria;
    }

    public String getScoringCriteria() 
    {
        return scoringCriteria;
    }

    public void setTotalStudents(Integer totalStudents) 
    {
        this.totalStudents = totalStudents;
    }

    public Integer getTotalStudents() 
    {
        return totalStudents;
    }

    public void setCompletedStudents(Integer completedStudents) 
    {
        this.completedStudents = completedStudents;
    }

    public Integer getCompletedStudents() 
    {
        return completedStudents;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("taskId", getTaskId())
            .append("taskName", getTaskName())
            .append("taskType", getTaskType())
            .append("taskDesc", getTaskDesc())
            .append("taskContent", getTaskContent())
            .append("teacherId", getTeacherId())
            .append("teacherName", getTeacherName())
            .append("classIds", getClassIds())
            .append("deadline", getDeadline())
            .append("requirements", getRequirements())
            .append("scoringCriteria", getScoringCriteria())
            .append("totalStudents", getTotalStudents())
            .append("completedStudents", getCompletedStudents())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
