package com.mall.user.services;/**
 * Created by cskaoyan on 2019/7/30.
 */

import com.mall.user.IMemberService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.converter.MemberConverter;
import com.mall.user.dal.entitys.Member;
import com.mall.user.dal.persistence.MemberMapper;
import com.mall.user.dto.*;
import com.mall.user.utils.ExceptionProcessorUtils;
import com.mall.user.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * cskaoyan
 */
@Slf4j
@Component
@Service
public class MemberServiceImpl implements IMemberService {

    @Autowired
    MemberMapper memberMapper;

//    @Autowired
//    IUserLoginService userLoginService;

    @Autowired
    MemberConverter memberConverter;

    BASE64Decoder base64Decoder;

    @Value("${spring.resources.static-locations}")
    private String imageLocation;

//    @Value("${server.port}")
//    private Integer serverPort;

    @Value("${image.filepath}")
    private String imageFilePath;

    @PostConstruct
    public void init() {
        base64Decoder = new BASE64Decoder();
        imageLocation=imageLocation.replace("file:", "");
    }

    /**
     * 根据用户id查询用户会员信息
     *
     * @param request
     * @return
     */
    @Override
    public QueryMemberResponse queryMemberById(QueryMemberRequest request) {
        QueryMemberResponse queryMemberResponse = new QueryMemberResponse();
        try {
            request.requestCheck();
            Member member = memberMapper.selectByPrimaryKey(request.getUserId());
            if (member == null) {
                queryMemberResponse.setCode(SysRetCodeConstants.DATA_NOT_EXIST.getCode());
                queryMemberResponse.setMsg(SysRetCodeConstants.DATA_NOT_EXIST.getMessage());
            }
            queryMemberResponse = memberConverter.member2Res(member);
            queryMemberResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
            queryMemberResponse.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("MemberServiceImpl.queryMemberById Occur Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(queryMemberResponse, e);
        }
        return queryMemberResponse;
    }

    /**
     * 测试一下 为什么从前端接受到的字符串decode不成功？
     * 原因猜测是 前端传过来的字符串有额外字符 data:image/jpeg;base64, 作为开头了，需要先去掉
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
//        BASE64Encoder base64Encoder = new BASE64Encoder();
//        FileInputStream in = new FileInputStream("C:\\Users\\hx\\Pictures\\Saved Pictures\\壁纸\\wallpaper\\1.jpg");
//        int available = in.available();
//        byte[] bytes = new byte[available];
//        in.read(bytes);
//        String encode = base64Encoder.encode(bytes);
//        System.out.println("encode = " + encode);
//        BASE64Decoder base64Decoder = new BASE64Decoder();
//        byte[] decodeBuffer = base64Decoder.decodeBuffer(encode);
//        /**
//         * 能接受中文字符吗作为路径名吗？  可以！
//         */
//        FileOutputStream out = new FileOutputStream("C:\\Users\\hx\\Pictures\\Saved Pictures\\壁纸\\wallpaper\\1_1.jpg");
//        out.write(decodeBuffer);
//        out.close();
//        in.close();

        /**
         * 测试正则表达式
         * 感觉好复杂， 不太会啊啊啊啊
         */
        String s = "data:image/jpeg;base64,/1561654/465464";
        System.out.println("s = " + s);
        String s1 = s.replaceFirst("d.*base64", "");
        System.out.println("s1 = " + s1);
    }


    @Override
    public HeadImageResponse updateHeadImage(HeadImageRequest request) {
        HeadImageResponse response = new HeadImageResponse();
        //TODO
        try {
            //
            request.requestCheck();
            Long userId = request.getUserId();
            String imageData = request.getImageData();
            imageData = imageData.replaceFirst("data.*base64,", "");
            log.info("imageData:{}",imageData);
            byte[] bytes = base64Decoder.decodeBuffer(imageData);
//            for (int i = 0; i < bytes.length; i++) {
//                if (bytes[i] < 0) {
//                    bytes[i] += 256;
//                }
//            }
            /**
             * 这里出现一个意想不到的bug， new Data()，是有空格的，文件名中有空格，是没办法创建的！！！！
             */
            String filename = userId + "_" + new Date().getTime()+".png";
            String imagePath = imageLocation +filename;
            /**
             * 如果文件不存在，输出流会创建一个文件吗?
             * 会的！！！！！
             * 但是文件的命名一定不能有空格！！！！！！！！
             * 否则就算file.createNewFile都创建不了文件
             */
            File file = new File(imagePath);
            log.info("imagePath:{}", imagePath);
//            if (!file.exists()) {
//                boolean newFile = file.createNewFile();
//                log.info("newFile:{}",newFile);
//            }

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            outputStream.close();

            Member member = new Member();
            member.setId(userId);
            member.setFile(imageFilePath + filename);
            memberMapper.updateByPrimaryKeySelective(member);
            return ResponseUtils.setCodeAndMsg(response, SysRetCodeConstants.SUCCESS);
        } catch (Exception e) {
            log.error("MemberServiceImpl.updateHeadImage occurs Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    @Override
    public UpdateMemberResponse updateMember(UpdateMemberRequest request) {
        return null;
    }

//    @Override
//    public UpdateMemberResponse updateMember(UpdateMemberRequest request) {
//        UpdateMemberResponse response = new UpdateMemberResponse();
//        try{
//            request.requestCheck();
//            CheckAuthRequest checkAuthRequest = new CheckAuthRequest();
//            checkAuthRequest.setToken(request.getToken());
//            CheckAuthResponse authResponse = userLoginService.freeAndValidToken(checkAuthRequest);
//            if (!authResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
//                response.setCode(authResponse.getCode());
//                response.setMsg(authResponse.getMsg());
//                return response;
//            }
//            Member member = memberConverter.updateReq2Member(request);
//            int row = memberMapper.updateByPrimaryKeySelective(member);
//            response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
//            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
//            log.info("MemberServiceImpl.updateMember effect row :"+row);
//        }catch (Exception e){
//            log.error("MemberServiceImpl.updateMember Occur Exception :"+e);
//            ExceptionProcessorUtils.wrapperHandlerException(response,e);
//        }
//        return response;
//    }
}
