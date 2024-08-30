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

import lombok.Data;

import java.util.List;

/**
 * @author Yami
 */
@Data
public class SankeyDto {

    private List<Node> node;
    private List<Edge> edge;
    private Graph[] graph;


    /**
     * Node的索引为 Edge 的id
     */
    @Data
    public static class Node{
        private String name;
        private Integer level;
    }
    /**
     * 模块上下级连接
     */
    @Data
    public static class Edge{
        //上一级id
        private Integer startNode;
        //下一级id
        private Integer endNode;

    }
    /**
     * 存放NodeProperty 和 EdgeProperty 的对象
     */
    @Data
    public static class Graph{
        private List<NodeProperty> nodeProperty;
        private List<EdgeProperty> edgeProperty;
    }
    /**
     * 设置Node显示的信息
     */
    @Data
    public static class NodeProperty{
        private Integer id;
        private Integer value;
    }
    /**
     * 设置上下级连接的值
     */
    @Data
    public static class EdgeProperty{
        private Integer id;
        private Integer value;
    }
}
