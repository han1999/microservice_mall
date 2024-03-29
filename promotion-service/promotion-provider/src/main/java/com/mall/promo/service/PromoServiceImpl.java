package com.mall.promo.service;

import com.mall.commons.lock.DistributedLockException;
import com.mall.order.OrderPromoService;
import com.mall.order.dto.CreateSeckillOrderRequest;
import com.mall.order.dto.CreateSeckillOrderResponse;
import com.mall.promo.PromoService;
import com.mall.promo.cache.CacheManager;
import com.mall.promo.constant.PromoRetCode;
import com.mall.promo.converter.PromoInfoConverter;
import com.mall.promo.converter.PromoProductConverter;
import com.mall.promo.dal.entitys.PromoItem;
import com.mall.promo.dal.entitys.PromoSession;
import com.mall.promo.dal.persistence.PromoItemMapper;
import com.mall.promo.dal.persistence.PromoSessionMapper;
import com.mall.promo.dto.*;
import com.mall.promo.mq.MQTransactionProducer;
import com.mall.shopping.IProductService;
import com.mall.shopping.dto.ProductDetailDto;
import com.mall.shopping.dto.ProductDetailRequest;
import com.mall.shopping.dto.ProductDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description
 **/
@Service
@Slf4j
public class PromoServiceImpl implements PromoService, ApplicationContextAware {

    @Autowired
    PromoSessionMapper sessionMapper;

    @Autowired
    PromoItemMapper promoItemMapper;

    @Reference(retries = 0, timeout = 3000, check = false)
    IProductService productService;

    @Autowired
    PromoInfoConverter promoInfoConverter;

    @Reference(retries = 0, timeout = 3000, check = false)
    OrderPromoService orderPromoService;

    @Autowired
    PromoProductConverter promoProductConverter;

    @Autowired
    MQTransactionProducer mqTransactionProducer;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    RedissonClient redissonClient;

    ApplicationContext context;


