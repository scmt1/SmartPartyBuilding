import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { LineOneLightConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import { graphic } from 'echarts/core'
import { defaultTheme, chartColorsSearch } from '@/settings/chartThemes/index'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'
import * as echarts from 'echarts'
export const includes = ['legend', 'xAxis', 'yAxis', 'grid']

export const option = {
  dataset: { ...dataJson },
  backgroundColor: "#001829",
  //轮播属性
  dataZoom: [
    {
      xAxisIndex: 0,// 这里是从X轴的0刻度开始
      show: false, // 是否显示滑动条，不影响使用
      type: "slider", // 这个 dataZoom 组件是 slider 型 dataZoom 组件
      startValue: 0, // 从头开始。
      endValue: 5, // 一次性展示多少个。
    }
  ],
  grid: {
    top: "15%",
    left: "1%",
    right: "1%",
    bottom: "1%",
    containLabel: true,
  },
  tooltip: {
    show: true,
    trigger: 'axis',
    axisPointer: {
      type: 'line'
    }
  },
  xAxis: {
    // data: xaxisData,
    type: 'category',
    boundaryGap: true,
    axisLine: {
      show: true,
      symbol: ["none", "rect"],
      symbolSize: [6, 12],
      lineStyle: {
        width: 2,
        color: "#537DAA",
      },
    },
    axisTick: {
      show: false,
    },
  },
  yAxis: {
    type: 'value',
    nameTextStyle: {
      color: "#AEC9FF",
      fontWeight: 400,
      fontSize: 14,
    },
    axisLabel: {
      color: "#AEC9FF",
    },
    axisLine: {
      show: true,
      symbol: ["none", "rect"],
      symbolSize: [6, 12],
      lineStyle: {
        width: 2,
        color: "#537DAA",
      },
    },
    axisTick: {
      show: false,
    },
    splitLine: {
      show: true,
      lineStyle: {
        color: "rgba(83, 125, 170, 0.2)",
      },
    },
  },
  series: [
    {
      type: "line",
      // smooth: true,
      label: {
        show: true,
        position: 'top',
        color: '#fff',
        fontSize: 12
      },
      lineWidth: 1.2,
      // data: yaxisData,
      symbol: "emptyCircle",
      // symbol: "none",
      itemStyle: {
        color: "rgba(114, 178, 255, 1)",
      },
      lineStyle: {
        color: "",
        width: 5,
        shadowBlur: 20,
        shadowColor: "#72B2FF",
        shadowOffsetY: 10,
      },

      areaStyle: {
        opacity: 0.8,
        // color: new graphic.LinearGradient(0, 0, 0, 1, [
        //   {
        //     offset: 0,
        //     color: chartColorsSearch[defaultTheme][3]
        //   },
        //   {
        //     offset: 1,
        //     color: 'rgba(0,0,0,0)'
        //   }
        // ])
        color: ""
      }
    },
    {
      type: "lines",
      coordinateSystem: "cartesian2d",
      data: [{
        coords: dataJson.coords
      }],
      zlevel: 1,
      polyline: true,
      symbol: "circle",
      effect:
          {
            show: true,
            trailLength: 0.4,
            symbol: "circle",
            period: 8,
            symbolSize: 8
          },
      lineStyle:
          {
            normal:
                {
                  color: '#64FFFF',
                  width: 0,
                  opacity: 0,
                  curveness: 0
                }
          }
    },
  ],
};


export default class Config extends PublicConfigClass implements CreateComponentType {
  public key: string = LineOneLightConfig.key
  public chartConfig = cloneDeep(LineOneLightConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
