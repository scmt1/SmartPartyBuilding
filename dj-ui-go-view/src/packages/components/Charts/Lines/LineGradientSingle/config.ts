import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { LineGradientSingleConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import { graphic } from 'echarts/core'
import { defaultTheme, chartColorsSearch } from '@/settings/chartThemes/index'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']

const options = {
  tooltip: {
    show: true,
    trigger: 'axis',
    axisPointer: {
      type: 'line'
    }
  },
  xAxis: {
    show: true,
    type: 'category'
  },
  yAxis: {
    show: true,
    type: 'value'
  },
  dataset: { ...dataJson },
  series: [
    {
      type: 'line',
      smooth: false,
      symbolSize: 5, //设定实心点的大小
      label: {
        show: true,
        position: 'top',
        color: '#fff',
        fontSize: 12
      },
      lineStyle: {
        type: 'solid',
        width: 3
      },
      areaStyle: {
        opacity: 0.8,
        color: new graphic.LinearGradient(0, 0, 0, 1, [
          {
            offset: 0,
            color: chartColorsSearch[defaultTheme][3]
          },
          {
            offset: 1,
            color: 'rgba(0,0,0,0)'
          }
        ])
      }
    }
  ],
  //轮播属性
  dataZoom: [
    {
      xAxisIndex: 0,// 这里是从X轴的0刻度开始
      show: false, // 是否显示滑动条，不影响使用
      type: "slider", // 这个 dataZoom 组件是 slider 型 dataZoom 组件
      startValue: 0, // 从头开始。
      endValue: 5, // 一次性展示多少个。
    }
  ]
}

export default class Config extends PublicConfigClass implements CreateComponentType {
  public key: string = LineGradientSingleConfig.key
  public chartConfig = cloneDeep(LineGradientSingleConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(options, includes)
}
