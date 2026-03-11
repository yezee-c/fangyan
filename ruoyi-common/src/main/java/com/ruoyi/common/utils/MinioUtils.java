package com.ruoyi.common.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * MinIO工具类
 * 
 * @author ruoyi
 */
@Component
public class MinioUtils
{
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 判断bucket是否存在
     *
     * @param bucketName 桶名
     * @return true存在，false不存在
     */
    public boolean bucketExists(String bucketName) throws Exception
    {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建bucket
     *
     * @param bucketName 桶名
     */
    public void makeBucket(String bucketName) throws Exception
    {
        boolean exists = bucketExists(bucketName);
        if (!exists)
        {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @param objectName 对象名
     * @return 文件路径
     */
    public String uploadFile(MultipartFile file, String objectName) throws Exception
    {
        return uploadFile(file, bucketName, objectName);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @param bucket 桶名
     * @param objectName 对象名
     * @return 文件路径
     */
    public String uploadFile(MultipartFile file, String bucket, String objectName) throws Exception
    {
        try (InputStream inputStream = file.getInputStream())
        {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return objectName;
        }
    }

    /**
     * 获取文件
     *
     * @param objectName 对象名
     * @return 文件流
     */
    public InputStream getObject(String objectName) throws Exception
    {
        return getObject(bucketName, objectName);
    }

    /**
     * 获取文件
     *
     * @param bucket 桶名
     * @param objectName 对象名
     * @return 文件流
     */
    public InputStream getObject(String bucket, String objectName) throws Exception
    {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名
     */
    public void removeObject(String objectName) throws Exception
    {
        removeObject(bucketName, objectName);
    }

    /**
     * 删除文件
     *
     * @param bucket 桶名
     * @param objectName 对象名
     */
    public void removeObject(String bucket, String objectName) throws Exception
    {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
    }

    /**
     * 批量删除文件
     *
     * @param objectNames 对象名列表
     */
    public List<String> removeObjects(List<String> objectNames) throws Exception
    {
        return removeObjects(bucketName, objectNames);
    }

    /**
     * 批量删除文件
     *
     * @param bucket 桶名
     * @param objectNames 对象名列表
     * @return 删除失败的文件列表
     */
    public List<String> removeObjects(String bucket, List<String> objectNames) throws Exception
    {
        List<DeleteObject> objects = new ArrayList<>();
        for (String objectName : objectNames)
        {
            objects.add(new DeleteObject(objectName));
        }
        
        List<String> deleteErrorNames = new ArrayList<>();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucket)
                        .objects(objects)
                        .build()
        );
        
        for (Result<DeleteError> result : results)
        {
            DeleteError error = result.get();
            deleteErrorNames.add(error.objectName());
        }
        return deleteErrorNames;
    }

    /**
     * 获取文件外链（有效期7天）
     *
     * @param objectName 对象名
     * @return 文件URL
     */
    public String getPresignedObjectUrl(String objectName) throws Exception
    {
        return getPresignedObjectUrl(bucketName, objectName, 7, TimeUnit.DAYS);
    }

    /**
     * 获取文件外链
     *
     * @param bucket 桶名
     * @param objectName 对象名
     * @param duration 有效时长
     * @param unit 时间单位
     * @return 文件URL
     */
    public String getPresignedObjectUrl(String bucket, String objectName, int duration, TimeUnit unit) throws Exception
    {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(objectName)
                        .expiry(duration, unit)
                        .build()
        );
    }

    /**
     * 获取文件下载链接（强制下载）
     *
     * @param bucket 桶名
     * @param objectName 对象名
     * @param fileName 下载文件名
     * @param duration 有效时长
     * @param unit 时间单位
     * @return 文件URL
     */
    public String getPresignedObjectUrl(String bucket, String objectName, String fileName, int duration, TimeUnit unit) throws Exception
    {
        // 设置响应头，强制浏览器下载而不是预览
        Map<String, String> reqParams = new HashMap<>();
        try {
            reqParams.put("response-content-disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        } catch (UnsupportedEncodingException e) {
            reqParams.put("response-content-disposition", "attachment; filename=\"" + fileName + "\"");
        }
        
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(objectName)
                        .expiry(duration, unit)
                        .extraQueryParams(reqParams)
                        .build()
        );
    }

    /**
     * 列出bucket中的文件
     *
     * @param bucket 桶名
     * @param prefix 前缀
     * @return 文件列表
     */
    public List<Item> listObjects(String bucket, String prefix) throws Exception
    {
        List<Item> items = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(prefix)
                        .build()
        );
        
        for (Result<Item> result : results)
        {
            items.add(result.get());
        }
        return items;
    }

    /**
     * 获取文件信息
     *
     * @param bucket 桶名
     * @param objectName 对象名
     * @return 文件信息
     */
    public StatObjectResponse getObjectInfo(String bucket, String objectName) throws Exception
    {
        return minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
    }
}
