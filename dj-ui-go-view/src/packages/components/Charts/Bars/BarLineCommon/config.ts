import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarLineCommonConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']
export const seriesItem = {
  type: 'bar',
  barWidth: 15,
  label: {
    show: true,
    position: 'top',
    color: '#fff',
    fontSize: 12
  },
  itemStyle: {
    color: null,
    borderRadius: 2
  },
}
export const linesItem = {
  type: 'line',
  label: {
    show: true,
    position: 'top',
    color: '#fff',
    fontSize: 12
  },
  symbolSize: 5, //设定实心点的大小
  itemStyle: {
    color: null,
    borderRadius: 0
  },
  lineStyle: {
    type: 'solid',
    width: 3,
    color: null
  }
}
export const option = {
  tooltip: {
    show: true,
    trigger: 'axis',
    axisPointer: {
      show: true,
      type: 'shadow'
    }
  },
xAxis: {
    show: true,
    type: 'category',
    axisLine: {
      show: false, //隐藏X轴轴线
    },
    axisTick: {
      show: false, //隐藏X轴刻度
      alignWithLabel: true,
    },
    axisLabel: {
      show: true,
      textStyle: {
        color: "#FFFFFF", //X轴文字颜色
        fontSize: 10,
      },
      margin: 12,
      interval: 0,
    },
  },
  yAxis: {
    show: true,
    type: 'value'
  },
  dataset: { ...dataJson },
  series: [seriesItem, seriesItem,linesItem,linesItem]
}

export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = BarLineCommonConfig.key
  public chartConfig = cloneDeep(BarLineCommonConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
