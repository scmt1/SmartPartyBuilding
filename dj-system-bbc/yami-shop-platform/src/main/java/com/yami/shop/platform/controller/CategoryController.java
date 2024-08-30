/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.app.dto.CategoryDto;
import com.yami.shop.bean.dto.CategoryShopDTO;
import com.yami.shop.bean.enums.EsOperationType;
import com.yami.shop.bean.event.EsProductUpdateEvent;
import com.yami.shop.bean.model.Category;
import com.yami.shop.bean.model.CategoryShop;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.param.ProductExportParam;
import com.yami.shop.bean.vo.CategoryShopVO;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.i18n.LanguageEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.platform.config.PlatformConstant;
import com.yami.shop.service.CategoryExcelService;
import com.yami.shop.service.CategoryService;
import com.yami.shop.service.CategoryShopService;
import com.yami.shop.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
@Api(tags="分类相关接口")
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
    @ApiOperation(value = "获取分类信息", notes = "获取分类信息")
    @ApiImplicitParam(name = "categoryId", value = "分类id")
    public ServerResponseEntity<Category> info(@PathVariable("categoryId") Long categoryId){
        Category category = categoryService.getCategoryByCategoryId(categoryId);

        return ServerResponseEntity.success(category);
    }

    @SysLog("保存分类")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('prod:category:save')")
    @ApiOperation(value = "保存分类", notes = "保存分类")
    public ServerResponseEntity<Void> save(@RequestBody Category category){
        category.setShopId(Constant.PLATFORM_SHOP_ID);
        category.setRecTime(new Date());
//        int count = categoryService.count(new LambdaQueryWrapper<Category>().eq(Category::getCategoryName, category.getCategoryName())
//                .eq(Category::getParentId, category.getParentId()).eq(Category::getShopId,Constant.PLATFORM_SHOP_ID))
        Integer count = categoryService.getCategoryName(category);
        if(count > 0){
            // 类目名称已存在
            throw new YamiShopBindException(I18nMessage.getMessage("yami.category.name.exist"));
        }
        category.setGrade(getGradeByParentId(category.getParentId()));
        category.setSuperiorId(-1L);
        // 获取上级的上级
        if(category.getGrade() == PlatformConstant.MAX_CATEGORY_GRADE) {
            Long superiorId = categoryService.getParentCategoryByParentId(category.getParentId());
            category.setSuperiorId(superiorId);
        }
        categoryService.saveCategroy(category);
        // 清除缓存
        removeCategoryCacheByParentId(category);
        return ServerResponseEntity.success();
    }

    @SysLog("更新分类")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('prod:category:update')")
    @ApiOperation(value = "更新分类", notes = "更新分类")
    public ServerResponseEntity<String> update(@RequestBody Category category){
        category.setShopId(Constant.PLATFORM_SHOP_ID);
        Category categoryDb = categoryService.getCategoryByCategoryId(category.getCategoryId());
        if (!Objects.equals(category.getGrade(), categoryDb.getGrade())) {
            throw new YamiShopBindException("不能改变分类层级");
        }

        Integer count = categoryService.getCategoryName(category);
        if(count > 0){
            // 类目名称已存在
            throw new YamiShopBindException(I18nMessage.getMessage("yami.category.name.exist"));
        }
        // 如果从下线改成正常，则需要判断上级的状态
        if (Objects.equals(categoryDb.getStatus(),0) && Objects.equals(category.getStatus(),1) && !Objects.equals(category.getParentId(),0L)){
            Category parentCategory = categoryService.getOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryId, category.getParentId()));
            if(Objects.isNull(parentCategory) || Objects.equals(parentCategory.getStatus(),0)){
                // 修改失败，上级分类不存在或者不为正常状态
                throw new YamiShopBindException("yami.category.status.check");
            }
        }
        category.setGrade(getGradeByParentId(category.getParentId()));
        category.setOldCategoryName(categoryDb.getCategoryName());
        category.setOldCategoryNameEn(categoryDb.getCategoryNameEn());
        category.setSuperiorId(-1L);
        // 获取上级的上级
        if(category.getGrade() == PlatformConstant.MAX_CATEGORY_GRADE) {
            Long superiorId = categoryService.getParentCategoryByParentId(category.getParentId());
            category.setSuperiorId(superiorId);
        }
        categoryService.updateCategroy(category);
        // 清除缓存
        removeCategoryCacheByParentId(category);
        eventPublisher.publishEvent(new EsProductUpdateEvent(category.getCategoryId(), null, EsOperationType.UPDATE_BY_CATEGORY_ID));
        return ServerResponseEntity.success();
    }

    @SysLog("删除分类")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("@pms.hasPermission('prod:category:delete')")
    @ApiOperation(value = "删除分类", notes = "删除分类")
    @ApiImplicitParam(name = "categoryId", value = "分类id")
    public ServerResponseEntity<String> delete(@PathVariable("categoryId") Long categoryId){
        if (categoryService.count(new LambdaQueryWrapper<Category>().eq(Category::getParentId,categoryId)) >0) {
            // 请删除子分类，再删除该分类
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.category.delete.child"));
        }
        int categoryProdCount = productService.count(new LambdaQueryWrapper<Product>().eq(Product::getCategoryId, categoryId).ne(Product::getStatus, -1));
        if (categoryProdCount>0){
            // 该分类下还有商品，请先删除该分类下的商品
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.category.delete.check"));
        }
        Category category = categoryService.getById(categoryId);
        category.setSuperiorId(-1L);
        // 获取上级的上级
        if(category.getGrade() == PlatformConstant.MAX_CATEGORY_GRADE) {
            Long superiorId = categoryService.getParentCategoryByParentId(category.getParentId());
            category.setSuperiorId(superiorId);
        }
        categoryService.deleteCategroy(category);
        // 清除缓存
        removeCategoryCacheByParentId(category);
        return ServerResponseEntity.success();
    }

    @GetMapping("/listCategory")
    @ApiOperation(value = "获取全部分类", notes = "获取全部分类")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "maxGrade", value = "0：一级分类，1：二级分类，2：三级分类(后面的包含前面等级的分类)"),
            @ApiImplicitParam(name = "status", value = "默认是1，表示正常状态,0为下线状态")
    })
    public ServerResponseEntity<List<Category>> listCategory(@RequestParam(value = "maxGrade", required = false, defaultValue = "2") Integer maxGrade,
                                                       @RequestParam(value = "status", required = false) Integer status) {

        List<Category> categories =  categoryService.listByLang(I18nMessage.getLang(),maxGrade,null,status,Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success(categories);
    }

    @GetMapping("/listCategoryByGrade")
    @ApiOperation(value = "根据等级与状态获取平台分类列表", notes = "根据等级与状态获取平台分类列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "grade", value = "0：一级分类，1：二级分类，2：三级分类"),
            @ApiImplicitParam(name = "status", value = "默认是1，表示正常状态,0为下线状态")
    })
    public ServerResponseEntity<List<Category>> listCategoryByGrade(@RequestParam(value = "grade", defaultValue = "2") Integer grade, @RequestParam(value = "status", required = false) Integer status) {
        List<Category> categories = categoryService.listByGrade(I18nMessage.getLang(), grade, status, Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success(categories);
    }

    @GetMapping("/upAndCurrCategoryList/{categoryId}")
    @ApiOperation(value = "获取上架分类和当前选中分类的父类", notes = "获取上架分类和当前选中分类的父类")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "maxGrade", value = "0：一级分类，1：二级分类，2：三级分类"),
            @ApiImplicitParam(name = "categoryId", value = "分类id")
    })
    public ServerResponseEntity<List<Category>> upAndCurrCategoryList(
            @RequestParam(value = "maxGrade", required = false, defaultValue = "2") Integer maxGrade,
            @PathVariable("categoryId") Long categoryId){
        Category category = new Category();
        category.setLang(I18nMessage.getDbLang());
        category.setStatus(1);
        category.setShopId(Constant.PLATFORM_SHOP_ID);
        category.setGrade(maxGrade);
        //获取上架的分类
        List<Category> upList = categoryService.categoryList(category);

        //如果是新增的，直接返回上架的分类即可
        if (categoryId==0){
            return ServerResponseEntity.success(upList);
        }
        Category currCategory = categoryService.getCategoryByCategoryId(categoryId);
        if (currCategory == null) {
            return ServerResponseEntity.success(upList);
        }
        while (currCategory.getParentId() != 0) {
            currCategory=categoryService.getCategoryByCategoryId(currCategory.getParentId());
            if (!Objects.equals(currCategory.getStatus(), 1)) {
                upList.add(currCategory);
            }
        }
        return ServerResponseEntity.success(upList);
    }

    private int getGradeByParentId(Long parentId) {
        // 如果上级为id为0，则设置分类等级为0
        if (Objects.equals(parentId,0L)) {
            return 0;
        }
        Category parentCategory = categoryService.getById(parentId);
        return parentCategory.getGrade() + 1;
    }

    @GetMapping("/platformCategory")
    @ApiOperation(value = "平台可用分类-必须是启用分类且分类下包含启动的三级分类", notes = "平台可用分类-必须是启用分类且分类下包含启动的三级分类")
    public ServerResponseEntity<List<Category>> platformCategory(){
        List<Category> list =  categoryService.platformCategory();
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/signingInfoByShopId")
    @ApiOperation(value = "获取签约的分类列表（状态参数为空则返回所有）", notes = "获取签约的分类列表（状态参数为空则返回所有）")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "shopId", value = "店铺id"),
            @ApiImplicitParam(name = "status", value = "签约状态：1：已通过 0待审核 -1未通过")
    })
    public ServerResponseEntity<List<CategoryShopVO>> listSigningByShopId(@RequestParam(value = "shopId") Long shopId, @RequestParam(value = "status", required = false) Integer status) {
        List<CategoryShopVO> categoryShopList = categoryShopService.listSigningCategoryByShopId(shopId, I18nMessage.getLang());
        if (Objects.nonNull(status)) {
            categoryShopList = categoryShopList.stream().filter(item -> Objects.equals(item.getStatus(), status)).collect(Collectors.toList());
        }
        return ServerResponseEntity.success(categoryShopList);
    }

    @GetMapping("/pageSigningInfo")
    @ApiOperation(value = "分页获取签约的分类列表", notes = "分页获取签约的分类列表")
    public ServerResponseEntity<IPage<CategoryShopVO>> pageSigningInfo(PageParam<CategoryShopVO> page, CategoryShopDTO categoryShop) {
        IPage<CategoryShopVO> categoryShopPage = categoryShopService.pageSigningInfo(page, categoryShop);
        return ServerResponseEntity.success(categoryShopPage);
    }

    @PutMapping("/signing")
    @ApiOperation(value = "更新店铺签约分类", notes = "更新店铺签约分类")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<Void> signing(@Valid @RequestBody List<CategoryShop> categoryShopDTOList, @RequestParam(value = "shopId") Long shopId) {
        categoryShopService.signingCategory(categoryShopDTOList, shopId, true);
        return ServerResponseEntity.success();
    }

    private void removeCategoryCacheByParentId(Category category) {
        categoryService.removeListRateCache();
        categoryService.removeCacheByParentIdAndLang(category.getParentId(), category.getShopId(), LanguageEnum.LANGUAGE_ZH_CN.getLang());
        categoryService.removeCacheByParentIdAndLang(category.getParentId(), category.getShopId(), LanguageEnum.LANGUAGE_EN.getLang());
        categoryService.removeCacheByParentIdAndLang(category.getSuperiorId(), category.getShopId(), LanguageEnum.LANGUAGE_ZH_CN.getLang());
        categoryService.removeCacheByParentIdAndLang(category.getSuperiorId(), category.getShopId(), LanguageEnum.LANGUAGE_EN.getLang());
        // 如果是2/3级分类，第一分类也需要清空缓存数据
        if(category.getGrade() == 1 || category.getGrade() == PlatformConstant.MAX_CATEGORY_GRADE) {
            categoryService.removeCacheByParentIdAndLang(Constant.CATEGORY_ID, Constant.PLATFORM_SHOP_ID, LanguageEnum.LANGUAGE_ZH_CN.getLang());
            categoryService.removeCacheByParentIdAndLang(Constant.CATEGORY_ID, Constant.PLATFORM_SHOP_ID, LanguageEnum.LANGUAGE_EN.getLang());
        }
    }

    @GetMapping("/categoryInfo")
    @ApiOperation(value = "分类信息列表", notes = "获取所有的产品分类信息，顶级分类的parentId为0,默认为顶级分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "分类ID", dataType = "Long"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", dataType = "Long")
    })
    public ServerResponseEntity<List<CategoryDto>> categoryInfo(@RequestParam(value = "parentId", defaultValue = "0") Long parentId,
                                                          @RequestParam(value = "shopId", defaultValue = "0") Long shopId) {
        List<Category> categories = categoryService.listByParentIdAndShopId(parentId, shopId, I18nMessage.getDbLang());
        List<CategoryDto> categoryDtos = mapperFacade.mapAsList(categories, CategoryDto.class);
        return ServerResponseEntity.success(categoryDtos);
    }

    @GetMapping("/getCategoryAndParent")
    @ApiOperation(value = "获取平台分类及所有上级分类", notes = "获取平台分类及所有上级分类")
    public ServerResponseEntity<List<Category>> getCategoryAndParent(@RequestParam(value = "categoryId") Long categoryId){
        List<Category> categories = categoryService.getCategoryAndParent(categoryId);
        return ServerResponseEntity.success(categories);
    }

    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('prod:prod:exportProd')")
    @ApiOperation(value = "导出分类", notes = "导出分类")
    public void export(HttpServletResponse response) {
        categoryExcelService.export(response, Constant.PLATFORM_SHOP_ID);
    }
}
