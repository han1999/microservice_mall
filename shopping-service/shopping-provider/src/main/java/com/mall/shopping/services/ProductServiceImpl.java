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
             * 这样感觉不太好，如果忘记写item.setImageBig的话， 这个属性就没有了
             * 并不是，因为mapstruct调用的是get方法，所以直接在get里面写逻辑就可以了
             */
      /*      item.setImageBig();
            item.setImages();*/
            ProductDetailDto productDetailDto = productConverter.item2ProductDetailDto(item);
            ItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(request.getId());
            //detail有可能就是null
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
             * 如果request.getPriceGt()==null 相当于忽略这个查询条件
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
                 * PageHelper会拦截Mybatis， 最后返回的itemList是Page类型的
                 * 所以itemPageInfo.getTotal()和itemList.size不一样
                 * 例如page==2 itemPageInfo.getTotal()==23  itemList.size()=3
                 * itemList实际上传过来时候，就只有三个值
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
             * id:6 热门推荐 但是如果id变了呢 所以还是用name搜吧
             */
//            Panel panel = panelMapper.selectByPrimaryKey(6);
            /**
             * List里面就只有一个元素
             */
            Example example = new Example(Panel.class);
            example.createCriteria().andEqualTo("name", "热门推荐");
            example.setOrderByClause("sort_order");
            List<Panel> panelList = panelMapper.selectByExample(example);
            if (panelList != null && panelList.size() == 1) {
                /**
                 * panel是个引用，panel改变， panelList第一个元素也改变,切记！
                 * C++不是这样的
                 */
                Panel panel = panelList.get(0);
                List<PanelContentItem> panelContentItems = panelContentMapper.selectPanelContentAndProductWithPanelId(panel.getId());
                if (panelContentItems != null && panel.getLimitNum() < panelContentItems.size()) {
                    panelContentItems = panelContentItems.subList(0, panel.getLimitNum());
                }
                panel.setPanelContentItems(panelContentItems);
                //直接对panelList转换成最终结果吧，方便
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
