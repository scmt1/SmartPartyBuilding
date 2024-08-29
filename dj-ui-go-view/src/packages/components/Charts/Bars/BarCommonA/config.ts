import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarCommonAConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']

// @ts-ignore
var newchartName = dataJson.xData;
// @ts-ignore
var newchartValue1 = dataJson.yData1;
// @ts-ignore
var newchartValue2 = dataJson.yData2;
var barWidth = 30;
var color1 = {
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
      color: '#1C98CD',
    },
    {
      offset: 1,

      color: 'rgba(61,187,255,.16)',
    },
  ],
};
var color2 = {
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
      color: '#E7AB47',
    },
    {
      offset: 1,
      color: 'rgba(255,164,41,.16)',
    },
  ],
};
export const option = {
  dataset: dataJson,
  backgroundColor: '#0D2048', //背景色
  legend: {
    data: dataJson.legendData,
    icon: 'rect',
    top: "10%",
    right: "center",
    textStyle: {
      color: '#fff',
      fontWeight: 'normal',
      fontSize: 12,
    },
  },
  //提示框
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'none',
    },
    formatter: function (param: any[]) {
      var resultTooltip =
          "<div style='background:rgb(255,255,255);border:1px solid rgba(255,255,255,.2);padding:5px 10px;border-radius:4px;'>" +
          "<div style='text-align:center;'>" +
          param[0].name +
          '</div>' +
          "<div style='padding-top:5px;'>" +
          "<span style=''> " +
          param[0].seriesName +
          ': </span>' +
          "<span style=''>" +
          param[0].value +
          '</span>' +
          '</div>' +
          "<div style='padding-top:5px;'>" +
          "<span style=''> " +
          param[1].seriesName +
          ': </span>' +
          "<span style=''>" +
          param[1].value +
          '</span>' +
          '</div>' +
          '</div>';
      return resultTooltip;
    },
  },
  title: [],
  grid: {
    top: '18%',
    left: '5%',
    bottom: '10%',
    right: '5%',
    containLabel: true,
  },
  animation: false,
  xAxis: [
    {
      type: 'category',
      axisTick: {
        show: false,
      },
      axisLine: {
        show: true,
        lineStyle: {
          color: '#333333',
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
      data: newchartName,
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
      data: newchartName,
    },
  ],
  yAxis: [
    {
      show: true,
      type: 'value',
      axisLabel: {
        show: false,
        textStyle: {
          color: '#fff',
        },
      },
      splitLine: {
        lineStyle: {
          color: '#212E4A',
        },
      },
      axisLine: {
        show: false,
      },
    },
  ],
  series: [
    {
      name: '失信',
      type: 'pictorialBar',
      symbolSize: [barWidth, 10],
      symbolOffset: ['-81%', -5],
      symbolPosition: 'end',
      z: 15,
      color: '#3eb6f5',
      zlevel: 2,
      data: newchartValue1,
    },
    {
      name: '守信',
      type: 'pictorialBar',
      symbolSize: [barWidth, 10],
      symbolOffset: ['81%', -5],
      symbolPosition: 'end',
      z: 15,
      color: '#ffc241',
      zlevel: 2,
      data: newchartValue2,
    },

    {
      name: '失信',
      type: 'bar',
      barGap: '60%',
      barWidth: barWidth,
      itemStyle: {
        color: color1,
        borderColor: color1,
        borderWidth: 1,
        borderType: 'solid',
      },
      label: {
        show: true,
        formatter: '{c}',
        position: 'top',
        // color: 'rgba(119,167,255,1)',
        color:'#fff',
        fontSize: 12,
        textAlign: 'center',
      },
      zlevel: 2,
      data: newchartValue1,
    },
    {
      name: '守信',
      type: 'bar',
      barGap: '60%',
      barWidth: barWidth,
      itemStyle: {
        color: color2,
        borderColor: color2,
        borderWidth: 1,
        borderType: 'solid',
      },
      label: {
        show: true,
        formatter: '{c}',
        position: 'top',
        color: 'rgba(255,228,59,1)',
        fontSize: 12,
        textAlign: 'center',
      },
      zlevel: 2,
      data: newchartValue2,
    },
    {
      name: '浅蓝柱子',
      type: 'bar',
      xAxisIndex: 1,
      barGap: '60%',
      data: [100, 100, 100, 100, 100, 100, 100],
      zlevel: 1,
      barWidth: barWidth,
      itemStyle: {
        normal: {
          color: 'rgba(61,187,255,.16)',
        },
      },
    },
    {
      name: '浅黄柱子',
      type: 'bar',
      xAxisIndex: 1,
      barGap: '60%',
      data: [100, 100, 100, 100, 100, 100, 100],
      zlevel: 1,
      barWidth: barWidth,
      itemStyle: {
        normal: {
          color: 'rgba(255,164,41,.16)',
        },
      },
    },
    {
      name: '黄色底座',
      type: 'pictorialBar',
      symbolSize: [barWidth, 10],
      symbolOffset: ['-81%', 5],
      z: 12,
      color: '#3eb6f5',

      data: [
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        }

      ],
    },
    {
      name: '蓝色底座',
      type: 'pictorialBar',
      symbolSize: [barWidth, 10],
      symbolOffset: ['81%', 5],
      z: 12,
      color: '#ffc241',
      show: false,
      data: [
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        },
        {
          name: '',
          value: '100',
        }
      ],
    },
    // 头

    {
      name: '蓝色动态封顶',
      type: 'effectScatter',
      rippleEffect: {
        period: 1,
        scale: 4, //控制上方蓝色动态效果封顶大小
        brushType: 'fill',
      },
      z: 20,
      zlevel: 3,
      symbolPosition: 'end',
      symbolSize: [barWidth/2, 4],
      symbolOffset: [-24, 0], //控制上方蓝色动态效果封顶位置
      itemStyle: {
        normal: {
          shadowColor: 'rgba(0, 0, 0, .3)',
          shadowBlur: 5,
          shadowOffsetY: 3,
          shadowOffsetX: 0,
          color: 'rgba(119,167,255,1)',
        },
      },
      data: [
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        }
      ],
    },
    {
      name: '黄色动态封顶',
      type: 'effectScatter',
      rippleEffect: {
        period: 1,
        scale: 4, //控制上方黄色动态效果封顶大小
        brushType: 'fill',
      },
      z: 20,
      zlevel: 2,
      symbolPosition: 'end',
      symbolSize: [barWidth/2, 4],
      symbolOffset: ['24', 0], //控制上方黄色动态效果封顶大小
      itemStyle: {
        normal: {
          shadowColor: 'rgba(0, 0, 0, .3)',
          shadowBlur: 5,
          shadowOffsetY: 3,
          shadowOffsetX: 0,
          color: 'rgba(255,164,41,0.5)',
        },
      },
      data: [
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        },
        {
          name: '',
          value: 100,
          symbolPosition: 'end',
        }
      ],
    },
  ]
}
export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = BarCommonAConfig.key
  public chartConfig = cloneDeep(BarCommonAConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
