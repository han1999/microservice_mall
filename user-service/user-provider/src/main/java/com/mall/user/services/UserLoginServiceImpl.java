package com.mall.user.services;

import com.mall.user.IUserLoginService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.converter.MemberConverter;
import com.mall.user.dal.entitys.Member;
import com.mall.user.dal.persistence.MemberMapper;
import com.mall.user.dto.UserLoginRequest;
import com.mall.user.dto.UserLoginResponse;
import com.mall.user.utils.ExceptionProcessorUtils;
import com.mall.user.utils.JwtTokenUtils;
import com.mall.user.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/16
 **/

@Service
@Component
@Slf4j
public class UserLoginServiceImpl implements IUserLoginService {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberConverter memberConverter;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        UserLoginResponse response = new UserLoginResponse();
        try {
            request.requestCheck();
            Example example = new Example(Member.class);
            String md5Password = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
            example.createCriteria().andEqualTo("username", request.getUserName())
                    .andEqualTo("password", md5Password);
            List<Member> memberList = memberMapper.selectByExample(example);
            //查到了,
            if (memberList.size() == 1) {
                Member member = memberList.get(0);
                //激活了
                if ("Y".equals(member.getIsVerified())) {
                    response = memberConverter.member2UserLoginResponse(member);
                    String message = member.getUsername();
                    String token = JwtTokenUtils.builder().msg(message).build().creatJwtToken();
                    response.setToken(token);
                    return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.SUCCESS);
                }
                //未激活
                    return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.USER_ISVERFIED_ERROR);
            }
                ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.USERORPASSWORD_ERRROR);
        } catch (
                Exception e) {
            log.error("UserLoginServiceImpl.login occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }
}
