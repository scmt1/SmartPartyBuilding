/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import com.yami.shop.bean.app.dto.SkuDto;
import lombok.Data;

import java.util.List;

/**
 * 商品信息及商品下所有规格信息
 */
/**
 * @author Yami
 */
@Data
public class ProdAndSkuListsDto {

    /**
     * 商品id
     */
    private Integer prodId;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 规格列表
     */
    private List<SkuDto> skuDtoList;
}
