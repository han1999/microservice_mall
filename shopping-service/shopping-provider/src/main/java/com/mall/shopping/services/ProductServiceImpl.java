package com.mall.shopping.services;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.shopping.IProductService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ContentConverter;
import com.mall.shopping.converter.ProductConverter;
import com.mall.shopping.dal.entitys.Item;
import com.mall.shopping.dal.entitys.ItemDesc;
import com.mall.shopping.dal.entitys.Panel;
import com.mall.shopping.dal.entitys.PanelContentItem;
import com.mall.shopping.dal.persistence.ItemDescMapper;
import com.mall.shopping.dal.persistence.ItemMapper;
import com.mall.shopping.dal.persistence.PanelContentMapper;
import com.mall.shopping.dal.persistence.PanelMapper;
import com.mall.shopping.dto.*;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import com.mall.shopping.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/

@Service
@Component
@Slf4j
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private PanelContentMapper panelContentMapper;

    @Autowired
    private PanelMapper panelMapper;

    @Autowired
    private ContentConverter contentConverter;

    @Override
    public ProductDetailResponse getProductDetail(ProductDetailRequest request) {
        ProductDetailResponse response = new ProductDetailResponse();
        try {
            request.requestCheck();
            Item item = itemMapper.selectByPrimaryKey(request.getId());
            if (item == null) {
                return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.DB_EXCEPTION);
            }
            /**
             * ???????????????????????????????????????item.setImageBig????????? ????????????????????????
             * ??????????????????mapstruct????????????get????????????????????????get???????????????????????????
             */
      /*      item.setImageBig();
            item.setImages();*/
            ProductDetailDto productDetailDto = productConverter.item2ProductDetailDto(item);
            ItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(request.getId());
            //detail???????????????null
            String detail = itemDesc.getItemDesc();
//            String detail = null;
            productDetailDto.setDetail(detail);
            response.setProductDetailDto(productDetailDto);
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
        } catch (Exception e) {
            log.error("ProductServiceImpl.getProductDetail occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public AllProductResponse getAllProduct(AllProductRequest request) {
        AllProductResponse response = new AllProductResponse();
        try {
            request.requestCheck();
            PageHelper.startPage(request.getPage(), request.getSize());
            Example example = new Example(Item.class);
            /**
             * ??????request.getPriceGt()==null ?????????????????????????????????
             */
            example.createCriteria().andGreaterThan("price", request.getPriceGt())
                    .andLessThan("price", request.getPriceLte());
            String sort = request.getSort();
            if ("1".equals(sort)) {
                example.setOrderByClause("price");
            } else if ("-1".equals(sort)) {
                example.setOrderByClause("price desc");
            }
            List<Item> itemList = itemMapper.selectByExample(example);
            if (itemList instanceof Page) {
                /**
                 * PageHelper?????????Mybatis??? ???????????????itemList???Page?????????
                 * ??????itemPageInfo.getTotal()???itemList.size?????????
                 * ??????page==2 itemPageInfo.getTotal()==23  itemList.size()=3
                 * itemList?????????????????????????????????????????????
                 */
                log.debug("itemList's type is Page");
            }
            if (!CollectionUtils.isEmpty(itemList)) {
                List<ProductDto> productDtoList = productConverter.items2Dto(itemList);
                log.debug("itemList's size is "+itemList.size());
                PageInfo<Item> itemPageInfo = new PageInfo<>(itemList);
//                response.setProductDtoList(productDtoList);
                response.setData(productDtoList);
                response.setTotal(itemPageInfo.getTotal());
                return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
            }
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.DB_EXCEPTION);
        } catch (Exception e) {
            log.error("ProductServiceImpl.getAllProduct occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public RecommendResponse getRecommendGoods() {
        RecommendResponse response = new RecommendResponse();
        try {
            /**
             * id:6 ???????????? ????????????id????????? ???????????????name??????
             */
//            Panel panel = panelMapper.selectByPrimaryKey(6);
            /**
             * List???????????????????????????
             */
            Example example = new Example(Panel.class);
            example.createCriteria().andEqualTo("name", "????????????");
            example.setOrderByClause("sort_order");
            List<Panel> panelList = panelMapper.selectByExample(example);
            if (panelList != null && panelList.size() == 1) {
                /**
                 * panel???????????????panel????????? panelList????????????????????????,?????????
                 * C++???????????????
                 */
                Panel panel = panelList.get(0);
                List<PanelContentItem> panelContentItems = panelContentMapper.selectPanelContentAndProductWithPanelId(panel.getId());
                if (panelContentItems != null && panel.getLimitNum() < panelContentItems.size()) {
                    panelContentItems = panelContentItems.subList(0, panel.getLimitNum());
                }
                panel.setPanelContentItems(panelContentItems);
                //?????????panelList?????????????????????????????????
                List<PanelDto> panelDtoList = contentConverter.panels2Dto(panelList);
                response.setPanelDtoList(panelDtoList);
                return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
            }
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.DB_EXCEPTION);

        } catch (Exception e) {
            log.error("ProductServiceImpl.getRecommendGoods occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }
}
