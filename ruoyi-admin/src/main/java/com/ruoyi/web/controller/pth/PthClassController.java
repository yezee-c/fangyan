package com.ruoyi.web.controller.pth;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.pth.domain.PthClass;
import com.ruoyi.pth.service.IPthClassService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 普通话班级Controller
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
@RestController
@RequestMapping("/pth/class")
public class PthClassController extends BaseController
{
    @Autowired
    private IPthClassService pthClassService;

    /**
     * 查询普通话班级列表
     */
    @PreAuthorize("@ss.hasPermi('pth:class:list')")
    @GetMapping("/list")
    public TableDataInfo list(PthClass pthClass)
    {
        startPage();
        List<PthClass> list = pthClassService.selectPthClassList(pthClass);
        return getDataTable(list);
    }

    /**
     * 查询教师的班级列表
     */
    @PreAuthorize("@ss.hasPermi('pth:class:list')")
    @GetMapping("/myList")
    public TableDataInfo myList()
    {
        Long teacherId = SecurityUtils.getUserId();
        List<PthClass> list = pthClassService.selectPthClassListByTeacherId(teacherId);
        return getDataTable(list);
    }

    /**
     * 根据邀请码查询班级信息（学生端）
     */
    @GetMapping("/findByCode/{invitationCode}")
    public AjaxResult findByInvitationCode(@PathVariable String invitationCode)
    {
        PthClass pthClass = pthClassService.selectPthClassByInvitationCode(invitationCode);
        return success(pthClass);
    }

    /**
     * 导出普通话班级列表
     */
    @PreAuthorize("@ss.hasPermi('pth:class:export')")
    @Log(title = "普通话班级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PthClass pthClass)
    {
        List<PthClass> list = pthClassService.selectPthClassList(pthClass);
        ExcelUtil<PthClass> util = new ExcelUtil<PthClass>(PthClass.class);
        util.exportExcel(response, list, "普通话班级数据");
    }

    /**
     * 获取普通话班级详细信息
     */
    @PreAuthorize("@ss.hasPermi('pth:class:query')")
    @GetMapping(value = "/{classId}")
    public AjaxResult getInfo(@PathVariable("classId") Long classId)
    {
        return success(pthClassService.selectPthClassByClassId(classId));
    }

    /**
     * 新增普通话班级
     */
    @PreAuthorize("@ss.hasPermi('pth:class:add')")
    @Log(title = "普通话班级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PthClass pthClass)
    {
        // 设置教师信息
        pthClass.setTeacherId(SecurityUtils.getUserId());
        pthClass.setTeacherName(SecurityUtils.getUsername());
        
        return toAjax(pthClassService.insertPthClass(pthClass));
    }

    /**
     * 修改普通话班级
     */
    @PreAuthorize("@ss.hasPermi('pth:class:edit')")
    @Log(title = "普通话班级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PthClass pthClass)
    {
        return toAjax(pthClassService.updatePthClass(pthClass));
    }

    /**
     * 删除普通话班级
     */
    @PreAuthorize("@ss.hasPermi('pth:class:remove')")
    @Log(title = "普通话班级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{classIds}")
    public AjaxResult remove(@PathVariable Long[] classIds)
    {
        return toAjax(pthClassService.deletePthClassByClassIds(classIds));
    }

    /**
     * 生成邀请码
     */
    @PreAuthorize("@ss.hasPermi('pth:class:add')")
    @GetMapping("/generateCode")
    public AjaxResult generateCode()
    {
        String code = pthClassService.generateInvitationCode();
        return success(code);
    }

    /**
     * 学生加入班级
     */
    @PreAuthorize("@ss.hasPermi('pth:class:student')")
    @Log(title = "学生加入班级", businessType = BusinessType.INSERT)
    @PostMapping("/join")
    public AjaxResult joinClass(@RequestParam String invitationCode)
    {
        Long studentId = SecurityUtils.getUserId();
        String studentName = SecurityUtils.getUsername();
        
        return toAjax(pthClassService.joinClass(invitationCode, studentId, studentName));
    }
}
