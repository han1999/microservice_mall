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
import com.mall.user.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

//    没办法自动注入吗？可以自动注入，可是没有@component咋做到的呢
    //考虑到这个是springframework里面的东西，可能是有一个配置类，已经注册过了
    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.verify.url}")
    private String emailVerifyUrl;


//    @PostConstruct
//    public void

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        UserRegisterResponse response = new UserRegisterResponse();
        try {
            request.requestCheck();
            log.info("参数校验完毕");
            //判断有没有重复的
            validUsernameRepeat(request);
            log.info("检查重名完毕");

            Member member = new Member();
            member.setUsername(request.getUserName());
            member.setEmail(request.getEmail());
            String userPassword = DigestUtils.md5DigestAsHex(request.getUserPwd().getBytes());
            member.setPassword(userPassword);

            member.setCreated(new Date());
            member.setUpdated(new Date());
            member.setIsVerified("N");
            member.setState(1);
            //向member表中插入一条记录
            int affectedRows = memberMapper.insert(member);
            log.info("member表插入完毕");
            if (affectedRows != 1) {
                response.setCode(SysRetCodeConstants.USER_REGISTER_FAILED.getCode());
                response.setMsg(SysRetCodeConstants.USER_REGISTER_FAILED.getMessage());
            }

            //向用户验证表插入一个记录
            String key = member.getUsername() + member.getPassword() + UUID.randomUUID().toString();
            String uuid = DigestUtils.md5DigestAsHex(key.getBytes());
            UserVerify userVerify = new UserVerify();
            userVerify.setUsername(member.getUsername());
            userVerify.setUuid(uuid);
            userVerify.setRegisterDate(new Date());
            userVerify.setIsExpire("N");
            userVerify.setIsVerify("N");
            //后来感觉 还是用set方法直观，也容易后面再来看时理解
//            UserVerify userVerify = new UserVerify(null, member.getUsername(),
//                    uuid, new Date(), "N", "N");
            int rows = userVerifyMapper.insert(userVerify);
            log.info("user_verify表插入完毕");
            if (rows != 1) {
                response.setCode(SysRetCodeConstants.USER_REGISTER_VERIFY_FAILED.getCode());
                response.setMsg(SysRetCodeConstants.USER_REGISTER_VERIFY_FAILED.getMessage());
            }

            // TODO: 2022/6/16  发送邮件
            sendEmail(uuid, request);
            log.info("邮件发送成功");

            //打印日志
            log.info("注册成功,注册参数:{}", JSON.toJSON(request));
            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
            response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
        }catch (DuplicateKeyException e){
            ResponseUtils.setCodeAndMsg(response,SysRetCodeConstants.USER_INFOR_INVALID);
        } catch (Exception e) {
//            response.setCode(SysRetCodeConstants.USER_REGISTER_FAILED.getCode());
//            response.setMsg(SysRetCodeConstants.USER_REGISTER_FAILED.getMessage());
            //这里的系统错误，其实就是数据库操作失败（例如有的键是unique, phone/username/email)
            log.error("RegisterServiceImpl.register occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;

    }

    /**
     * 发送激活邮件
     * @param uuid
     * @param request
     */
    private void sendEmail(String uuid, UserRegisterRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        // TODO: 2022/6/16  不要直接写， 后续应该放到配置文件中
        message.setSubject("网上商城用户激活");
        message.setFrom("1295806070@qq.com");
        message.setTo(request.getEmail());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("请点击下方链接，以验证注册：\n");
        stringBuilder.append(emailVerifyUrl)
                .append(uuid)
                .append("&username=")
                .append(request.getUserName());
        message.setText(stringBuilder.toString());
        mailSender.send(message);
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
            log.info("有重名！");
            throw new ValidateException(
                    SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getCode(),
                    SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getMessage()
            );
        }


    }
}
