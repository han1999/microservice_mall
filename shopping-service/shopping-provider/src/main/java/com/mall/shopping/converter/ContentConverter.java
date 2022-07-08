package com.mall.shopping.converter;

import com.mall.shopping.dal.entitys.Panel;
import com.mall.shopping.dal.entitys.PanelContent;
import com.mall.shopping.dal.entitys.PanelContentItem;
import com.mall.shopping.dto.PanelContentDto;
import com.mall.shopping.dto.PanelContentItemDto;
import com.mall.shopping.dto.PanelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

/**
 *  cskaoyan
 */
@Mapper(componentModel = "spring")
public interface ContentConverter {

    @Mappings({})
    PanelContentDto panelContent2Dto(PanelContent panelContent);

    @Mappings({})
    PanelContentDto panelContentItems2Dto(PanelContentItem panelContentItem);

    /**
     * 这两个就是一模一样，根本不需要转换！
     * 下面代码是转化的源码，panelContentItems转换过程中有调用了转换函数
     * panelDto.setPanelContentItems( panelContentItems2Dto( panel.getPanelContentItems() ) );
     * @param panel
     * @return
     */
/*    @Override
    public PanelDto panel2Dto(Panel panel) {
        if ( panel == null ) {
            return null;
        }

        PanelDto panelDto = new PanelDto();

        panelDto.setId( panel.getId() );
        panelDto.setName( panel.getName() );
        panelDto.setType( panel.getType() );
        panelDto.setSortOrder( panel.getSortOrder() );
        panelDto.setPosition( panel.getPosition() );
        panelDto.setLimitNum( panel.getLimitNum() );
        panelDto.setStatus( panel.getStatus() );
        panelDto.setRemark( panel.getRemark() );
        panelDto.setPanelContentItems( panelContentItems2Dto( panel.getPanelContentItems() ) );

        return panelDto;
    }*/
    @Mappings({})
    PanelDto panel2Dto(Panel panel);

    List<PanelContentDto> panelContents2Dto(List<PanelContent> panelContents);

    List<PanelContentItemDto> panelContentItems2Dto(List<PanelContentItem> panelContentItems);

    List<PanelDto> panels2Dto(List<Panel> panels);
}
