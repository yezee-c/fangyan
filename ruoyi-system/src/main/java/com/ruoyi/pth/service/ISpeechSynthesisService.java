package com.ruoyi.pth.service;

/**
 * 语音合成服务接口（科大讯飞）
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public interface ISpeechSynthesisService 
{
    /**
     * 文本转语音
     * 
     * TODO: 集成科大讯飞在线语音合成API
     * 
     * @param text 要合成的文本
     * @param voiceType 音色类型（male男声、female女声、child童声等）
     * @param speed 语速（0-100，默认50）
     * @param pitch 音调（0-100，默认50）
     * @param volume 音量（0-100，默认50）
     * @return 合成结果
     */
    public SynthesisResult synthesize(String text, String voiceType, Integer speed, Integer pitch, Integer volume);

    /**
     * 文本转语音（使用默认参数）
     * 
     * @param text 要合成的文本
     * @return 合成结果
     */
    public SynthesisResult synthesize(String text);

    /**
     * 获取支持的音色列表
     * 
     * @return 音色列表
     */
    public VoiceType[] getSupportedVoiceTypes();

    /**
     * 合成结果类
     */
    public static class SynthesisResult {
        /** 音频文件URL */
        private String audioUrl;
        
        /** 音频文件路径 */
        private String audioPath;
        
        /** 音频时长（秒） */
        private Double duration;
        
        /** 文件大小（字节） */
        private Long fileSize;
        
        /** 合成状态 */
        private Boolean success;
        
        /** 错误信息 */
        private String errorMessage;

        public SynthesisResult() {}

        public SynthesisResult(String audioUrl, String audioPath, Double duration, Long fileSize) {
            this.audioUrl = audioUrl;
            this.audioPath = audioPath;
            this.duration = duration;
            this.fileSize = fileSize;
            this.success = true;
        }

        // Getters and Setters
        public String getAudioUrl() { return audioUrl; }
        public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
        
        public String getAudioPath() { return audioPath; }
        public void setAudioPath(String audioPath) { this.audioPath = audioPath; }
        
        public Double getDuration() { return duration; }
        public void setDuration(Double duration) { this.duration = duration; }
        
        public Long getFileSize() { return fileSize; }
        public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
        
        public Boolean getSuccess() { return success; }
        public void setSuccess(Boolean success) { this.success = success; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    /**
     * 音色类型枚举
     */
    public enum VoiceType {
        MALE_1("xiaoyan", "青年女声"),
        MALE_2("aisjiuxu", "许久"),
        FEMALE_1("aisxping", "小萍"),
        FEMALE_2("aisjinger", "靖儿"),
        CHILD_1("aisbabyxu", "许宝宝");

        private String code;
        private String name;

        VoiceType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() { return code; }
        public String getName() { return name; }
    }
}
