package com.ruoyi.web.controller.pth;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import com.ruoyi.system.domain.PthDigitalResource;
import com.ruoyi.system.service.IPthDigitalResourceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数字馆资源Controller
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
@RestController
@RequestMapping("/pth/digital/resource")
public class PthDigitalResourceController extends BaseController
{
    @Autowired
    private IPthDigitalResourceService pthDigitalResourceService;

    /**
     * 查询数字馆资源列表
     */
    @PreAuthorize("@ss.hasPermi('pth:digital:list')")
    @GetMapping("/list")
    public TableDataInfo list(PthDigitalResource pthDigitalResource)
    {
        startPage();
        List<PthDigitalResource> list = pthDigitalResourceService.selectPthDigitalResourceList(pthDigitalResource);
        return getDataTable(list);
    }

    /**
     * 导出数字馆资源列表
     */
    @PreAuthorize("@ss.hasPermi('pth:digital:export')")
    @Log(title = "数字馆资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PthDigitalResource pthDigitalResource)
    {
        List<PthDigitalResource> list = pthDigitalResourceService.selectPthDigitalResourceList(pthDigitalResource);
        ExcelUtil<PthDigitalResource> util = new ExcelUtil<PthDigitalResource>(PthDigitalResource.class);
        util.exportExcel(response, list, "数字馆资源数据");
    }

    /**
     * 获取数字馆资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('pth:digital:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pthDigitalResourceService.selectPthDigitalResourceById(id));
    }

    /**
     * 新增数字馆资源
     */
    @PreAuthorize("@ss.hasPermi('pth:digital:add')")
    @Log(title = "数字馆资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PthDigitalResource pthDigitalResource)
    {
        return toAjax(pthDigitalResourceService.insertPthDigitalResource(pthDigitalResource));
    }

    /**
     * 修改数字馆资源
     */
    @PreAuthorize("@ss.hasPermi('pth:digital:edit')")
    @Log(title = "数字馆资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PthDigitalResource pthDigitalResource)
    {
        return toAjax(pthDigitalResourceService.updatePthDigitalResource(pthDigitalResource));
    }

    /**
     * 删除数字馆资源
     */
    @PreAuthorize("@ss.hasPermi('pth:digital:remove')")
    @Log(title = "数字馆资源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pthDigitalResourceService.deletePthDigitalResourceByIds(ids));
    }
}