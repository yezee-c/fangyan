package com.ruoyi.pth.service;

import java.util.List;
import com.ruoyi.pth.domain.PthCelebrityQuote;

/**
 * 大别山名人名言Service接口
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public interface IPthCelebrityQuoteService 
{
    /**
     * 查询大别山名人名言
     * 
     * @param quoteId 大别山名人名言主键
     * @return 大别山名人名言
     */
    public PthCelebrityQuote selectPthCelebrityQuoteByQuoteId(Long quoteId);

    /**
     * 查询大别山名人名言列表
     * 
     * @param pthCelebrityQuote 大别山名人名言
     * @return 大别山名人名言集合
     */
    public List<PthCelebrityQuote> selectPthCelebrityQuoteList(PthCelebrityQuote pthCelebrityQuote);

    /**
     * 查询前台展示的名人名言列表
     * 
     * @return 大别山名人名言集合
     */
    public List<PthCelebrityQuote> selectDisplayQuoteList();

    /**
     * 新增大别山名人名言
     * 
     * @param pthCelebrityQuote 大别山名人名言
     * @return 结果
     */
    public int insertPthCelebrityQuote(PthCelebrityQuote pthCelebrityQuote);

    /**
     * 修改大别山名人名言
     * 
     * @param pthCelebrityQuote 大别山名人名言
     * @return 结果
     */
    public int updatePthCelebrityQuote(PthCelebrityQuote pthCelebrityQuote);

    /**
     * 批量删除大别山名人名言
     * 
     * @param quoteIds 需要删除的大别山名人名言主键集合
     * @return 结果
     */
    public int deletePthCelebrityQuoteByQuoteIds(Long[] quoteIds);

    /**
     * 删除大别山名人名言信息
     * 
     * @param quoteId 大别山名人名言主键
     * @return 结果
     */
    public int deletePthCelebrityQuoteByQuoteId(Long quoteId);
}
