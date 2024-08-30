/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;


/**
 * 用于点与多边形位置关系的判断
 *
 * @author lhd
 * @date 2020-5-22 上午09:59:35
 */
public class GraphUtils {

   public static int TWO = 2;
   /**
    * 判断点是否在多边形内(基本思路是用交点法)
    *
    * @param point
    * @param boundaryPoints
    * @return
    */
   public static boolean isPointInPolygon(BmapPoint point, List<BmapPoint> boundaryPoints) {
      if(CollectionUtils.isEmpty(boundaryPoints) || Objects.isNull(point)){
         return false;
      }
      int pointCount = boundaryPoints.size();

      // 首先判断点是否在多边形的外包矩形内，如果在，则进一步判断，否则返回false
      if (!isPointInRectangle(point, boundaryPoints)) {
         return false;
      }
      // 如果点与多边形的其中一个顶点重合，那么直接返回true
      for (BmapPoint boundaryPoint : boundaryPoints) {
         if (point.equals(boundaryPoint)) {
            return true;
         }
      }
      /*
        基本思想是利用X轴射线法，计算射线与多边形各边的交点，如果是偶数，则点在多边形外，否则在多边形内。还会考虑一些特殊情况，如点在多边形顶点上
        ， 点在多边形边上等特殊情况。
       */
      // X轴射线与多边形的交点数
      int intersectPointCount = 0;
      // X轴射线与多边形的交点权值
      float intersectPointWeights = 0;
      // 浮点类型计算时候与0比较时候的容差
      double precision = 2e-10;
      // 边P1P2的两个端点
      BmapPoint point1 = boundaryPoints.get(0), point2;
      // 循环判断所有的边
      for (int i = 1; i <= pointCount; i++) {
         point2 = boundaryPoints.get(i % pointCount);

         /*
           如果点的y坐标在边P1P2的y坐标开区间范围之外，那么不相交。
          */
         if (point.getLat() < Math.min(point1.getLat(), point2.getLat())
                 || point.getLat() > Math.max(point1.getLat(), point2.getLat())) {
            point1 = point2;
            continue;
         }

         /*
           此处判断射线与边相交
          */
         // 如果点的y坐标在边P1P2的y坐标开区间内
         if (point.getLat() > Math.min(point1.getLat(), point2.getLat())
                 && point.getLat() < Math.max(point1.getLat(), point2.getLat())) {
            // 若边P1P2是垂直的
            if (point1.getLng() == point2.getLng()) {
               if (point.getLng() == point1.getLng()) {
                  // 若点在垂直的边P1P2上，则点在多边形内
                  return true;
               } else if (point.getLng() < point1.getLng()) {
                  // 若点在在垂直的边P1P2左边，则点与该边必然有交点
                  ++intersectPointCount;
               }
            } else {
               // 若边P1P2是斜线
               // 点point的x坐标在点P1和P2的左侧
               if (point.getLng() <= Math.min(point1.getLng(), point2.getLng())) {
                  ++intersectPointCount;
                  // 点point的x坐标在点P1和P2的x坐标中间
               } else if (point.getLng() > Math.min(point1.getLng(), point2.getLng())
                       && point.getLng() < Math.max(point1.getLng(), point2.getLng())) {
                  double slopeDiff = 0.0d;
                  if (point1.getLat() > point2.getLat()) {
                     slopeDiff = (point.getLat() - point2.getLat()) / (point.getLng() - point2.getLng())
                             - (point1.getLat() - point2.getLat()) / (point1.getLng() - point2.getLng());
                  } else {
                     slopeDiff = (point.getLat() - point1.getLat()) / (point.getLng() - point1.getLng())
                             - (point2.getLat() - point1.getLat()) / (point2.getLng() - point1.getLng());
                  }
                  if (slopeDiff > 0) {
                     // 由于double精度在计算时会有损失，故匹配一定的容差。经试验，坐标经度可以达到0.0001
                     if (slopeDiff < precision) {
                        // 点在斜线P1P2上
                        return true;
                     } else {
                        // 点与斜线P1P2有交点
                        intersectPointCount++;
                     }
                  }
               }
            }
         } else {
            // 边P1P2水平
            if (point1.getLat() == point2.getLat()) {
               if (point.getLng() <= Math.max(point1.getLng(), point2.getLng())
                       && point.getLng() >= Math.min(point1.getLng(), point2.getLng())) {
                  // 若点在水平的边P1P2上，则点在多边形内
                  return true;
               }
            }
            // 判断点通过多边形顶点
            boolean res = ((point.getLat() == point1.getLat() && point.getLng() < point1.getLng()))
                    || (point.getLat() == point2.getLat() && point.getLng() < point2.getLng());
            if (res) {
               if (point2.getLat() < point1.getLat()) {
                  intersectPointWeights += -0.5;
               } else if (point2.getLat() > point1.getLat()) {
                  intersectPointWeights += 0.5;
               }
            }
         }
         point1 = point2;
      }
      // 偶数在多边形外
      // 奇数在多边形内
      return (intersectPointCount + Math.abs(intersectPointWeights)) % TWO != 0;
   }

   /**
    * 判断点是否在矩形内在矩形边界上，也算在矩形内(根据这些点，构造一个外包矩形)
    *
    * @param point
    *            点对象
    * @param boundaryPoints
    *            矩形边界点
    * @return
    */
   public static boolean isPointInRectangle(BmapPoint point, List<BmapPoint> boundaryPoints) {
      // 西南角点
      BmapPoint southWestPoint = getSouthWestPoint(boundaryPoints);
      // 东北角点
      BmapPoint northEastPoint = getNorthEastPoint(boundaryPoints);
      return (point.getLng() >= southWestPoint.getLng() && point.getLng() <= northEastPoint.getLng()
              && point.getLat() >= southWestPoint.getLat() && point.getLat() <= northEastPoint.getLat());

   }

   /**
    * 根据这组坐标，画一个矩形，然后得到这个矩形西南角的顶点坐标
    *
    * @param vertexs
    * @return
    */
   private static BmapPoint getSouthWestPoint(List<BmapPoint> vertexs) {
      double minLng = vertexs.get(0).getLng(), minLat = vertexs.get(0).getLat();
      for (BmapPoint bmapPoint : vertexs) {
         double lng = bmapPoint.getLng();
         double lat = bmapPoint.getLat();
         if (lng < minLng) {
            minLng = lng;
         }
         if (lat < minLat) {
            minLat = lat;
         }
      }
      return new BmapPoint(minLng, minLat);
   }

   /**
    * 根据这组坐标，画一个矩形，然后得到这个矩形东北角的顶点坐标
    *
    * @param vertexs
    * @return
    */
   private static BmapPoint getNorthEastPoint(List<BmapPoint> vertexs) {
      double maxLng = 0.0d, maxLat = 0.0d;
      for (BmapPoint bmapPoint : vertexs) {
         double lng = bmapPoint.getLng();
         double lat = bmapPoint.getLat();
         if (lng > maxLng) {
            maxLng = lng;
         }
         if (lat > maxLat) {
            maxLat = lat;
         }
      }
      return new BmapPoint(maxLng, maxLat);
   }

}
