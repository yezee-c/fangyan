package com.ruoyi.pth.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 普通话测评记录对象 pth_assessment_record
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public class PthAssessmentRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 学生ID */
    @Excel(name = "学生ID")
    private Long studentId;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 班级ID */
    @Excel(name = "班级ID")
    private Long classId;

    /** 测评类型 */
    @Excel(name = "测评类型", readConverterExp = "single_word=单字朗读,word=词语朗读,article=短文朗读,speech=命题说话")
    private String assessmentType;

    /** 测评标题 */
    @Excel(name = "测评标题")
    private String assessmentTitle;

    /** 测评内容 */
    private String content;

    /** 音频地址 */
    private String audioUrl;

    /** 总分 */
    @Excel(name = "总分")
    private BigDecimal totalScore;

    /** 语音标准度得分 */
    @Excel(name = "语音标准度得分")
    private BigDecimal pronunciationScore;

    /** 词汇语法得分 */
    @Excel(name = "词汇语法得分")
    private BigDecimal grammarScore;

    /** 流畅度得分 */
    @Excel(name = "流畅度得分")
    private BigDecimal fluencyScore;

    /** 薄弱环节 */
    private String weakPoints;

    /** 提升建议 */
    private String suggestions;

    /** 测评时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "测评时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date assessmentTime;

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }

    public void setStudentId(Long studentId) 
    {
        this.studentId = studentId;
    }

    public Long getStudentId() 
    {
        return studentId;
    }

    public void setStudentName(String studentName) 
    {
        this.studentName = studentName;
    }

    public String getStudentName() 
    {
        return studentName;
    }

    public void setClassId(Long classId) 
    {
        this.classId = classId;
    }

    public Long getClassId() 
    {
        return classId;
    }

    public void setAssessmentType(String assessmentType) 
    {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentType() 
    {
        return assessmentType;
    }

    public void setAssessmentTitle(String assessmentTitle) 
    {
        this.assessmentTitle = assessmentTitle;
    }

    public String getAssessmentTitle() 
    {
        return assessmentTitle;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setAudioUrl(String audioUrl) 
    {
        this.audioUrl = audioUrl;
    }

    public String getAudioUrl() 
    {
        return audioUrl;
    }

    public void setTotalScore(BigDecimal totalScore) 
    {
        this.totalScore = totalScore;
    }

    public BigDecimal getTotalScore() 
    {
        return totalScore;
    }

    public void setPronunciationScore(BigDecimal pronunciationScore) 
    {
        this.pronunciationScore = pronunciationScore;
    }

    public BigDecimal getPronunciationScore() 
    {
        return pronunciationScore;
    }

    public void setGrammarScore(BigDecimal grammarScore) 
    {
        this.grammarScore = grammarScore;
    }

    public BigDecimal getGrammarScore() 
    {
        return grammarScore;
    }

    public void setFluencyScore(BigDecimal fluencyScore) 
    {
        this.fluencyScore = fluencyScore;
    }

    public BigDecimal getFluencyScore() 
    {
        return fluencyScore;
    }

    public void setWeakPoints(String weakPoints) 
    {
        this.weakPoints = weakPoints;
    }

    public String getWeakPoints() 
    {
        return weakPoints;
    }

    public void setSuggestions(String suggestions) 
    {
        this.suggestions = suggestions;
    }

    public String getSuggestions() 
    {
        return suggestions;
    }

    public void setAssessmentTime(Date assessmentTime) 
    {
        this.assessmentTime = assessmentTime;
    }

    public Date getAssessmentTime() 
    {
        return assessmentTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("studentId", getStudentId())
            .append("studentName", getStudentName())
            .append("classId", getClassId())
            .append("assessmentType", getAssessmentType())
            .append("assessmentTitle", getAssessmentTitle())
            .append("content", getContent())
            .append("audioUrl", getAudioUrl())
            .append("totalScore", getTotalScore())
            .append("pronunciationScore", getPronunciationScore())
            .append("grammarScore", getGrammarScore())
            .append("fluencyScore", getFluencyScore())
            .append("weakPoints", getWeakPoints())
            .append("suggestions", getSuggestions())
            .append("assessmentTime", getAssessmentTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
