package com.mall.order.biz.handler;

import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.dal.entitys.Stock;
import com.mall.order.dal.persistence.StockMapper;
import com.mall.order.dto.CartProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 扣减库存处理器
 * @Author： wz
 * @Date: 2019-09-16 00:03
 **/
@Component
@Slf4j
public class SubStockHandler extends AbstractTransHandler {

    @Autowired
    private StockMapper stockMapper;

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@Transactional
	public boolean handle(TransHandlerContext context) {
		CreateOrderContext createOrderContext = (CreateOrderContext) context;
		List<CartProductDto> cartProductDtoList = createOrderContext.getCartProductDtoList();

//		List<Long> buyProductIds = createOrderContext.getBuyProductIds();
//		/**
//		 * 以下是废话，因为每次新创建一个context的时候,getBuyProductIds返回都是null
//		 */
//		if (CollectionUtils.isEmpty(buyProductIds)) {
//			for (CartProductDto cartProductDto : cartProductDtoList) {
//				buyProductIds.add(cartProductDto.getProductId());
//			}
//		}
		List<Long> buyProductIds = new ArrayList<>();
		for (CartProductDto cartProductDto : cartProductDtoList) {
			buyProductIds.add(cartProductDto.getProductId());
		}
		buyProductIds.sort(Long::compareTo);

		List<Stock> stocksForUpdate = new ArrayList<>();
		stocksForUpdate = stockMapper.findStocksForUpdate(buyProductIds);
//		if (CollectionUtils.isEmpty(stocksForUpdate)) {
//			throw new BizException(OrderRetCode.DB_EXCEPTION.getCode(), "库存没有初始化");
//		}
		if (stocksForUpdate.size() != buyProductIds.size()) {
//			throw new BizException(OrderRetCode.DB_EXCEPTION.getCode(), "部分商品库存没有初始化");
			/**
			 * 暂时先不让他报错，对于没有初始化的商品， 直接在这里修改让它初始化, 最后下面这段代码，将注释掉
			 */
			List<Long> stocksForUpdateIds = new ArrayList<>();
			for (Stock stock : stocksForUpdate) {
				stocksForUpdateIds.add(stock.getItemId());
			}
			for (Long buyProductId : buyProductIds) {
				if (!stocksForUpdateIds.contains(buyProductId)) {
					Stock stock = new Stock();
					stock.setItemId(buyProductId);
					stock.setStockCount(10000L);
					stock.setLockCount(0);
					stock.setRestrictCount(5);
					int insert = stockMapper.insert(stock);
					if (insert == 1) {
						log.debug("tb_stock中插入成功, itemId={}", stock.getItemId());
					}
				}
			}
		}

		for (CartProductDto cartProductDto : cartProductDtoList) {
			Long productId = cartProductDto.getProductId();
			Long productNum = cartProductDto.getProductNum();
			Stock stock = new Stock();
			stock.setItemId(productId);
			stock.setLockCount(productNum.intValue());
			stock.setStockCount(-productNum);
			stockMapper.updateStock(stock);
		}
		createOrderContext.setBuyProductIds(buyProductIds);
		return true;
	}
}