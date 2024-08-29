import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarBeamAConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']

export const option = {
  dataset: { ...dataJson },
  backgroundColor: 'rgb(8,25,34)',
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(7,55,63,0.7)',
    borderColor: 'rgba(7,55,63,0.7)',
    textStyle: {
      color: '#fff',
    },
    formatter: function (params: any[]) {
      return params[0].marker + '' + params[0].name + ': ' + params[0].value;
    },
  },
  grid: {
    bottom: '20%',
    right: 0,
    left: 0,
  },
  xAxis: {
    data: [],
    axisTick: {
      show: false,
    },
    axisLine: {
      show: false,
    },
    axisLabel: {
      show: true,
      textStyle: {
        color: '#fff',
      },
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
      show: false,
    },
  },
  series: [
    {
      name: '权属情况',
      type: 'effectScatter',
      rippleEffect: {
        period: 1,
        scale: 2,
        brushType: 'fill',
      },
      symbolSize: [60, 25],
      symbolOffset: [0, 10],
      z: 12,
      itemStyle: {
        normal: {
          color: '#14b1eb',
          opacity: 0.5,
        },
      },
      data: [],
    },
    {
      name: '',
      type: 'pictorialBar',
      symbolSize: [80, 30],
      symbolOffset: [0, -2],
      z: 10,
      itemStyle: {
        normal: {
          color: 'transparent',
          borderColor: '#14b1eb',
          borderType: 'solid',
          borderWidth: 5,
        },
      },
      data: [],
    },
    {
      name: '',
      type: 'pictorialBar',
      symbolSize: [60, 25],
      symbolOffset: [0, -5],
      z: 10,
      itemStyle: {
        normal: {
          color: 'transparent',
          borderColor: '#14b1eb',
          borderType: 'solid',
          borderWidth: 5,
        },
      },
      data: [],
    },
    {
      name: '',
      type: 'pictorialBar',
      symbolSize: [50, 10],
      symbolOffset: [0, -5],
      z: 12,
      itemStyle: {
        normal: {
          color: 'rgb(9,227,255)',
        },
      },
      data: [],
    },
    {
      type: 'bar',
      itemStyle: {
        normal: {
          color: {
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            type: 'linear',
            global: false,
            colorStops: [
              {
                offset: 0,
                color: 'rgb(24,118,158)',
              },
              {
                offset: 1,
                color: 'rgba(9,30,41,0.5)', //底部渐变颜色
              },
            ],
          },
        },
      },
      silent: true,
      barWidth: 60,
      barGap: '-100%', // Make series be overlap
      data: [],
      label: {
        show: true,
        position: 'top',
        distance: 15,
        color: '#fff',
        fontSize: 12,
        formatter: (params: any) => {
          return '{a|' + params.value + '}';
        },
        rich: {
          a: {
            color: '#03adff',
            fontSize: 18,
            fontWeight: 'bold',
          }
        },
      },
    },
  ],
}
export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = BarBeamAConfig.key
  public chartConfig = cloneDeep(BarBeamAConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
