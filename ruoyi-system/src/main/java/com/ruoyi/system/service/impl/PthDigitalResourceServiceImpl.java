package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PthDigitalResourceMapper;
import com.ruoyi.system.domain.PthDigitalResource;
import com.ruoyi.system.service.IPthDigitalResourceService;

/**
 * 数字馆资源Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
@Service
public class PthDigitalResourceServiceImpl implements IPthDigitalResourceService 
{
    @Autowired
    private PthDigitalResourceMapper pthDigitalResourceMapper;

    /**
     * 查询数字馆资源
     * 
     * @param id 数字馆资源主键
     * @return 数字馆资源
     */
    @Override
    public PthDigitalResource selectPthDigitalResourceById(Long id)
    {
        return pthDigitalResourceMapper.selectPthDigitalResourceById(id);
    }

    /**
     * 查询数字馆资源列表
     * 
     * @param pthDigitalResource 数字馆资源
     * @return 数字馆资源
     */
    @Override
    public List<PthDigitalResource> selectPthDigitalResourceList(PthDigitalResource pthDigitalResource)
    {
        return pthDigitalResourceMapper.selectPthDigitalResourceList(pthDigitalResource);
    }

    /**
     * 新增数字馆资源
     * 
     * @param pthDigitalResource 数字馆资源
     * @return 结果
     */
    @Override
    public int insertPthDigitalResource(PthDigitalResource pthDigitalResource)
    {
        return pthDigitalResourceMapper.insertPthDigitalResource(pthDigitalResource);
    }

    /**
     * 修改数字馆资源
     * 
     * @param pthDigitalResource 数字馆资源
     * @return 结果
     */
    @Override
    public int updatePthDigitalResource(PthDigitalResource pthDigitalResource)
    {
        return pthDigitalResourceMapper.updatePthDigitalResource(pthDigitalResource);
    }

    /**
     * 批量删除数字馆资源
     * 
     * @param ids 需要删除的数字馆资源主键
     * @return 结果
     */
    @Override
    public int deletePthDigitalResourceByIds(Long[] ids)
    {
        return pthDigitalResourceMapper.deletePthDigitalResourceByIds(ids);
    }

    /**
     * 删除数字馆资源信息
     * 
     * @param id 数字馆资源主键
     * @return 结果
     */
    @Override
    public int deletePthDigitalResourceById(Long id)
    {
        return pthDigitalResourceMapper.deletePthDigitalResourceById(id);
    }
}