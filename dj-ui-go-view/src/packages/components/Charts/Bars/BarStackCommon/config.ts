import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarStackCommonConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'
import { graphic } from 'echarts/core'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']
export const seriesItem = {
    type: 'bar',
    stack: '排名',
    label: {
      show: true,
      position: 'top',
      color: '#fff',
      fontSize: 12
    },
    barWidth: 8,
    itemStyle: {
      color: new graphic.LinearGradient(0, 0, 0, 1, [
        {
          offset: 0,
          color: '#3366FF',
        },
        {
          offset: 1,
          color: 'rgba(42, 193, 216, 0)',
        },
      ]),
    },
}
export const seriesItem1 = {
    type: 'bar',
    stack: '排名',
    label: {
      show: true,
      position: 'top',
      color: '#fff',
      fontSize: 12
    },
    barWidth: 16,
    itemStyle: {
      color: new graphic.LinearGradient(0, 0, 0, 1, [
        {
          offset: 0,
          color: 'rgba(241, 173, 84, 1)',
        },
        {
          offset: 1,
          color: 'rgba(241, 173, 84, 0)',
        },
      ]),
    },
}
export const seriesItem2 = {
  type: 'bar',
  stack: '排名',
  label: {
    show: true,
    position: 'top',
    color: '#fff',
    fontSize: 12
  },
  barWidth: 16,
  itemStyle: {
    color: new graphic.LinearGradient(0, 0, 0, 1, [
      {
        offset: 0,
        color: '#00FF99',
      },
      {
        offset: 1,
        color: 'rgba(241, 173, 84, 0)',
      },
    ]),
  },
}
const lineColor = '#224824';
export const option = {
  dataset: { ...dataJson },
  backgroundColor: '#0e1c47',
  tooltip: {
    backgroundColor: 'rgba(1, 13, 19, 0.5)',
    show: true,
    trigger: 'axis',
    borderWidth: 0,
    axisPointer: {
      show: true,
      type: 'shadow'
    },
    textStyle: {
      color: 'rgba(212, 232, 254, 1)',
      fontSize: 12,
    },
  },
  legend: {
    show: true,
    right: '10%',
    top: '5%',
    itemWidth: 8,
    itemHeight: 8,
    textStyle: {
      color: '#fff',
    },
  },
  grid:{
    left: '2%',
    right: '5%',
    bottom: '5%',
    top: '20%',
    containLabel: true,
  },
xAxis: {
    show: true,
    type: 'category',
    // data: datax,
    axisLabel: {
      fontSize: '12',
      color: '#fff',
    },
    axisTick: {
      show: false,
    },
    axisLine: {
      //坐标轴轴线相关设置。数学上的x轴
      show: true,
      lineStyle: {
        color: "#fff",
      },
    }
  },
  yAxis: {
    show: true,
    type: 'value',
    nameTextStyle: {
      color: '#fff',
      fontSize: 12,
    },
    splitLine: {
      lineStyle: {
        color: lineColor,
        type: 'dashed',
      },
    },
    axisLabel: {
      fontSize: '12',
      color: '#fff',
    }
  },
  series: [seriesItem, seriesItem1, seriesItem2]
}
export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = BarStackCommonConfig.key
  public chartConfig = cloneDeep(BarStackCommonConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
