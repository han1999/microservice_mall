package com.mall.user.dto;

import lombok.Data;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/17
 **/
@Data
public class CheckAuthUserInfo {
//    private Boolean permit;
    private Long uid;
    private String file;
}
