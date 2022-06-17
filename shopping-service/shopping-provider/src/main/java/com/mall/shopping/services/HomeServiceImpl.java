package com.mall.shopping.services;

import com.mall.shopping.IHomeService;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ContentConverter;
import com.mall.shopping.dal.entitys.Panel;
import com.mall.shopping.dal.entitys.PanelContentItem;
import com.mall.shopping.dal.persistence.PanelContentMapper;
import com.mall.shopping.dal.persistence.PanelMapper;
import com.mall.shopping.dto.HomePageResponse;
import com.mall.shopping.dto.PanelDto;
import com.mall.shopping.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.HashSet;
import java.util.List;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/17
 **/
@Service
@Component
@Slf4j
public class HomeServiceImpl implements IHomeService {
    @Autowired
    PanelMapper panelMapper;

    @Autowired
    PanelContentMapper panelContentMapper;

    @Autowired
    ContentConverter contentConverter;

    @Override
    public HomePageResponse homepage() {
        HomePageResponse response = new HomePageResponse();
        HashSet<PanelDto> panelDtos = new HashSet<>();
        try {
            Example example = new Example(Panel.class);
            //status=1 一级板块
            example.createCriteria().andEqualTo("status", 1);
            //即便panel有序，那么传过去的set，也是无序的呀，所以这个语句应该没用
            //实际上测试后发现确实没用，但是还是写上去吧
            example.setOrderByClause("sort_order");
            List<Panel> panelList = panelMapper.selectByExample(example);
            for (Panel panel : panelList) {
                List<PanelContentItem> panelContentItems = panelContentMapper.selectPanelContentAndProductWithPanelId(panel.getId());
                Integer limitNum = panel.getLimitNum();
                if (panelContentItems != null && panelContentItems.size() < limitNum) {
                    limitNum = panelContentItems.size();
                }
                //这个limitNum业务确实没想到， 如果返回数据过多会怎么样呢
                panelContentItems = panelContentItems.subList(0, limitNum);
                // panelContentItem已经满足json的要求了
                // 并不需要 panelContentItemDto dto中SubTitle首字母大写反而是错的
                panel.setPanelContentItems(panelContentItems);

                //Panel需要转成PanelDto, 因为Panel中有created和updated， json里面没有
                PanelDto panelDto = contentConverter.panen2Dto(panel);
                panelDtos.add(panelDto);
            }
            //定义得好混乱！！！！
            response.setPanelContentItemDtos(panelDtos);
            return ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.SUCCESS);
        } catch (Exception e) {
            log.error("HomeServiceImpl.homepage occurs Exception :" + e);
            ResponseUtils.setCodeAndMsg(response, ShoppingRetCode.DB_EXCEPTION);
            //如果先setCodeAndMsg再wrapperHandlerException， 最后都会变成系统异常
//            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }
}
