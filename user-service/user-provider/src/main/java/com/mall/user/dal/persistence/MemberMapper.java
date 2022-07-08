package com.mall.user.dal.persistence;

import com.mall.commons.tool.tkmapper.TkMapper;
import com.mall.user.dal.entitys.Member;

public interface MemberMapper extends TkMapper<Member> {
    //实际上没用到，就是为了锻炼一下sql
    String selectFileByPrimaryKey(Long id);
}