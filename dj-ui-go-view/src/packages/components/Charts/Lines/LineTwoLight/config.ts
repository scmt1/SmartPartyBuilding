import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { LineTwoLightConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import { defaultTheme, chartColorsSearch } from '@/settings/chartThemes/index'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'
import * as echarts from 'echarts'



export const includes = ['legend', 'xAxis', 'yAxis', 'grid']
 const option = {
   dataset: dataJson,
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
   tooltip: {
     show: true,
     trigger: 'axis',
     axisPointer: {
       type: 'line'
     }
   },
   legend:{
     padding: "",
     left: "auto",
     top: "auto",
     itemHeight: 20.5,
     itemWidth: 30,
     textStyle:{
       fontSize:'12'
     }
   },
  backgroundColor: '#080b30',
  title: {
    text: '',
    textStyle: {
      align: 'center',
      color: '#fff',
      fontSize: 20,
    },
    top: '5%',
    left: 'center',
  },
  grid: {
    top: '15%',
    left: '5%',
    right: '5%',
    bottom: '15%',
    // containLabel: true
  },
  xAxis: [{
    type: 'category',
    axisLine: {
      lineStyle: {
        width: 4.5
      },
      show: true
    },
    splitArea: {
      // show: true,
      color: '#f00',
      lineStyle: {
        color: '#f00'
      },
    },
    axisLabel: {
      color: '#fff',
      fontSize: 12//刻度字体大小
    },
    splitLine: {
      show: false
    },
    boundaryGap: false,
    // data: ['A', 'B', 'C', 'D', 'E', 'F'],

  }],

  yAxis: [{
    // type: 'value',
    min: 0,
    // max: 140,
    splitNumber: 4,
    splitLine: {
      show: true,
      lineStyle: {
        color: 'rgba(255,255,255,0.1)'
      }
    },
    axisLine: {
      lineStyle: {
        width: 4.5,
      },
      show: false,
    },
    axisLabel: {
      show: false,
      margin: 20,
      textStyle: {
        color: '#d1e6eb',

      },
    },
    axisTick: {
      show: false,
    },
  }],
  series: [
      {
    // name: '注册总量',
    type: 'line',
    // smooth: true, //是否平滑
    showAllSymbol: true,
    // symbol: 'image://./static/images/guang-circle.png',
    symbol: 'circle',
    symbolSize: 4,//标记的大小
    lineStyle: {
      normal: {
        color: "#78c6e3",//线的颜色。
        width: 2,//线宽
        shadowColor: 'rgba(0, 0, 0, .3)',
        shadowBlur: 0,
        shadowOffsetY: 5,
        shadowOffsetX: 5,
      },
    },
    label: {
      show: true,
      position: 'top',
      fontSize: 10,
      textStyle: {
        color: '#6c50f3',
      }
    },
    itemStyle: {
      color: "#6c50f3",
      borderColor: "#fff",
      borderWidth: 3,
      shadowColor: 'rgba(0, 0, 0, .3)',
      shadowBlur: 0,
      shadowOffsetY: 2,
      shadowOffsetX: 2,
    },
    areaStyle: {
      normal: {
        // color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
        //   offset: 0,
        //   color: 'rgba(108,80,243,0.3)'
        // },
        //   {
        //     offset: 1,
        //     color: 'rgba(108,80,243,0)'
        //   }
        // ], false),
        color: "#81C8FFFF",
        opacity: 0.1,
        shadowColor: '#3318FFFF',
        shadowBlur: 20
      }
    },
  },
    {
      // name: '注册总量1',
      type: 'line',
      // smooth: true, //是否平滑
      showAllSymbol: true,
      // symbol: 'image://./static/images/guang-circle.png',
      symbol: 'circle',
      symbolSize: 4,
      lineStyle: {
        normal: {
          color: "#00ca95",
          shadowColor: 'rgba(0, 0, 0, .3)',
          shadowBlur: 0,
          shadowOffsetY: 5,
          shadowOffsetX: 5,
        },
      },
      label: {
        show: true,
        position: 'top',
        fontSize: 10,
        textStyle: {
          color: '#00ca95',
        }
      },

      itemStyle: {
        color: "#00ca95",
        borderColor: "#fff",
        borderWidth: 3,
        shadowColor: 'rgba(0, 0, 0, .3)',
        shadowBlur: 0,
        shadowOffsetY: 2,
        shadowOffsetX: 2,
      },
      areaStyle: {
        normal: {
          // color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
          //   offset: 0,
          //   color: 'rgba(0,202,149,0.3)'
          // },
          //   {
          //     offset: 1,
          //     color: 'rgba(0,202,149,0)'
          //   }
          // ], false),
          color: "#40F981FF",
          shadowColor: '#44F219FF',
          shadowBlur: 20,
          opacity: 0.1
        }
      },
      // data: dataJson.data2
    },
    {
      type: "lines",
      coordinateSystem: "cartesian2d",
      data: [{
        coords: dataJson.coords1
      }],
      zlevel: 1,
      polyline: true,
      symbol: "circle",
      effect:
          {
            show: true,
            trailLength: 0.1,
            symbol: "circle",
            period: 5,
            symbolSize: 10
          },
      lineStyle:
          {
            normal:
                {
                  color: '#008AEDFF',
                  width: 0,
                  opacity: 0,
                  curveness: 0
                }
          }
    },
    {
      type: "lines",
      coordinateSystem: "cartesian2d",
      data: [{
        coords: dataJson.coords2
      }],
      zlevel: 1,
      polyline: true,
      symbol: "circle",
      effect:
          {
            show: true,
            trailLength: 0.1,
            symbol: "circle",
            period: 5,
            symbolSize: 10

          },
      lineStyle:
          {
            normal:
                {
                  color: '#00FF44FF',
                  width: 0,
                  opacity: 0,
                  curveness: 0
                }
          }
    }
  ]
};
export default class Config extends PublicConfigClass implements CreateComponentType {
  public key: string = LineTwoLightConfig.key
  public chartConfig = cloneDeep(LineTwoLightConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