    @Override
    public PromoInfoResponse getPromoList(PromoInfoRequest request) {
        PromoInfoResponse response = new PromoInfoResponse();
        try {
            request.requestCheck();

            log.info("start getPromoList sessionId = " + request.getSessionId() + ", yyyymmdd " + request.getYyyymmdd());
            //查询场次信息
            Example promoSessionExample = new Example(PromoSession.class);
            promoSessionExample.createCriteria()
                    .andEqualTo("sessionId", request.getSessionId())
                    .andEqualTo("yyyymmdd", request.getYyyymmdd());
            List<PromoSession> sessionList = sessionMapper.selectByExample(promoSessionExample);


            if (CollectionUtils.isEmpty(sessionList)) {
                // 如果没找到秒杀场次
//                response.setCode(PromoRetCode.PROMO_NOT_EXIST.getCode());
//                response.setMsg(PromoRetCode.PROMO_NOT_EXIST.getMessage());
//                return response;
                /**
                 * 如果没有找到秒杀场次，我们直接在数据库里面添加一个秒杀场次
                 * 仅仅是demo， 不用于生产环境
                 */
                for (int i = 1; i <= 2; i++) {
                    PromoSession promoSession = new PromoSession();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(calendar.HOUR_OF_DAY, (i - 1) * 6);
                    promoSession.setStartTime(calendar.getTime());

                    calendar.setTime(new Date());
                    calendar.add(Calendar.HOUR_OF_DAY, 2+(i-1)*6);
//                    log.info("timeAfter2hours:{}", timeAfter2Hours);
                    promoSession.setEndTime(calendar.getTime());

                    promoSession.setSessionId(i);
                    promoSession.setYyyymmdd(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                    int insert = sessionMapper.insert(promoSession);
                    log.info("insert:{}",insert);

                    /**
                     * 主键可以为空，数据自增
                     * start，endTime不能为空
                     */
                    PromoItem promoItem = new PromoItem();
                    promoItem.setItemId(100023501L);
                    promoItem.setItemStock(10);
                    promoItem.setPsId(promoSession.getId());
                    /**
                     * 这里输出的ps_id还是null
                     * 但是插入到数据库中的ps_id确实是正确的ps_id
                     * 怀疑是有动态代理的东西，所以调试的时候看不出来ps_id
                     */
                    Integer ps_id = promoItem.getId();
                    log.info("ps_id:{}", ps_id);
                    promoItem.setSeckillPrice(new BigDecimal(30L));
                    promoItemMapper.insert(promoItem);

                    promoItem.setItemId(100052801L);
                    promoItemMapper.insert(promoItem);

                    promoItem.setItemId(100057501L);
                    promoItemMapper.insert(promoItem);

                    promoItem.setItemId(100047101L);
                    promoItemMapper.insert(promoItem);
                }
                sessionList = sessionMapper.selectByExample(promoSessionExample);
            }

            PromoSession promoSession = sessionList.get(0);

            Example promoItemExample = getExample();
            promoItemExample.createCriteria().andEqualTo("psId", promoSession.getId());
            List<PromoItem> promoItems = promoItemMapper.selectByExample(promoItemExample);

            List<PromoItemInfoDto> productList = new ArrayList<>();

            // 对该秒杀场次的每一个商品，查找其商品详情
            promoItems.stream().forEach(promoItem -> {
                Long itemId = promoItem.getItemId();

                ProductDetailRequest productDetailRequest = new ProductDetailRequest();
                productDetailRequest.setId(itemId);
                ProductDetailResponse productDetail = productService.getProductDetail(productDetailRequest);
                ProductDetailDto productDetailDto = productDetail.getProductDetailDto();
                PromoItemInfoDto promoItemInfoDto = promoInfoConverter.convert2InfoDto(productDetailDto);
                promoItemInfoDto.setInventory(promoItem.getItemStock());
                promoItemInfoDto.setSeckillPrice(promoItem.getSeckillPrice());
                productList.add(promoItemInfoDto);

            });

            //组装参数
            response.setPsId(promoSession.getId());
            response.setSessionId(request.getSessionId());
            response.setCode(PromoRetCode.SUCCESS.getCode());
            response.setMsg(PromoRetCode.SUCCESS.getMessage());
            response.setProductList(productList);
        } catch (Exception e) {
            response.setCode(PromoRetCode.SYSTEM_ERROR.getCode());
            response.setMsg(PromoRetCode.SYSTEM_ERROR.getMessage());
        }
        return response;
    }

    @Override
    public CreatePromoOrderResponse createPromoOrder(CreatePromoOrderRequest request) {
        CreatePromoOrderResponse createPromoOrderResponse = new CreatePromoOrderResponse();
        createPromoOrderResponse.setCode(PromoRetCode.SUCCESS.getCode());
        createPromoOrderResponse.setMsg(PromoRetCode.SUCCESS.getMessage());


        List<PromoItem> promoItems = null;
        try {
            request.requestCheck();
            int effectiveRow = promoItemMapper.decreaseStock(request.getProductId(), request.getPsId());
            if (effectiveRow < 1) {
                createPromoOrderResponse.setCode(PromoRetCode.PROMO_ITEM_STOCK_NOT_ENOUGH.getCode());
                createPromoOrderResponse.setMsg(PromoRetCode.PROMO_ITEM_STOCK_NOT_ENOUGH.getMessage());
                return createPromoOrderResponse;
            }

            //获取商品的秒杀价格
            Example example = getExample();
            example.createCriteria()
                    .andEqualTo("psId", request.getPsId())
                    .andEqualTo("itemId", request.getProductId());
            promoItems = promoItemMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("PromoServiceImpl.createPromoOrder occurs error" + e);
            createPromoOrderResponse.setCode(PromoRetCode.SYSTEM_ERROR.getCode());
            createPromoOrderResponse.setMsg(PromoRetCode.SYSTEM_ERROR.getMessage());
            return createPromoOrderResponse;
        }
        if (CollectionUtils.isEmpty(promoItems)) {
            createPromoOrderResponse.setCode(PromoRetCode.PROMO_ITEM_NOT_EXIST.getCode());
            createPromoOrderResponse.setMsg(PromoRetCode.PROMO_ITEM_NOT_EXIST.getMessage());
            return createPromoOrderResponse;
        }

        PromoItem promoItem = promoItems.get(0);

        // 生成订单
        CreateSeckillOrderRequest createSeckillOrderRequest = new CreateSeckillOrderRequest();
        createSeckillOrderRequest.setUsername(request.getUsername());
        createSeckillOrderRequest.setUserId(request.getUserId());
        createSeckillOrderRequest.setProductId(request.getProductId());
        createSeckillOrderRequest.setPrice(promoItem.getSeckillPrice());
        CreateSeckillOrderResponse createSeckillOrderResponse
                = orderPromoService.createPromoOrder(createSeckillOrderRequest);

        if (!createSeckillOrderResponse.getCode().equals(PromoRetCode.SUCCESS.getCode())) {
            createPromoOrderResponse.setCode(createSeckillOrderResponse.getCode());
            createPromoOrderResponse.setMsg(createSeckillOrderResponse.getMsg());
            return createPromoOrderResponse;
        }

        createPromoOrderResponse.setInventory(promoItem.getItemStock());
        createPromoOrderResponse.setProductId(promoItem.getItemId());
        createPromoOrderResponse.setCode(PromoRetCode.SUCCESS.getCode());
        createPromoOrderResponse.setMsg(PromoRetCode.SUCCESS.getMessage());

        return createPromoOrderResponse;
    }

    @Override
    public CreatePromoOrderResponse createPromoOrderInTransaction(CreatePromoOrderRequest request) throws DistributedLockException {
        CreatePromoOrderResponse response = new CreatePromoOrderResponse();
//        String notEnoughKey = cacheManager.checkCache("promo_order_stock_not_enough_" + request.getProductId() + "_" + request.getPsId());
//        if (notEnoughKey != null && "none".equals(notEnoughKey.trim())) {
//            response.setCode(PromoRetCode.PROMO_ITEM_STOCK_NOT_ENOUGH.getCode());
//            response.setMsg(PromoRetCode.PROMO_ITEM_STOCK_NOT_ENOUGH.getMessage());
//            return response;
//        }
        // # issue 5 setCode调用了两次
        response.setCode(PromoRetCode.SUCCESS.getCode());
        response.setMsg(PromoRetCode.SUCCESS.getMessage());
        request.requestCheck();

        context.getBean(PromoService.class);
        Example example = getExample();
        example.createCriteria()
                .andEqualTo("psId", request.getPsId())
                .andEqualTo("itemId", request.getProductId());
        List<PromoItem> beforItems = promoItemMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(beforItems)) {
            response.setCode(PromoRetCode.PROMO_ITEM_NOT_EXIST.getCode());
            response.setMsg(PromoRetCode.PROMO_ITEM_NOT_EXIST.getMessage());
            return response;
        }
        PromoItem beforeItem = beforItems.get(0);

//        Integer itemStock = beforeItem.getItemStock();
////        if (itemStock < 1) {
////            String key = "promo_item_stock_not_enough_" + request.getPsId() + "_" + request.getProductId();
////            cacheManager.setCache(key, "stock_not_enough", 1);
////        }


        // 发送事务性消息

        CreateSeckillOrderRequest createSeckillOrderRequest = new CreateSeckillOrderRequest();
        createSeckillOrderRequest.setUsername(request.getUsername());
        createSeckillOrderRequest.setUserId(request.getUserId());
        createSeckillOrderRequest.setProductId(request.getProductId());
        createSeckillOrderRequest.setPrice(beforeItem.getSeckillPrice());
        createSeckillOrderRequest.setAddressId(request.getAddressId());
        createSeckillOrderRequest.setTel(request.getTel());
        createSeckillOrderRequest.setStreetName(request.getStreetName());
        boolean isSuccess = mqTransactionProducer.sendPromoOrderTransaction(createSeckillOrderRequest, request.getPsId(), request.getProductId());
        if (!isSuccess) {
            response.setCode(PromoRetCode.SYSTEM_ERROR.getCode());
            response.setMsg(PromoRetCode.SYSTEM_ERROR.getMessage());
            return response;
        }

        //发送消息成功，获取减少后的库存
        List<PromoItem> afterItems = promoItemMapper.selectByExample(example);
        PromoItem afterItem = afterItems.get(0);
        response.setProductId(afterItem.getItemId());
        response.setInventory(afterItem.getItemStock());
        return response;
    }

