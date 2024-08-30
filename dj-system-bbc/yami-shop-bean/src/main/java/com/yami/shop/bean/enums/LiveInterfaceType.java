/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.enums;

/**
 * 微信直播间接口类型
 * @author lhd
 */
public enum LiveInterfaceType {

    /**
     * 0.创建直播间
     */
    CREATE_ROOM(0,20,10000),
    /**
     * 1.获取直播间列表、获取直播间回放共用次数
     */
    ROOM_LIST(1,100000,100000),
    /**
     * 2.获取直播间回放共用一个接口
     */
    GET_ROOM_BACK(2,100000,100000),
    /**
     * 3.直播间导入商品
     */
    IMPORT_ROOM_PROD(3,100,10000),

    /**
     * 4.商品添加并提审,重新提交审核共用次数
     */
    ADD_PROD_AUDIT(4,10,500),

    /**
     * 5.撤回审核
     */
    CANCEL_PROD_AUDIT(5,5,500),
//    /**
//     * 6.
//     */
//    AGAIN_PROD_AUDIT(6,5,500),
    /**
     * 7.删除商品
     */
    DELETE_PROD(7,10,999),
    /**
     * 8.更新商品
     */
    UPDATE_PROD(8,10,1000),
    /**
     * 9.获取商品状态
     */
    GET_PROD_STATUS(9,10,1000),
    /**
     * 10.获取商品列表
     */
    GET_PROD_LIST(10,10,10000),
    /**
     * 11.设置成员角色
     */
    ADD_LIVE_ROLE(11,10,10000),
    /**
     * 12.解除成员角色
     */
    REMOVE_LIVE_ROLE(12,10,10000),
    /**
     * 13.删除直播间
     */
    REMOVE_LIVE_ROOM(13,10,10000)

    ;
    private Integer num;

    private Integer shopNumLimit;
    private Integer numLimit;

    public Integer value() {
        return num;
    }

    public Integer getNumLimit() {
        return numLimit;
    }

    public Integer getShopNumLimit() {
        return shopNumLimit;
    }

    LiveInterfaceType(Integer num,Integer shopNumLimit,Integer numLimit){
        this.num = num;
        this.shopNumLimit = shopNumLimit;
        this.numLimit = numLimit;
    }

    public static LiveInterfaceType instance(Integer value) {
        LiveInterfaceType[] enums = values();
        for (LiveInterfaceType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
