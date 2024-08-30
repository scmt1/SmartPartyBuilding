/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.multishop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.dto.BrandSigningDTO;
import com.yami.shop.bean.model.Brand;
import com.yami.shop.bean.vo.BrandSigningVO;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.enums.StatusEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.BrandService;
import com.yami.shop.service.BrandShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 品牌管理
 *
 * @author lgh
 */
@RestController
@RequestMapping("/admin/brand")
@Api(tags="品牌接口")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandShopService brandShopService;

    @GetMapping("/page")
    @ApiOperation(value = "根据参数分页获取平台品牌列表", notes = "根据参数分页获取平台品牌列表")
    public ServerResponseEntity<IPage<Brand>> page(PageParam<Brand> page, Brand brand) {
        brand.setStatus(StatusEnum.ENABLE.value());
        brand.setShopId(Constant.PLATFORM_SHOP_ID);
        IPage<Brand> brandList = brandService.page(page, brand);
        return ServerResponseEntity.success(brandList);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取平台品牌列表（不分页）", notes = "获取平台品牌列表（不分页）")
    public ServerResponseEntity<List<Brand>> list(Brand brand) {
        brand.setShopId(Constant.PLATFORM_SHOP_ID);
        List<Brand> brandList = brandService.listByParams(brand);
        return ServerResponseEntity.success(brandList);
    }

    @PostMapping("/signing")
    @ApiOperation(value = "签约品牌", notes = "签约品牌")
    public ServerResponseEntity<Void> signingBrands(@Valid @RequestBody BrandSigningDTO brandSigningDTO) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        brandShopService.signingBrands(brandSigningDTO, shopId, false);
        return ServerResponseEntity.success();
    }

    @GetMapping("/listSigningBrand")
    @ApiOperation(value = "获取店铺下已签约的品牌列表(status为空则返回所有）", notes = "获取店铺下已签约的品牌列表(status为空则返回所有）")
    @ApiImplicitParam(name = "status", value = "签约状态：1：已通过 0待审核 -1未通过", required = false)
    public ServerResponseEntity<BrandSigningVO> listSigning(@RequestParam(value = "status", required = false) Integer status) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        BrandSigningVO brandSigningVO = brandShopService.listSigningByShopId(shopId, status);
        return ServerResponseEntity.success(brandSigningVO);
    }

    @GetMapping("/listAvailableByCategoryAndName")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "categoryId",value = "分类id"),
            @ApiImplicitParam(name = "brandName",value = "品牌名称")
    })
    @ApiOperation(value = "根据分类id与品牌名称分页获取分类下的品牌与店铺签约的品牌", notes = "根据分类id与品牌名称分页获取分类下的品牌与店铺签约的品牌")
    public ServerResponseEntity<IPage<Brand>> listAvailableBrandByCategoryIdAndBrandName(PageParam<Brand> page, @RequestParam(required = false) Long categoryId, @RequestParam(defaultValue = "") String brandName) {
        return ServerResponseEntity.success(brandService.pageAvailableBrandByCategoryIdAndBrandNameAndShopId(page, categoryId, brandName, SecurityUtils.getShopUser().getShopId()));
    }
}
