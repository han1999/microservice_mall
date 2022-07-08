package com.mall.user.services;

import com.mall.user.IVerifyService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dal.entitys.Member;
import com.mall.user.dal.entitys.UserVerify;
import com.mall.user.dal.persistence.MemberMapper;
import com.mall.user.dal.persistence.UserVerifyMapper;
import com.mall.user.dto.UserVerifyRequest;
import com.mall.user.dto.UserVerifyResponse;
import com.mall.user.utils.ExceptionProcessorUtils;
import com.mall.user.utils.ResponseUtils;
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
 * @date: 2022/6/17
 **/

@Service
@Component
@Slf4j
public class VerifyServiceImpl implements IVerifyService {
    @Autowired
    UserVerifyMapper userVerifyMapper;

    @Autowired
    MemberMapper memberMapper;

    @Override
    public UserVerifyResponse verify(UserVerifyRequest request) {
        UserVerifyResponse response = new UserVerifyResponse();
        try {
            /*
            验证uuid和username是否对应
             */
            request.requestCheck();
            List<UserVerify> userVerifyList = getUserVerifyList(request);
            if (CollectionUtils.isEmpty(userVerifyList)) {
                return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.USERVERIFY_INFOR_INVALID);
            }

            // TODO: 2022/6/17  修改两个表的字段应该是一个事务 
            /*
            修改tb_member和tb_user_verify的isverified/is_verify字段
             */
            int rows = updateMember(request);
            if (rows == 0) {
                return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.DB_EXCEPTION);
            }

            rows = updateUserVerify(request);
            if (rows == 0) {
                return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.DB_EXCEPTION);
            }
            return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.SUCCESS);
        } catch (Exception e) {
            log.error("VerifyServiceImpl.verify occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    private int updateUserVerify(UserVerifyRequest request) {
        int rows;
        UserVerify userVerify = new UserVerify();
        userVerify.setIsVerify("Y");
        Example example2 = new Example(UserVerify.class);
        example2.createCriteria().andEqualTo("username", request.getUserName())
                .andEqualTo("uuid", request.getUuid());
        rows = userVerifyMapper.updateByExampleSelective(userVerify, example2);
        return rows;
    }

    private int updateMember(UserVerifyRequest request) {
        Example example1 = new Example(Member.class);
        example1.createCriteria().andEqualTo("username", request.getUserName());
        Member member = new Member();
        member.setIsVerified("Y");
        return memberMapper.updateByExampleSelective(member, example1);
    }

    private List<UserVerify> getUserVerifyList(UserVerifyRequest request) {
        Example example = new Example(UserVerify.class);
        example.createCriteria().andEqualTo("username", request.getUserName())
                .andEqualTo("uuid", request.getUuid());
        return userVerifyMapper.selectByExample(example);
    }
}
