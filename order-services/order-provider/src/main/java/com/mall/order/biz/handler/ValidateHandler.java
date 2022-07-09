package com.mall.order.biz.handler;

import com.mall.commons.tool.exception.BizException;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.user.IMemberService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.QueryMemberRequest;
import com.mall.user.dto.QueryMemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Slf4j
@Component
public class ValidateHandler extends AbstractTransHandler {

    /**
     * pom文件中有user-api
     */
    @Reference(timeout = 3000, retries = 0, check = false)
    private IMemberService memberService;

    /**
     * 验证用户合法性
     * @return
     */

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @Transactional
    public boolean handle(TransHandlerContext context) {
        CreateOrderContext createOrderContext = (CreateOrderContext) context;
        Long userId = createOrderContext.getUserId();
        QueryMemberRequest request = new QueryMemberRequest();
        request.setUserId(userId);
        QueryMemberResponse response = memberService.queryMemberById(request);
        if (!SysRetCodeConstants.SUCCESS.getCode().equals(response.getCode())) {
            throw new BizException(response.getCode(), response.getMsg());
        }
        return true;
    }

}
