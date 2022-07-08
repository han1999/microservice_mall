package com.mall.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/18
 **/
@Data
public class CheckLoginMemberDto implements Serializable {
    private Long uid;
    private String file;
}
