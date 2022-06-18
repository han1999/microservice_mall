package com.mall.shopping.services;

import com.mall.shopping.INavigationService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ContentConverter;
import com.mall.shopping.dal.entitys.PanelContent;
import com.mall.shopping.dal.persistence.PanelContentMapper;
import com.mall.shopping.dto.NavigationResponse;
import com.mall.shopping.dto.PanelContentDto;
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
public class NavigationServiceImpl implements INavigationService {
    @Autowired
    PanelContentMapper panelContentMapper;

    @Autowired
    ContentConverter contentConverter;

    @Override
    public NavigationResponse navigation() {
        NavigationResponse response = new NavigationResponse();
        try {
            Example example = new Example(PanelContent.class);
            example.createCriteria().andEqualTo("panelId", 0);
            example.setOrderByClause("id");
            List<PanelContent> panelContentList = panelContentMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(panelContentList)) {
                List<PanelContentDto> panelContentDtoList = contentConverter.panelContents2Dto(panelContentList);
                response.setPanelContentDtoList(panelContentDtoList);
                return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
            }
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.DB_EXCEPTION);
        } catch (Exception e) {
            log.error("NavigationServiceImpl.navigation occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }
}
