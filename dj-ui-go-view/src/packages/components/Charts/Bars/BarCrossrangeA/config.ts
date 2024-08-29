import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarCrossrangeAConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import { graphic } from 'echarts/core'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']

let yLabel = dataJson.dimensions;
var getmyd = dataJson.source;
export const option = {
  dataset: { ...dataJson },
  tooltip: {},
  backgroundColor: '#0f375f',
  animation: false,
  grid: {
    top: '25%',
    bottom: '10%', //也可设置left和right设置距离来控制图表的大小
  },
  xAxis: [
    {
      axisLabel: {
        color: '#fff',
        textStyle: {
          fontSize: '13',
        },
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'dashed',
          color: 'rgba(255,255,255,0.2)',
        },
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: '#fff',
          width: 1,
          opacity: 0.3,
        },
      },
      axisTick: {
        show: false,
      },
    },
  ],
  yAxis: [
    {
      type: 'category',
      inverse: true,
      splitLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      axisLine: {
        lineStyle: {
          // color: '#fff',
          color: new graphic.LinearGradient(0, 1,1, 0, [
            {
              offset: 0,
              color: 'rgba(255,255,255,0.8)',
            },
            {
              offset: 1,
              color: 'rgba(255,255,255,0)',
            },
          ]),
          width: 1,
        },
      },
      axisLabel: {
        color: '#fff',
        textStyle: {
          fontSize: '13',
        },
      },
      data: yLabel,
    },
  ],
  series: [
    {
      name: '值',
      type: 'bar',
      xAxisIndex: 0,
      itemStyle: {
        normal: {
          color: {
            colorStops: [
              {
                offset: 0,
                color: 'rgba(0, 121, 255, 1)', // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0, 202, 220, 1)', // 100% 处的颜色
              },
            ],
          },
        },
      },
      barWidth: 15,
      data: getmyd,
      z: 0,
    },
    {
      // 分隔
      type: 'pictorialBar',
      itemStyle: {
        normal: {
          color: '#022539',
        },
      },
      symbolRepeat: 'fixed',
      symbolMargin: 4,
      symbol: 'rect',
      symbolClip: true,
      symbolSize: [2, 15],
      symbolPosition: 'start',
      symbolOffset: [-1, 0],
      data: getmyd,
      z: 66,
      animationEasing: 'elasticOut',
    },
  ],
  //轮播属性
  dataZoom: [
    {
      yAxisIndex: 0,// 这里是从X轴的0刻度开始
      show: false, // 是否显示滑动条，不影响使用
      type: "slider", // 这个 dataZoom 组件是 slider 型 dataZoom 组件
      startValue: 0, // 从头开始。
      endValue: 4, // 一次性展示多少个。
    }
  ]
}

export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = BarCrossrangeAConfig.key
  public chartConfig = cloneDeep(BarCrossrangeAConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
