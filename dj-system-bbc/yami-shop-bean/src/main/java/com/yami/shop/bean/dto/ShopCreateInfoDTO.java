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

import com.yami.shop.bean.model.CategoryShop;
import com.yami.shop.bean.model.ShopBankCard;
import com.yami.shop.bean.model.ShopCompany;
import com.yami.shop.bean.model.ShopDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author lth
 * @Date 2021/8/3 14:42
 */
@Data
public class ShopCreateInfoDTO {

    @ApiModelProperty(value = "商家注册信息")
    @Valid
    private ShopUserRegisterDto shopUserRegisterInfo;

    @ApiModelProperty(value = "店铺基本信息")
    @Valid
    private ShopDetail shopDetail;

    @ApiModelProperty(value = "店铺工商信息")
    @Valid
    private ShopCompany shopCompany;

    @ApiModelProperty(value = "签约分类列表")
    @Valid
    private List<CategoryShop> categorySigningList;

    @ApiModelProperty(value = "签约品牌列表")
    @Valid
    private List<BrandShopDTO> brandSigningList;

    @ApiModelProperty(value = "店铺银行卡列表")
    @Valid
    private List<ShopBankCard> shopBankCardList;
}