    @Transactional
    public Example getExample() {
        return new Example(PromoItem.class);
    }

    @Override
    public PromoProductDetailResponse getPromoProductDetail(PromoProductDetailRequest request) {

        PromoProductDetailResponse promoProductDetailResponse = new PromoProductDetailResponse();

        Example example = getExample();
        example.createCriteria()
                .andEqualTo("psId", request.getPsId())
                .andEqualTo("itemId", request.getProductId());


        List<PromoItem> promoItems = null;
        try {
            request.requestCheck();
            promoItems = promoItemMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(promoItems)) {
                promoProductDetailResponse.setCode(PromoRetCode.SYSTEM_ERROR.getCode());
                promoProductDetailResponse.setMsg(PromoRetCode.SYSTEM_ERROR.getMessage());
                return promoProductDetailResponse;
            }
        } catch (Exception e) {
            log.error("PromoServiceImpl.getPromoProductDetail occurs error");
            promoProductDetailResponse.setCode(PromoRetCode.SYSTEM_ERROR.getCode());
            promoProductDetailResponse.setMsg(PromoRetCode.SYSTEM_ERROR.getMessage());
            return promoProductDetailResponse;
        }
        // 秒杀商品条目
        PromoItem promoItem = promoItems.get(0);

        ProductDetailRequest productDetailRequest = new ProductDetailRequest();
        productDetailRequest.setId(request.getProductId());
        ProductDetailResponse productDetailResponse = productService.getProductDetail(productDetailRequest);
        if (!PromoRetCode.SUCCESS.getCode().equals(productDetailResponse.getCode())) {
            promoProductDetailResponse.setMsg(productDetailResponse.getMsg());
            return promoProductDetailResponse;
        }
        PromoProductDetailDTO promoProductDetailDTO
                = promoProductConverter.convert2DetailDTO(promoItem, productDetailResponse.getProductDetailDto());

        promoProductDetailResponse.setPromoProductDetailDTO(promoProductDetailDTO);
        promoProductDetailResponse.setCode(PromoRetCode.SUCCESS.getCode());
        promoProductDetailResponse.setMsg(PromoRetCode.SUCCESS.getMessage());
        return promoProductDetailResponse;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}