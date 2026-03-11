package com.ruoyi.web.controller.demo;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 测试演示控制器
 * 
 * @author ruoyi
 */
@Api(tags = "测试演示")
@RestController
@RequestMapping("/demo/test")
public class TestDemoController extends BaseController
{
    // 为了简化示例，使用内存Map存储数据
    private static final Map<Long, TestDemo> testDemoMap = new HashMap<>();
    private static long nextId = 1;

    /**
     * 查询测试数据列表
     */
    @ApiOperation("查询测试数据列表")
    @PreAuthorize("@ss.hasPermi('demo:test:list')")
    @GetMapping("/list")
    public TableDataInfo list(TestDemo testDemo)
    {
        startPage();
        List<TestDemo> list = getAllTestDemos(testDemo);
        return getDataTable(list);
    }

    /**
     * 获取测试数据详细信息
     */
    @ApiOperation("获取测试数据详细信息")
    @PreAuthorize("@ss.hasPermi('demo:test:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(testDemoMap.get(id));
    }

    /**
     * 新增测试数据
     */
    @ApiOperation("新增测试数据")
    @PreAuthorize("@ss.hasPermi('demo:test:add')")
    @Log(title = "测试数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TestDemo testDemo)
    {
        testDemo.setId(nextId++);
        testDemo.setCreateTime(new java.util.Date());
        testDemoMap.put(testDemo.getId(), testDemo);
        return AjaxResult.success();
    }

    /**
     * 修改测试数据
     */
    @ApiOperation("修改测试数据")
    @PreAuthorize("@ss.hasPermi('demo:test:edit')")
    @Log(title = "测试数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TestDemo testDemo)
    {
        if (testDemoMap.containsKey(testDemo.getId())) {
            testDemoMap.put(testDemo.getId(), testDemo);
            return AjaxResult.success();
        }
        return AjaxResult.error("测试数据不存在");
    }

    /**
     * 删除测试数据
     */
    @ApiOperation("删除测试数据")
    @PreAuthorize("@ss.hasPermi('demo:test:remove')")
    @Log(title = "测试数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        if (testDemoMap.containsKey(id)) {
            testDemoMap.remove(id);
            return AjaxResult.success();
        }
        return AjaxResult.error("测试数据不存在");
    }

    // 辅助方法：获取所有测试数据并支持过滤
    private List<TestDemo> getAllTestDemos(TestDemo query) {
        List<TestDemo> result = new java.util.ArrayList<>(testDemoMap.values());
        
        // 根据查询条件过滤
        if (query != null) {
            if (query.getName() != null && !query.getName().isEmpty()) {
                result.removeIf(item -> !item.getName().contains(query.getName()));
            }
            if (query.getDescription() != null && !query.getDescription().isEmpty()) {
                result.removeIf(item -> !item.getDescription().contains(query.getDescription()));
            }
        }
        
        // 按ID排序
        result.sort((a, b) -> a.getId().compareTo(b.getId()));
        return result;
    }

    // 内部静态类：测试数据实体
    public static class TestDemo {
        private Long id;
        private String name;
        private String description;
        private java.util.Date createTime;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public java.util.Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.util.Date createTime) {
            this.createTime = createTime;
        }
    }
}