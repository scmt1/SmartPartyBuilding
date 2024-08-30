/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.util;

import com.yami.shop.bean.model.Category;
import com.yami.shop.bean.param.CategoryScoreConfigParam;
import com.yami.shop.bean.param.ScoreConfigParam;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 计算出分类比例
 * @author lhd
 */
public class CategoryScale {

    /**
     * 计算出分类比例
     *
     * @param type 0为使用 1为获取比例
     * @return 返回各分类的比例集合
     *
     */
    public static Map<Long, Double> getScaleByCategory(List<Category> categories, ScoreConfigParam scoreParam, Integer type) {
        Map<Long, Double> categoryMap = new HashMap<>(50);
        //获取分类比例
        List<CategoryScoreConfigParam> categoryConfigs = scoreParam.getCategoryConfigs();
        Map<Long, List<Category>> listMap = categories.stream().collect(Collectors.groupingBy(Category::getParentId));
        Map<Long, Double> categoryMapDb;
        //0为使用 1为获取比例
        if(type == 0){
            categoryMapDb = categoryConfigs.stream().collect(Collectors.toMap(
                CategoryScoreConfigParam::getCategoryId, CategoryScoreConfigParam::getUseScoreLimit));
        }else{
            categoryMapDb = categoryConfigs.stream().collect(Collectors.toMap(
                    CategoryScoreConfigParam::getCategoryId, CategoryScoreConfigParam::getGetScoreLimit));
        }
        //将小类的分类及比例放进map,计算出总的折扣额度
        for (Long categoryId : categoryMapDb.keySet()) {
            List<Category> categoryList = listMap.get(categoryId);
            if(CollectionUtils.isEmpty(categoryList)){
                continue;
            }
            categoryList.forEach(category -> categoryMap.put(category.getCategoryId(), categoryMapDb.get(categoryId)));
            for (Category category : categoryList) {
                List<Category> threeCategoryList = listMap.get(category.getCategoryId());
                if(CollectionUtils.isEmpty(threeCategoryList)){
                    continue;
                }
                threeCategoryList.forEach(threeCategory -> categoryMap.put(threeCategory.getCategoryId(), categoryMapDb.get(categoryId)));
            }

        }
        return categoryMap;
    }


}
