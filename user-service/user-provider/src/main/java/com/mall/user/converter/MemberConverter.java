package com.mall.user.converter;

import com.mall.user.dal.entitys.Member;
import com.mall.user.dto.CheckLoginMemberDto;
import com.mall.user.dto.QueryMemberResponse;
import com.mall.user.dto.UpdateMemberRequest;
import com.mall.user.dto.UserLoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 *  cskaoyan
 * create-date: 2019/7/31-上午12:05
 */
@Mapper(componentModel = "spring")
public interface MemberConverter {

    @Mappings({})
    QueryMemberResponse member2Res(Member member);

    @Mappings({})
    Member updateReq2Member(UpdateMemberRequest request);

    UserLoginResponse member2UserLoginResponse(Member member);

    @Mappings({
            @Mapping(source = "id", target = "uid")
    })
    CheckLoginMemberDto member2CheckLoginMemberDto(Member member);
}
