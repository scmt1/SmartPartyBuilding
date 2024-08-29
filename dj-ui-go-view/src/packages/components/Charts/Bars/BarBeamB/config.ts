import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarBeamBConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']

var xData2 = dataJson.dimensions;
var data1 = dataJson.source;
var barWidth = 30;
export const option = {
  dataset: { ...dataJson },
  backgroundColor: '#021132',
  tooltip: {
    trigger: 'item',
  },
  grid: {
    left: 100,
    bottom: 100,
  },
  xAxis: {
    data: xData2,
    axisTick: {
      show: false,
    },
    axisLine: {
      show: false,
    },
    axisLabel: {
      interval: 0,
      textStyle: {
        color: '#fff',
        fontSize: 16,
      },
      margin: 26, //刻度标签与轴线之间的距离。
    },
  },
  yAxis: {
    splitLine: {
      show: false,
    },
    axisTick: {
      show: false,
    },
    axisLine: {
      show: false,
    },
    axisLabel: {
      textStyle: {
        color: '#fff',
        fontSize: 16,
      },
    },
  },
  series: [
    {
      // 上半截柱子
      type: 'bar',
      barWidth: barWidth,
      barGap: '-100%',
      z: 0,
      itemStyle: {
        color: '#163F7A',
        opacity: 0.3,
      },
      tooltip: {
        trigger: 'none'
      },
      emphasis: {
        itemStyle: {
          color: '#163F7A'
        }
      },
      data: new Array(xData2.length).fill(100),
    },
    {
      //下半截柱子
      name: '',
      type: 'bar',
      barWidth: barWidth,
      barGap: '-100%',
      itemStyle: {
        //lenged文本
        opacity: 1,
        fontSize: 14
      },
      data: data1,
    },
    {
      // 替代柱状图 默认不显示颜色，是最下方柱图（邮件营销）的value值 - 20
      type: 'bar',
      barWidth: barWidth,
      barGap: '-100%',
      stack: '广告',
      itemStyle: {
        color: 'transparent',
      },
      data: data1,
    },
    {
      //上半截柱子顶部圆片
      name: '',
      type: 'pictorialBar',
      symbolSize: [barWidth, 16],
      symbolOffset: [0, -8],
      z: 12,
      symbolPosition: 'end',
      itemStyle: {
        color: '#163F7A',
        opacity: 1,
      },
      tooltip: {
        trigger: 'none'
      },
      emphasis: {
        itemStyle: {
          color: '#163F7A'
        }
      },
      data: new Array(xData2.length).fill(100),
    },
    {
      //下半截柱子顶部圆片
      name: '',
      type: 'pictorialBar',
      symbolSize: [barWidth, 16],
      symbolOffset: [0, -8],
      z: 12,
      itemStyle: {
        opacity: 1,
        color: 'rgb(26, 158, 230)',
      },
      symbolPosition: 'end',
      data: data1,
    },
    {
      //下半截柱子底部圆片
      name: '',
      type: 'pictorialBar',
      symbolSize: [barWidth, 16],
      symbolOffset: [0, 8],
      z: 12,
      itemStyle: {
        opacity: 1,
        color: 'rgb(23, 97, 222)',
      },
      data: new Array(xData2.length).fill(1),
    },
  ],
}
export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = BarBeamBConfig.key
  public chartConfig = cloneDeep(BarBeamBConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
