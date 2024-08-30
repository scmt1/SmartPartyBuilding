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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.EsOperationType;
import com.yami.shop.bean.event.EsProductUpdateEvent;
import com.yami.shop.bean.model.Category;
import com.yami.shop.bean.model.CategoryShop;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.param.ProductExportParam;
import com.yami.shop.bean.vo.CategoryShopVO;
import com.yami.shop.bean.vo.CategoryVO;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.enums.StatusEnum;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.i18n.LanguageEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.CategoryExcelService;
import com.yami.shop.service.CategoryService;
import com.yami.shop.service.CategoryShopService;
import com.yami.shop.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 分类管理
 * @author lgh
 *
 */
@RestController
@RequestMapping("/prod/category")
@Api(tags="分类接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryShopService categoryShopService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private CategoryExcelService categoryExcelService;


    @GetMapping("/info/{categoryId}")
    @ApiOperation(value = "根据分类id获取分类信息", notes = "根据分类id获取分类信息")
    public ServerResponseEntity<Category> info(@PathVariable("categoryId") Long categoryId){
        // 获取数据库中的分类，如果分类为空则抛异常
        Category category = categoryService.getCategoryByCategoryIdAndShopId(categoryId, SecurityUtils.getShopUser().getShopId());
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), category.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(category);
    }

    @SysLog("保存分类")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('prod:category:save')")
    @ApiOperation(value = "保存分类信息", notes = "保存分类信息")
    public ServerResponseEntity<Void> save(@RequestBody Category category){
        category.setShopId(SecurityUtils.getShopUser().getShopId());
        category.setRecTime(new Date());
        Integer count = categoryService.getCategoryName(category);
        if(count > 0){
            // 类目名称已存在！
            throw new YamiShopBindException("yami.category.name.exist");
        }
        category.setSeq(Objects.isNull(category.getSeq()) ? 0 : category.getSeq());
        categoryService.saveCategroy(category);
        categoryService.removeCacheByParentIdAndLang(category.getParentId(),category.getShopId(), LanguageEnum.LANGUAGE_ZH_CN.getLang());
        categoryService.removeCacheByParentIdAndLang(category.getParentId(), category.getShopId(), LanguageEnum.LANGUAGE_EN.getLang());
        return ServerResponseEntity.success();
    }

    @SysLog("更新分类")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('prod:category:update')")
    @ApiOperation(value = "更新分类信息", notes = "更新分类信息")
    public ServerResponseEntity<String> update(@RequestBody Category category){
        // 获取数据库中的分类，如果分类为空则抛异常
        categoryService.getCategoryByCategoryIdAndShopId(category.getCategoryId(),SecurityUtils.getShopUser().getShopId());
        category.setShopId(SecurityUtils.getShopUser().getShopId());
        Category categoryDb = categoryService.getCategoryByCategoryId(category.getCategoryId());
        if (Objects.equals(categoryDb.getParentId(), Constant.CATEGORY_ID) && !Objects.equals(category.getParentId(), Constant.CATEGORY_ID)){
            // 一级分类不能改为二级分类
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.category.one.check"));
        }else if(Objects.equals(category.getParentId(), Constant.CATEGORY_ID) && !Objects.equals(categoryDb.getParentId(), Constant.CATEGORY_ID)){
            // 二级分类不能改为一级分类
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.category.two.check"));
        }
        if (Objects.equals(category.getParentId(),category.getCategoryId())) {
            // 分类的上级不能是自己本身
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.category.superior.check"));
        }
        Integer count = categoryService.getCategoryName(category);
        if(count > 0){
            // 类目名称已存在！
            throw new YamiShopBindException("yami.category.name.exist");
        }
        category.setOldCategoryName(categoryDb.getCategoryName());
        category.setOldCategoryNameEn(categoryDb.getCategoryNameEn());
        categoryService.updateCategroy(category);
        categoryService.removeCacheByParentIdAndLang(category.getParentId(), category.getShopId(), LanguageEnum.LANGUAGE_ZH_CN.getLang());
        categoryService.removeCacheByParentIdAndLang(category.getParentId(), category.getShopId(), LanguageEnum.LANGUAGE_EN.getLang());
        eventPublisher.publishEvent(new EsProductUpdateEvent(category.getCategoryId(), null, EsOperationType.UPDATE_BY_SHOP_CATEGORY_ID));
        return ServerResponseEntity.success();
    }

    @SysLog("删除分类")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("@pms.hasPermission('prod:category:delete')")
    @ApiOperation(value = "根据分类id删除分类", notes = "根据分类id删除分类")
    public ServerResponseEntity<String> delete(@PathVariable("categoryId") Long categoryId){
        int categoryProdCount = productService.count(new LambdaQueryWrapper<Product>().eq(Product::getShopCategoryId, categoryId).ne(Product::getStatus, -1));
        if (categoryProdCount>0){
            // 该分类下还有商品，请先删除该分类下的商品
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.category.delete.check"));
        }

        Category category = categoryService.getCategoryByCategoryIdAndShopId(categoryId,SecurityUtils.getShopUser().getShopId());
        categoryService.deleteCategroy(category);
        categoryService.removeCacheByParentIdAndLang(category.getParentId(), category.getShopId(), LanguageEnum.LANGUAGE_ZH_CN.getLang());
        categoryService.removeCacheByParentIdAndLang(category.getParentId(), category.getShopId(), LanguageEnum.LANGUAGE_EN.getLang());
        return ServerResponseEntity.success();
    }

    @GetMapping("/listCategory")
    @PreAuthorize("@pms.hasPermission('prod:category:listCategory')")
    @ApiOperation(value = "获取一级分类", notes = "获取一级分类")
    public ServerResponseEntity<List<Category>> listCategory(Category category){
        category.setLang(I18nMessage.getLang());
        category.setGrade(1);
        category.setShopId(SecurityUtils.getShopUser().getShopId());
        List<Category> list =  categoryService.categoryLangList(category);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/pageCategory")
    @PreAuthorize("@pms.hasPermission('prod:category:listCategory')")
    @ApiOperation(value = "分页获取一级分类", notes = "分页获取一级分类")
    public ServerResponseEntity<IPage<Category>> pageCategory(PageParam<Category> page, Category category){
        category.setLang(I18nMessage.getLang());
        category.setGrade(1);
        category.setShopId(SecurityUtils.getShopUser().getShopId());
        IPage<Category> categoryPage =  categoryService.pageCategoryLangList(page, category);
        return ServerResponseEntity.success(categoryPage);
    }

    @GetMapping("/platformCategory")
    @ApiOperation(value = "获取平台所有上架的分类", notes = "获取平台所有上架的分类")
    public ServerResponseEntity<List<Category>> listCategory(){
        List<Category> list =  categoryService.platformCategory();
        return ServerResponseEntity.success(list);
    }

    @PostMapping("/signing")
    @ApiOperation(value = "批量签约分类（申请店铺时使用）", notes = "批量签约分类（申请店铺时使用）")
    public ServerResponseEntity<Void> signingCategory(@RequestBody List<CategoryShop> categoryShopList) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        categoryShopService.signingCategory(categoryShopList, shopId, false);
        return ServerResponseEntity.success();
    }

    @GetMapping("/listSigningCategory")
    @ApiOperation(value = "获取签约的分类列表（状态参数为空则返回所有）", notes = "获取签约的分类列表（状态参数为空则返回所有）")
    @ApiImplicitParam(name = "status", value = "签约状态：1：已通过 0待审核 -1未通过")
    public ServerResponseEntity<List<CategoryShopVO>> listByShopId(@RequestParam(value = "status", required = false) Integer status) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        List<CategoryShopVO> categoryShopVOList = categoryShopService.listSigningCategoryByShopId(shopId, I18nMessage.getLang());
        if (Objects.nonNull(status)) {
            categoryShopVOList = categoryShopVOList.stream().filter(item -> Objects.equals(item.getStatus(), status)).collect(Collectors.toList());
        }
        return ServerResponseEntity.success(categoryShopVOList);
    }

    @GetMapping("/listAvailableSigningCategory")
    @ApiModelProperty(value = "获取店铺签约的可用分类列表(发布商品时使用）", notes = "获取店铺签约的可用分类列表(发布商品时使用）")
    public ServerResponseEntity<List<Category>> listAvailableSigningCategory() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        List<Category> categories = categoryShopService.listAvailableSigningCategory(shopId, I18nMessage.getLang());
        return ServerResponseEntity.success(categories);
    }

    @GetMapping("/shopCategory")
    @ApiOperation(value = "获取店铺分类", notes = "获取店铺分类")
    public ServerResponseEntity<List<CategoryVO>> shopCategory(){
        Category category = new Category();
        category.setLang(I18nMessage.getLang());
        category.setGrade(1);
        category.setShopId(SecurityUtils.getShopUser().getShopId());
        List<Category> list =  categoryService.categoryLangList(category);
        List<CategoryVO> categoryVOS = mapperFacade.mapAsList(list, CategoryVO.class);
        return ServerResponseEntity.success(categoryVOS);
    }


    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('prod:category:export')")
    @ApiOperation(value = "导出分类", notes = "导出分类")
    public void export(HttpServletResponse response) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        categoryExcelService.export(response, shopId);
    }

}
