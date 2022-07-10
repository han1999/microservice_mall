package com.mall.order.dal.persistence;

import com.mall.commons.tool.tkmapper.TkMapper;
import com.mall.order.dal.entitys.Stock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 **/
public interface StockMapper extends TkMapper<Stock> {
 int updateStock(Stock stock);
 Stock selectStockForUpdate(Long itemId);
 Stock selectStock(Long itemId);

 List<Stock> findStocksForUpdate(@Param("itemIds")List<Long> itemIds);
}