package com.mall.user.services;

import com.mall.user.IUserLoginService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.converter.MemberConverter;
import com.mall.user.dal.entitys.Member;
import com.mall.user.dal.persistence.MemberMapper;
import com.mall.user.dto.*;
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
                    CheckAuthUserInfo userInfo = new CheckAuthUserInfo();
//                    userInfo.setPermit(true);
                    userInfo.setUid(member.getId());
                    //file存到token_message里面没用，用户可能随时改头像，token不会及时更新
                    //如果只有uid这个属性的话，那么直接写一个uid=??即可，别的方法拿到message, 也能直接解析
                    //但是这里还是放到json中，因为方便后续的扩充（成对象)
                    //另外别的方法拿到message， 然后放到userInfo中，还是要封装成json对象的，直接一开始就封好，省事
//                    userInfo.setFile(member.getFile());
//                    String message = JSON.toJSONString(member.getId());
                    String message = "{\"uid\":" + member.getId() + "}";
                    log.info("message:{}", message);
//                    String message = TokenConstants.PERMITTED.getTokenMessage()+member.getId()+":"+member.getFile();
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

    @Override
    public CheckAuthResponse freeAndValidToken(CheckAuthRequest request) {
        CheckAuthResponse response = new CheckAuthResponse();
        try {
            request.requestCheck();
            String message = JwtTokenUtils.builder().token(request.getToken()).build().freeJwt();
            //转换不成功的话，会是null还是报错
//            CheckAuthUserInfo userInfo = JSON.parseObject(message, CheckAuthUserInfo.class);
//            JSONObject jsonObject = JSON.parseObject(message);
//            Boolean permit = jsonObject.getObject("permit", Boolean.class);
            //如果CheckAuthUserInfo里面有一个permit的话，login get的result不好传数据
//            Long uid = jsonObject.getObject("uid", Long.class);

            //uid>0 检查太严格了， 没有必要解析Json，浪费资源
            if (/*uid>0*/ message.contains("uid")) {
                //message 原封不动地 放入 userInfo 中
                response.setUserInfo(message);
                return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.SUCCESS);
            }
//            if (message!=null && message.startsWith(TokenConstants.PERMITTED.getTokenMessage())) {
//                //验证成功
//                String userInfo = message.replace(TokenConstants.PERMITTED.getTokenMessage(), "");
////                String[] uidAndFile = message.split(":");
//                response.setUserInfo(userInfo);
//                return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.SUCCESS);
//            }
            ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.TOKEN_VALID_FAILED);
        } catch (Exception e) {
            log.error("UserLoginServiceImpl.freeAndValidToken occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public CheckLoginResponse checkLogin(CheckLoginRequest request) {
        CheckLoginResponse response = new CheckLoginResponse();
        try {
            request.requestCheck();
            Member member = memberMapper.selectByPrimaryKey(request.getUid());
            if (member != null) {
                CheckLoginMemberDto checkLoginMemberDto = memberConverter.member2CheckLoginMemberDto(member);
                response.setCheckLoginMemberDto(checkLoginMemberDto);
                return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.SUCCESS);
            }
            return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.DB_EXCEPTION);
        } catch (Exception e) {
            log.error("UserLoginServiceImpl.checkLogin occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }
}
