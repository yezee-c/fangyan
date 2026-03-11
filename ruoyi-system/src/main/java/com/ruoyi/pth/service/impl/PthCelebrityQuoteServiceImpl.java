package com.ruoyi.pth.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.pth.mapper.PthCelebrityQuoteMapper;
import com.ruoyi.pth.domain.PthCelebrityQuote;
import com.ruoyi.pth.service.IPthCelebrityQuoteService;

/**
 * 大别山名人名言Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
@Service
public class PthCelebrityQuoteServiceImpl implements IPthCelebrityQuoteService 
{
    @Autowired
    private PthCelebrityQuoteMapper pthCelebrityQuoteMapper;

    /**
     * 查询大别山名人名言
     * 
     * @param quoteId 大别山名人名言主键
     * @return 大别山名人名言
     */
    @Override
    public PthCelebrityQuote selectPthCelebrityQuoteByQuoteId(Long quoteId)
    {
        return pthCelebrityQuoteMapper.selectPthCelebrityQuoteByQuoteId(quoteId);
    }

    /**
     * 查询大别山名人名言列表
     * 
     * @param pthCelebrityQuote 大别山名人名言
     * @return 大别山名人名言
     */
    @Override
    public List<PthCelebrityQuote> selectPthCelebrityQuoteList(PthCelebrityQuote pthCelebrityQuote)
    {
        return pthCelebrityQuoteMapper.selectPthCelebrityQuoteList(pthCelebrityQuote);
    }

    /**
     * 查询前台展示的名人名言列表
     * 
     * @return 大别山名人名言集合
     */
    @Override
    public List<PthCelebrityQuote> selectDisplayQuoteList()
    {
        return pthCelebrityQuoteMapper.selectDisplayQuoteList();
    }

    /**
     * 新增大别山名人名言
     * 
     * @param pthCelebrityQuote 大别山名人名言
     * @return 结果
     */
    @Override
    public int insertPthCelebrityQuote(PthCelebrityQuote pthCelebrityQuote)
    {
        pthCelebrityQuote.setCreateTime(DateUtils.getNowDate());
        return pthCelebrityQuoteMapper.insertPthCelebrityQuote(pthCelebrityQuote);
    }

    /**
     * 修改大别山名人名言
     * 
     * @param pthCelebrityQuote 大别山名人名言
     * @return 结果
     */
    @Override
    public int updatePthCelebrityQuote(PthCelebrityQuote pthCelebrityQuote)
    {
        pthCelebrityQuote.setUpdateTime(DateUtils.getNowDate());
        return pthCelebrityQuoteMapper.updatePthCelebrityQuote(pthCelebrityQuote);
    }

    /**
     * 批量删除大别山名人名言
     * 
     * @param quoteIds 需要删除的大别山名人名言主键
     * @return 结果
     */
    @Override
    public int deletePthCelebrityQuoteByQuoteIds(Long[] quoteIds)
    {
        return pthCelebrityQuoteMapper.deletePthCelebrityQuoteByQuoteIds(quoteIds);
    }

    /**
     * 删除大别山名人名言信息
     * 
     * @param quoteId 大别山名人名言主键
     * @return 结果
     */
    @Override
    public int deletePthCelebrityQuoteByQuoteId(Long quoteId)
    {
        return pthCelebrityQuoteMapper.deletePthCelebrityQuoteByQuoteId(quoteId);
    }
}
