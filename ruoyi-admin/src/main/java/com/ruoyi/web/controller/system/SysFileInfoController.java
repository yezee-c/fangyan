package com.ruoyi.web.controller.system;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysFileInfo;
import com.ruoyi.system.service.ISysFileInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件信息Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/file")
public class SysFileInfoController extends BaseController
{
    @Autowired
    private ISysFileInfoService sysFileInfoService;

    /**
     * 查询文件信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysFileInfo sysFileInfo)
    {
        startPage();
        List<SysFileInfo> list = sysFileInfoService.selectSysFileInfoList(sysFileInfo);
        return getDataTable(list);
    }

    /**
     * 查询我的私有文件列表
     */
    @GetMapping("/myPrivateList")
    public TableDataInfo myPrivateList(SysFileInfo sysFileInfo)
    {
        startPage();
        sysFileInfo.setUserId(SecurityUtils.getUserId());
        sysFileInfo.setIsPublic("0");
        List<SysFileInfo> list = sysFileInfoService.selectSysFileInfoList(sysFileInfo);
        return getDataTable(list);
    }

    /**
     * 查询公共文件列表
     */
    @GetMapping("/publicList")
    public TableDataInfo publicList(SysFileInfo sysFileInfo)
    {
        startPage();
        sysFileInfo.setIsPublic("1");
        List<SysFileInfo> list = sysFileInfoService.selectSysFileInfoList(sysFileInfo);
        return getDataTable(list);
    }

    /**
     * 导出文件信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:export')")
    @Log(title = "文件信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysFileInfo sysFileInfo)
    {
        List<SysFileInfo> list = sysFileInfoService.selectSysFileInfoList(sysFileInfo);
        ExcelUtil<SysFileInfo> util = new ExcelUtil<SysFileInfo>(SysFileInfo.class);
        util.exportExcel(response, list, "文件信息数据");
    }

    /**
     * 获取文件信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return success(sysFileInfoService.selectSysFileInfoByFileId(fileId));
    }

    /**
     * 上传文件
     */
    @Log(title = "文件上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("files") MultipartFile[] files, 
                            @RequestParam(value = "isPublic", defaultValue = "0") String isPublic)
    {
        try
        {
            logger.info("开始上传文件，文件数量：{}，是否公开：{}", files.length, isPublic);
            for (MultipartFile file : files) {
                logger.info("文件名：{}，大小：{} bytes", file.getOriginalFilename(), file.getSize());
            }
            
            List<SysFileInfo> fileInfoList = sysFileInfoService.uploadFiles(files, isPublic);
            logger.info("文件上传成功，成功数量：{}", fileInfoList.size());
            
            AjaxResult ajax = AjaxResult.success("上传成功");
            ajax.put("data", fileInfoList);
            return ajax;
        }
        catch (Exception e)
        {
            logger.error("文件上传失败", e);
            return error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 修改文件信息
     */
    @PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "文件信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFileInfo sysFileInfo)
    {
        return toAjax(sysFileInfoService.updateSysFileInfo(sysFileInfo));
    }

    /**
     * 删除文件信息
     */
    @Log(title = "文件删除", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(sysFileInfoService.deleteSysFileInfoByFileIds(fileIds));
    }

    /**
     * 下载文件
     */
    @Log(title = "文件下载", businessType = BusinessType.OTHER)
    @GetMapping("/download/{fileId}")
    public void download(@PathVariable Long fileId, HttpServletResponse response)
    {
        try
        {
            SysFileInfo fileInfo = sysFileInfoService.downloadFile(fileId);
            
            // 设置响应头
            response.setContentType(fileInfo.getContentType());
            response.setHeader("Content-Disposition", 
                "attachment; filename=" + URLEncoder.encode(fileInfo.getOriginalName(), "UTF-8"));
            
            // 通过重定向到预签名URL下载文件
            response.sendRedirect(fileInfo.getFileUrl());
        }
        catch (Exception e)
        {
            logger.error("下载文件失败", e);
        }
    }

    /**
     * 获取文件下载URL
     */
    @GetMapping("/downloadUrl/{fileId}")
    public AjaxResult getDownloadUrl(@PathVariable Long fileId)
    {
        try
        {
            SysFileInfo fileInfo = sysFileInfoService.downloadFile(fileId);
            return success(fileInfo);
        }
        catch (Exception e)
        {
            return error("获取下载链接失败：" + e.getMessage());
        }
    }

    /**
     * 获取公共文件展示列表（无需权限）
     */
    @Anonymous
    @GetMapping("/gallery/list")
    public AjaxResult getGalleryList(@RequestParam(value = "fileType", required = false) String fileType)
    {
        try
        {
            List<SysFileInfo> fileList = sysFileInfoService.getPublicGalleryList(fileType);
            return success(fileList);
        }
        catch (Exception e)
        {
            logger.error("获取公共文件展示列表失败", e);
            return error("获取失败：" + e.getMessage());
        }
    }
}
