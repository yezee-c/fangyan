package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PthDigitalResource;

/**
 * 数字馆资源Service接口
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
public interface IPthDigitalResourceService 
{
    /**
     * 查询数字馆资源
     * 
     * @param id 数字馆资源主键
     * @return 数字馆资源
     */
    public PthDigitalResource selectPthDigitalResourceById(Long id);

    /**
     * 查询数字馆资源列表
     * 
     * @param pthDigitalResource 数字馆资源
     * @return 数字馆资源集合
     */
    public List<PthDigitalResource> selectPthDigitalResourceList(PthDigitalResource pthDigitalResource);

    /**
     * 新增数字馆资源
     * 
     * @param pthDigitalResource 数字馆资源
     * @return 结果
     */
    public int insertPthDigitalResource(PthDigitalResource pthDigitalResource);

    /**
     * 修改数字馆资源
     * 
     * @param pthDigitalResource 数字馆资源
     * @return 结果
     */
    public int updatePthDigitalResource(PthDigitalResource pthDigitalResource);

    /**
     * 批量删除数字馆资源
     * 
     * @param ids 需要删除的数字馆资源主键集合
     * @return 结果
     */
    public int deletePthDigitalResourceByIds(Long[] ids);

    /**
     * 删除数字馆资源信息
     * 
     * @param id 数字馆资源主键
     * @return 结果
     */
    public int deletePthDigitalResourceById(Long id);
}