package com.mall.user.constants;

/**
 * @description:
 * @author: Han Xiao
 * @date: 2022/6/17
 **/
public enum TokenConstants {
    PERMITTED  ("PERMIT:");
    private String tokenMessage;

    public String getTokenMessage() {
        return tokenMessage;
    }

    public void setTokenMessage(String tokenMessage) {
        this.tokenMessage = tokenMessage;
    }

    TokenConstants(String tokenMessage) {
        this.tokenMessage = tokenMessage;
    }}
