package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysUserFileConfig;
import com.ruoyi.system.service.ISysUserFileConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户文件配置Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/fileconfig")
public class SysUserFileConfigController extends BaseController
{
    @Autowired
    private ISysUserFileConfigService sysUserFileConfigService;

    /**
     * 查询用户文件配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:fileconfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUserFileConfig sysUserFileConfig)
    {
        startPage();
        List<SysUserFileConfig> list = sysUserFileConfigService.selectSysUserFileConfigList(sysUserFileConfig);
        return getDataTable(list);
    }

    /**
     * 导出用户文件配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:fileconfig:export')")
    @Log(title = "用户文件配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUserFileConfig sysUserFileConfig)
    {
        List<SysUserFileConfig> list = sysUserFileConfigService.selectSysUserFileConfigList(sysUserFileConfig);
        ExcelUtil<SysUserFileConfig> util = new ExcelUtil<SysUserFileConfig>(SysUserFileConfig.class);
        util.exportExcel(response, list, "用户文件配置数据");
    }

    /**
     * 获取用户文件配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:fileconfig:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(sysUserFileConfigService.selectSysUserFileConfigByUserId(userId));
    }

    /**
     * 新增用户文件配置
     */
    @PreAuthorize("@ss.hasPermi('system:fileconfig:add')")
    @Log(title = "用户文件配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUserFileConfig sysUserFileConfig)
    {
        return toAjax(sysUserFileConfigService.insertSysUserFileConfig(sysUserFileConfig));
    }

    /**
     * 修改用户文件配置
     */
    @PreAuthorize("@ss.hasPermi('system:fileconfig:edit')")
    @Log(title = "用户文件配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUserFileConfig sysUserFileConfig)
    {
        return toAjax(sysUserFileConfigService.updateSysUserFileConfig(sysUserFileConfig));
    }

    /**
     * 删除用户文件配置
     */
    @PreAuthorize("@ss.hasPermi('system:fileconfig:remove')")
    @Log(title = "用户文件配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(sysUserFileConfigService.deleteSysUserFileConfigByUserIds(userIds));
    }
}
