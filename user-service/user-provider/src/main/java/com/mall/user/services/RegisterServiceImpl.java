package com.mall.user.services;

import com.alibaba.fastjson.JSON;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.user.IRegisterService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dal.entitys.Member;
import com.mall.user.dal.entitys.UserVerify;
import com.mall.user.dal.persistence.MemberMapper;
import com.mall.user.dal.persistence.UserVerifyMapper;
import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;
import com.mall.user.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/16
 **/
@Service
@Slf4j
@Component
public class RegisterServiceImpl implements IRegisterService {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private UserVerifyMapper userVerifyMapper;

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        UserRegisterResponse response = new UserRegisterResponse();
        try {
            request.requestCheck();
            validUsernameRepeat(request);

            Member member = new Member();
            member.setUsername(request.getUserName());
            member.setEmail(request.getEmail());
            String userPassword = DigestUtils.md5DigestAsHex(request.getUserPwd().getBytes());
            member.setPassword(userPassword);

            member.setCreated(new Date());
            member.setUpdated(new Date());
            member.setIsVerified("N");
            //1 初始状态
            member.setState(1);
            int affectedRows = memberMapper.insert(member);
            if (affectedRows != 1) {
                response.setCode(SysRetCodeConstants.USER_REGISTER_FAILED.getCode());
                response.setMsg(SysRetCodeConstants.USER_REGISTER_FAILED.getMessage());
            }
            //向用户验证表插入一个记录
            String key = member.getUsername() + member.getPassword() + UUID.randomUUID().toString();
            String uuid = DigestUtils.md5DigestAsHex(key.getBytes());
            UserVerify userVerify = new UserVerify(null, member.getUsername(),
                    uuid, new Date(), "N", "N");
            int rows = userVerifyMapper.insert(userVerify);
            if (rows != 1) {
                response.setCode(SysRetCodeConstants.USER_REGISTER_VERIFY_FAILED.getCode());
                response.setMsg(SysRetCodeConstants.USER_REGISTER_VERIFY_FAILED.getMessage());
            }

            // TODO: 2022/6/16  发送邮件
            //打印日志
            log.info("注册成功,注册参数:{}", JSON.toJSON(request));
            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
            response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
        } catch (Exception e) {
//            response.setCode(SysRetCodeConstants.USER_REGISTER_FAILED.getCode());
//            response.setMsg(SysRetCodeConstants.USER_REGISTER_FAILED.getMessage());
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;

    }

    /**
     * 验证用户名是否重复
     * @param registerRequest
     */
    private void validUsernameRepeat(UserRegisterRequest registerRequest) {
        Example example = new Example(Member.class);
        example.createCriteria().andEqualTo("username", registerRequest.getUserName());
        List<Member> members = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(members)) {
            throw new ValidateException(
                    SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getCode(),
                    SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getMessage()
            );
        }

    }
}
