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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.pth.domain.PthCelebrityQuote;
import com.ruoyi.pth.service.IPthCelebrityQuoteService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 大别山名人名言Controller
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
@RestController
@RequestMapping("/pth/quote")
public class PthCelebrityQuoteController extends BaseController
{
    @Autowired
    private IPthCelebrityQuoteService pthCelebrityQuoteService;

    /**
     * 查询大别山名人名言列表
     */
    @PreAuthorize("@ss.hasPermi('pth:quote:list')")
    @GetMapping("/list")
    public TableDataInfo list(PthCelebrityQuote pthCelebrityQuote)
    {
        startPage();
        List<PthCelebrityQuote> list = pthCelebrityQuoteService.selectPthCelebrityQuoteList(pthCelebrityQuote);
        return getDataTable(list);
    }

    /**
     * 查询前台展示的名人名言列表（无需权限）
     */
    @GetMapping("/display")
    public AjaxResult displayList()
    {
        List<PthCelebrityQuote> list = pthCelebrityQuoteService.selectDisplayQuoteList();
        return success(list);
    }

    /**
     * 导出大别山名人名言列表
     */
    @PreAuthorize("@ss.hasPermi('pth:quote:export')")
    @Log(title = "大别山名人名言", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PthCelebrityQuote pthCelebrityQuote)
    {
        List<PthCelebrityQuote> list = pthCelebrityQuoteService.selectPthCelebrityQuoteList(pthCelebrityQuote);
        ExcelUtil<PthCelebrityQuote> util = new ExcelUtil<PthCelebrityQuote>(PthCelebrityQuote.class);
        util.exportExcel(response, list, "大别山名人名言数据");
    }

    /**
     * 获取大别山名人名言详细信息
     */
    @PreAuthorize("@ss.hasPermi('pth:quote:query')")
    @GetMapping(value = "/{quoteId}")
    public AjaxResult getInfo(@PathVariable("quoteId") Long quoteId)
    {
        return success(pthCelebrityQuoteService.selectPthCelebrityQuoteByQuoteId(quoteId));
    }

    /**
     * 新增大别山名人名言
     */
    @PreAuthorize("@ss.hasPermi('pth:quote:add')")
    @Log(title = "大别山名人名言", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PthCelebrityQuote pthCelebrityQuote)
    {
        return toAjax(pthCelebrityQuoteService.insertPthCelebrityQuote(pthCelebrityQuote));
    }

    /**
     * 修改大别山名人名言
     */
    @PreAuthorize("@ss.hasPermi('pth:quote:edit')")
    @Log(title = "大别山名人名言", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PthCelebrityQuote pthCelebrityQuote)
    {
        return toAjax(pthCelebrityQuoteService.updatePthCelebrityQuote(pthCelebrityQuote));
    }

    /**
     * 删除大别山名人名言
     */
    @PreAuthorize("@ss.hasPermi('pth:quote:remove')")
    @Log(title = "大别山名人名言", businessType = BusinessType.DELETE)
	@DeleteMapping("/{quoteIds}")
    public AjaxResult remove(@PathVariable Long[] quoteIds)
    {
        return toAjax(pthCelebrityQuoteService.deletePthCelebrityQuoteByQuoteIds(quoteIds));
    }
}
