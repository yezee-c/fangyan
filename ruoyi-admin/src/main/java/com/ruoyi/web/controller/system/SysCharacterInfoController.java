package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysCharacterInfo;
import com.ruoyi.system.domain.SysCharacterFile;
import com.ruoyi.system.service.ISysCharacterInfoService;
import com.ruoyi.system.service.ISysFileInfoService;

/**
 * 人物信息Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/character")
public class SysCharacterInfoController extends BaseController
{
    private static final Logger logger = LoggerFactory.getLogger(SysCharacterInfoController.class);

    @Autowired
    private ISysCharacterInfoService sysCharacterInfoService;
    
    @Autowired
    private ISysFileInfoService sysFileInfoService;

    /**
     * 查询人物信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:character:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCharacterInfo sysCharacterInfo)
    {
        startPage();
        List<SysCharacterInfo> list = sysCharacterInfoService.selectSysCharacterInfoList(sysCharacterInfo);
        return getDataTable(list);
    }

    /**
     * 获取人物信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:character:query')")
    @GetMapping(value = "/{characterId}")
    public AjaxResult getInfo(@PathVariable("characterId") Long characterId)
    {
        return success(sysCharacterInfoService.selectSysCharacterInfoByCharacterId(characterId));
    }

    /**
     * 新增人物信息
     */
    @PreAuthorize("@ss.hasPermi('system:character:add')")
    @Log(title = "人物信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCharacterInfo sysCharacterInfo)
    {
        return toAjax(sysCharacterInfoService.insertSysCharacterInfo(sysCharacterInfo));
    }

    /**
     * 修改人物信息
     */
    @PreAuthorize("@ss.hasPermi('system:character:edit')")
    @Log(title = "人物信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCharacterInfo sysCharacterInfo)
    {
        return toAjax(sysCharacterInfoService.updateSysCharacterInfo(sysCharacterInfo));
    }

    /**
     * 删除人物信息
     */
    @PreAuthorize("@ss.hasPermi('system:character:remove')")
    @Log(title = "人物信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{characterIds}")
    public AjaxResult remove(@PathVariable Long[] characterIds)
    {
        return toAjax(sysCharacterInfoService.deleteSysCharacterInfoByCharacterIds(characterIds));
    }

    /**
     * 为人物添加文件关联
     */
    @PreAuthorize("@ss.hasPermi('system:character:edit')")
    @Log(title = "人物文件关联", businessType = BusinessType.INSERT)
    @PostMapping("/files")
    public AjaxResult addFiles(@RequestBody Map<String, Object> params)
    {
        Long characterId = Long.valueOf(params.get("characterId").toString());

        List<?> rawList = (List<?>) params.get("fileIds");
        Long[] fileIds = rawList.stream()
                .filter(Objects::nonNull)
                .map(obj -> Long.valueOf(obj.toString()))
                .toArray(Long[]::new);

        String fileType = params.get("fileType").toString();
        String fileCategory = params.get("fileCategory") != null ? params.get("fileCategory").toString() : null;

        return toAjax(sysCharacterInfoService.addCharacterFiles(characterId, fileIds, fileType, fileCategory));
    }


    /**
     * 获取人物关联文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:character:query')")
    @GetMapping("/files/{characterId}")
    public AjaxResult getCharacterFiles(@PathVariable Long characterId)
    {
        try
        {
            List<SysCharacterFile> files = sysCharacterInfoService.getCharacterFiles(characterId);
            return success(files);
        }
        catch (Exception e)
        {
            logger.error("获取人物文件列表失败", e);
            return error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 直接上传文件并关联到人物
     */
    @PreAuthorize("@ss.hasPermi('system:character:edit')")
    @Log(title = "人物文件上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload/{characterId}")
    public AjaxResult uploadFile(
            @PathVariable Long characterId,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("fileType") String fileType,
            @RequestParam(value = "fileCategory", required = false) String fileCategory,
            @RequestParam(value = "description", required = false) String description)
    {
        try
        {
            // 先上传文件到MinIO
            List<Long> fileIds = new java.util.ArrayList<>();
            for (MultipartFile file : files) {
                Long fileId = sysFileInfoService.uploadPrivateFile(file);
                fileIds.add(fileId);
            }
            
            // 关联文件到人物
            Long[] fileIdArray = fileIds.toArray(new Long[0]);
            sysCharacterInfoService.addCharacterFiles(characterId, fileIdArray, fileType, fileCategory);
            
            // 返回更新后的文件列表
            List<SysCharacterFile> characterFiles = sysCharacterInfoService.getCharacterFiles(characterId);
            return success(characterFiles);
        }
        catch (Exception e)
        {
            logger.error("上传文件失败", e);
            return error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 删除人物文件关联
     */
    @PreAuthorize("@ss.hasPermi('system:character:edit')")
    @Log(title = "人物文件关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/files/{id}")
    public AjaxResult removeFile(@PathVariable Long id)
    {
        return toAjax(sysCharacterInfoService.deleteCharacterFile(id));
    }

    /**
     * 清空人物的所有文件关联
     */
    @PreAuthorize("@ss.hasPermi('system:character:edit')")
    @Log(title = "清空人物文件关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/files/clear/{characterId}")
    public AjaxResult clearCharacterFiles(@PathVariable Long characterId)
    {
        try
        {
            sysCharacterInfoService.clearCharacterFiles(characterId);
            return success();
        }
        catch (Exception e)
        {
            logger.error("清空人物文件关联失败", e);
            return error("清空失败：" + e.getMessage());
        }
    }

    /**
     * 获取公开人物画廊列表（无需权限）
     */
    @Anonymous
    @GetMapping("/gallery/list")
    public AjaxResult getGalleryList()
    {
        try
        {
            logger.info("请求获取人物画廊列表");
            List<SysCharacterInfo> list = sysCharacterInfoService.getPublicCharacterGallery();
            logger.info("获取到 {} 个人物", list.size());
            if (!list.isEmpty())
            {
                SysCharacterInfo first = list.get(0);
                logger.info("第一个人物: ID={}, 姓名={}, avatarUrl={}, coverUrl={}", 
                    first.getCharacterId(), first.getCharacterName(), first.getAvatarUrl(), first.getCoverUrl());
            }
            return success(list);
        }
        catch (Exception e)
        {
            logger.error("获取人物画廊列表失败", e);
            return error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取人物详情（无需权限）
     */
    @Anonymous
    @GetMapping("/gallery/detail/{characterId}")
    public AjaxResult getCharacterDetail(@PathVariable Long characterId)
    {
        try
        {
            SysCharacterInfo character = sysCharacterInfoService.getCharacterDetail(characterId);
            if (character == null)
            {
                return error("人物不存在");
            }
            return success(character);
        }
        catch (Exception e)
        {
            logger.error("获取人物详情失败", e);
            return error("获取失败：" + e.getMessage());
        }
    }
}
