import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarDoubleConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']
var
    barWidth = 20,
    color1 = {
      x: 0,
      y: 0,
      x2: 0,
      y2: 1,
      type: 'linear',
      global: false,
      colorStops: [
        {
          //第一节下面
          offset: 0,
          color: 'rgba(32, 170, 146,0.8)',
        },
        {
          offset: 1,
          color: 'rgba(20, 76, 41,1)',
        },
      ],
    },
    color2 = {
      x: 0,
      y: 0,
      x2: 0,
      y2: 1,
      type: 'linear',
      global: false,
      colorStops: [
        {
          //第一节下面
          offset: 0,
          color: 'rgba(5, 164, 224,0.8)',
        },
        {
          offset: 1,
          color: 'rgba(16, 57, 89,1)',
        },
      ],
    };

const option = {
    dataset: dataJson,
  backgroundColor: 'rgb(6, 29, 43)', //背景色
  legend: {
    data: ['A', 'B'],
    show:true,
    icon: 'rect',
    top: 'top',
    textStyle: {
      color: '#fff',
      fontWeight: 'normal',
      fontSize: 12,
    },
  },
  //提示框
  tooltip: {
    show: true,
    trigger: 'axis',
    axisPointer: {
      type: 'shadow',
    },
  },
  grid: {
    top: '25%',
    left: '5%',
    bottom: '10%',
    right: '5%',
    containLabel: true,
  },
  animation: true,
  xAxis: [
    {
      type: 'category',
      axisTick: {
        show: false,
      },
      axisLine: {
        show: true,
        lineStyle: {
          color: 'rgba(0,228,255,1)',
        },
      },
      axisLabel: {
        inside: false,
        textStyle: {
          color: '#fff',
          fontWeight: 'normal',
          fontSize: 12,
        },
        margin: 20, //刻度标签与轴线之间的距离。
      },
      data: dataJson.xData,
    },
    {
      type: 'category',
      axisLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      axisLabel: {
        show: false,
      },
      splitArea: {
        show: false,
      },
      splitLine: {
        show: false,
      },
      data: dataJson.xData,
    },
  ],
  yAxis: [
    {
      show: true,
      type: 'value',
      axisLabel: {
        textStyle: {
          color: '#fff',
        },
      },
      splitLine: {
        show: false,
      },
      axisLine: {
        show: true,
        lineStyle: {
          color: 'rgba(0,228,255,1)',
        },
      },
    },
  ],
  // dataZoom: [
  //   {
  //     xAxisIndex: 0,// 这里是从X轴的0刻度开始
  //     show: true, // 是否显示滑动条，不影响使用
  //     type: "slider", // 这个 dataZoom 组件是 slider 型 dataZoom 组件
  //     startValue: 1, // 从头开始。
  //     endValue: 5, // 一次性展示多少个。
  //   }
  // ],
  series: [
    {
      name: '内部柱子顶部1',
      type: 'pictorialBar',
      tooltip: { show: false },
      symbolSize: [barWidth, 10],
      symbolOffset: ['-81%', -5],
      symbolPosition: 'end',
      z: 15,
      color: '#2DCEB1E5',
      zlevel: 2,
      data: dataJson.yData1,
    },
    {
      name: '内部柱子顶部2',
      type: 'pictorialBar',
      tooltip: { show: false },
      symbolSize: [barWidth, 10],
      symbolOffset: ['81%', -5],
      symbolPosition: 'end',
      z: 15,
      color: '#02AFF9FF',
      zlevel: 2,
      data: dataJson.yData2,
    },

    {
      name: '',
      type: 'bar',
      barGap: '60%',
      barWidth: barWidth,
      itemStyle: {
        color: {
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          type: 'linear',
          global: false,
          colorStops: [
            {
              //第一节下面
              offset: 0,
              color: '#35ffbe',
            },
            {
              offset: 1,
              color: '#000000FF',
            },
          ],
        },
        borderColor: {
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          type: 'linear',
          global: false,
          colorStops: [
            {
              //第一节下面
              offset: 0,
              color: 'rgba(32, 170, 146,0.8)',
            },
            {
              offset: 1,
              color: 'rgba(20, 76, 41,1)',
            },
          ],
        },
        borderWidth: 1,
        borderType: 'solid',
        borderRadius: 0
      },
      label: {
        show: true,
        position: 'top',
        fontSize: 12,
        fontFamily: 'Arial',
        foontWeight: 'normal',
        color: '#FFFFFFFF',
        formatter: '{c}'
      },
      zlevel: 2,
      data: dataJson.yData1,
    },
    {
      name: '',
      type: 'bar',
      barGap: '60%',
      barWidth: barWidth,
      itemStyle: {
        color: {
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          type: 'linear',
          global: false,
          colorStops: [
            {
              //第一节下面
              offset: 0,
              color: '#05A4E0CC',
            },
            {
              offset: 1,
              color: '#103959FF',
            },
          ],
        },
        borderColor: {
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          type: 'linear',
          global: false,
          colorStops: [
            {
              //第一节下面
              offset: 0,
              color: '#05A4E0CC',
            },
            {
              offset: 1,
              color: '#103959FF',
            },
          ],
        },
        borderWidth: 1,
        borderType: 'solid',
        borderRadius: 0
      },
      label: {
        show: true,
        position: 'top',
        fontSize: 12,
        fontFamily: 'Arial',
        foontWeight: 'normal',
        color: '#FFFFFFFF',
        formatter: '{c}'
      },
      zlevel: 2,
      data: dataJson.yData2,
    },
    {
      name: '背景柱子1',
      type: 'bar',
      tooltip: { show: false },
      xAxisIndex: 1,
      barGap: '60%',
      data: [100,100,100,100,100,100],
      zlevel: 1,
      barWidth: barWidth,
      itemStyle: {
        normal: {
          color: '#103846CC',
        },
      },
    },
    {
      name: '背景柱子2',
      type: 'bar',
      tooltip: { show: false },
      xAxisIndex: 1,
      barGap: '60%',
      data: [100,100,100,100,100,100],
      zlevel: 1,
      barWidth: barWidth,
      itemStyle: {
        normal: {
          color: '#103846CC',
        },
      },
    },
    {
      name: '底部圆1',
      type: 'pictorialBar',
      tooltip: { show: false },
      symbolSize: [barWidth, 10],
      symbolOffset: ['-81%', 5],
      z: 12,
      color: 'rgb(19, 74, 35)',
      data: [100,100,100,100,100,100],
    },
    {
      name: '底部圆2',
      type: 'pictorialBar',
      tooltip: { show: false },
      symbolSize: [barWidth, 10],
      symbolOffset: ['81%', 5],
      z: 12,
      color: 'rgb(16, 57, 89)',
        data: [100,100,100,100,100,100],
    },
    // 头
    {
      name: '顶部圆1',
      type: 'pictorialBar',
      tooltip: { show: false },
      z: 20,
      zlevel: 3,
      symbolPosition: 'end',
      symbolSize: [barWidth, 10],
      symbolOffset: [-16, -5],
      itemStyle: {
        normal: {
          shadowColor: 'rgba(0, 0, 0, .3)',
          shadowBlur: 5,
          shadowOffsetY: 3,
          shadowOffsetX: 0,
          color: 'rgba(30, 100, 112,1)',
        },
      },
      data:[
          {name: '', value: 100, symbolPosition: 'end'},
          {name: '', value: 100, symbolPosition: 'end'},
          {name: '', value: 100, symbolPosition: 'end'},
          {name: '', value: 100, symbolPosition: 'end'},
          {name: '', value: 100, symbolPosition: 'end'},
          {name: '', value: 100, symbolPosition: 'end'}
      ],
    },
    {
      name: '顶部圆2',
      type: 'pictorialBar',
      tooltip: { show: false },
      z: 20,
      zlevel: 2,
      symbolPosition: 'end',
      symbolSize: [barWidth, 10],
      symbolOffset: [16, -5],
      itemStyle: {
        normal: {
          shadowColor: 'rgba(0, 0, 0, .3)',
          shadowBlur: 5,
          shadowOffsetY: 3,
          shadowOffsetX: 0,
          color: 'rgba(15, 69, 133,1)',
        },
      },  data:[
            {name: '', value: 100, symbolPosition: 'end'},
            {name: '', value: 100, symbolPosition: 'end'},
            {name: '', value: 100, symbolPosition: 'end'},
            {name: '', value: 100, symbolPosition: 'end'},
            {name: '', value: 100, symbolPosition: 'end'},
            {name: '', value: 100, symbolPosition: 'end'}
        ],
    },
  ],
};


export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = BarDoubleConfig.key
  public chartConfig = cloneDeep(BarDoubleConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
