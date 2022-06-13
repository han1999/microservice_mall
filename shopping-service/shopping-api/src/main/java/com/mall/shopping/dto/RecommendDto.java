package com.mall.shopping.dto;/**
 * Created on 2019/7/29.
 */

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class RecommendDto implements Serializable{

    private Integer id;

    private String name;

    private Integer type;

    private Integer sortOrder;

    private Integer position;

    private String remark;




}
