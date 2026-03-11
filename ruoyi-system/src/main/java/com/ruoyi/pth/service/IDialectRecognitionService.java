package com.ruoyi.pth.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 方言识别服务接口（FunASR）
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public interface IDialectRecognitionService 
{
    /**
     * 方言识别
     * 
     * TODO: 集成FunASR语音识别工具包
     * 
     * @param audioFile 音频文件
     * @param dialectType 方言类型
     * @return 识别结果
     */
    public RecognitionResult recognize(MultipartFile audioFile, String dialectType);

    /**
     * 方言转普通话
     * 
     * TODO: 实现方言文字转普通话文字的功能
     * 
     * @param dialectText 方言文字
     * @param dialectType 方言类型
     * @return 普通话文字
     */
    public String convertToMandarin(String dialectText, String dialectType);

    /**
     * 识别结果类
     */
    public static class RecognitionResult {
        /** 方言识别文字 */
        private String dialectText;
        
        /** 普通话对照文字 */
        private String mandarinText;
        
        /** 识别置信度 */
        private Double confidence;
        
        /** 音频时长（秒） */
        private Double duration;

        public RecognitionResult() {}

        public RecognitionResult(String dialectText, String mandarinText, Double confidence, Double duration) {
            this.dialectText = dialectText;
            this.mandarinText = mandarinText;
            this.confidence = confidence;
            this.duration = duration;
        }

        // Getters and Setters
        public String getDialectText() { return dialectText; }
        public void setDialectText(String dialectText) { this.dialectText = dialectText; }
        
        public String getMandarinText() { return mandarinText; }
        public void setMandarinText(String mandarinText) { this.mandarinText = mandarinText; }
        
        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }
        
        public Double getDuration() { return duration; }
        public void setDuration(Double duration) { this.duration = duration; }
    }
}
