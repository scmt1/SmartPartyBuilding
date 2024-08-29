import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { BarTwoBeamConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import { graphic } from 'echarts/core'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']

export const option = {
  dataset: { ...dataJson },
  backgroundColor: '#031245',
  grid: {
    left: '5%',
    top: '10%',
    right: '5%',
    bottom: '10%',
  },
  legend: {
    show: true,
    icon: 'circle',
    orient: 'horizontal',
    top: '3%',
    right: 'center',
    itemWidth: 16.5,
    itemHeight: 6,
    textStyle: {
      color: '#C9C8CD',
      fontSize: 14
    }
  },
  xAxis: [{
    data: ["监测中心站", "调查中心", "核与辐射安全中心", "宣传教育中心"],
    axisLabel: {
      textStyle: {
        color: '#fff',
        fontSize:12
      },
      margin: 30, //刻度标签与轴线之间的距离。
    },
    axisLine: {
      show: true, //不显示x轴
      lineStyle: {
        color: '#384267'
      }
    },
    axisTick: {
      show: false //不显示刻度
    },
    boundaryGap: true,
    splitLine: {
      show: false,
      width: 0.08,
      lineStyle: {
        type: "solid",
        color: "#03202E"
      }
    }
  }],
  yAxis: [{
    splitLine: {
      show: true,
      lineStyle: {
        color: '#384267',
        type: 'dashed'
      }
    },
    axisTick: {
      show: false
    },
    axisLine: {
      show: true,
      lineStyle: {
        color: "#384267"
      }
    },
    axisLabel: {
      textStyle: {
        color: '#fff',
        fontSize: 12
      },
    }
  }],
  series: [
    //柱顶圆片0000
    {
      name: "",
      type: "pictorialBar",
      symbolSize: [50, 20],//调整截面形状
      symbolOffset: [-33, -10],
      z: 12,
      symbolPosition: "end",
      "itemStyle": {
        "normal": {
          color: new graphic.LinearGradient(0,0,0,1,
              [{
                offset: 0,
                color: "#50A7FF"
              },
                {
                  offset: 1,
                  color: "#02D6EA"
                }
              ],
              false
          ),
        }
      },
      data: ["40", "45", "65","100"]
    },
    {//柱底圆片
      name: "",
      type: "pictorialBar",
      symbolSize: [50, 20],//调整截面形状
      symbolOffset: [-33, 10],
      z: 12,
      itemStyle: {
        "normal": {
          color: new graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: "#02D6EA"
          },
            {
              offset: 1,
              color: "#02D6EA"
            }
          ],false)
        }
      },
      data: ["50", "75", "105","130"]
    },

    //柱体
    {
      name: '参保机构',
      type: 'bar',
      barWidth: 50,
      barGap: '30%',
      itemStyle: {
        "normal": {
          "color": {
            "x": 0,
            "y": 0,
            "x2": 0,
            "y2": 1,
            "type": "linear",
            "global": false,
            "colorStops": [{//第一节下面
              "offset": 0,
              "color": "#057DFE"
            }, {
              "offset": 1,
              "color": "#02D7EA"
            }]
          }
        }
      },
      data: ["40", "45", "65","100"]
    },
    //柱顶圆片
    {
      name: "",
      type: "pictorialBar",
      symbolSize: [50, 20],//调整截面形状
      symbolOffset: [33, -10],
      z: 12,
      symbolPosition: "end",
      "itemStyle": {
        "normal": {
          color: new graphic.LinearGradient(0,0,0,1,
              [{
                offset: 0,
                color: "#9816c6"
              },
                {
                  offset: 1,
                  color: "#9816c6"
                }
              ],
              false
          ),
        }
      },
      data: ["50", "75", "105","130"]
    },
    {//柱底圆片
      name: "",
      type: "pictorialBar",
      symbolSize: [50, 20],//调整截面形状
      symbolOffset: [33, 10],
      z: 12,
      itemStyle: {
        "normal": {
          color: new graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: "#a522dc"
          },
            {
              offset: 1,
              color: "#a522dc"
            }
          ],false)
        }
      },
      data: ["40", "45", "65","100"]
    },
    //柱体
    {
      name: '实际参保机构',
      type: 'bar',
      barWidth: 50,
      barGap: '30%',
      itemStyle: {
        "normal": {
          "color": {
            "x": 0,
            "y": 0,
            "x2": 0,
            "y2": 1,
            "type": "linear",
            "global": false,
            "colorStops": [{//第一节下面
              "offset": 0,
              "color": "#8319c0"
            }, {
              "offset": 1,
              "color": "#a522dc"
            }]
          }
        }
      },
      data: ["50", "75", "105","130"]
    },
  ],
  "tooltip": {
    "trigger": "axis",
    backgroundColor:'rgba(0,0,0,0.8)',
    borderWidth:0,
    "show": true,
    textStyle: { fontSize: '100%' },
    formatter:function(item: any[]){
      var returnData1 =
          '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background:' +
          item[1].color.colorStops[0].color+
          '"></span>';
      var returnData2=
          '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background:' +
          item[4].color.colorStops[0].color+
          '"></span>';
      return '<div style="color:#FFF">'+item[0].axisValue+"<br/>"+returnData1+item[2].seriesName+":"+item[2].value+'个<br/>'+returnData2+item[5].seriesName+":"+item[5].value+'个</div>';
    }
  }
}
export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = BarTwoBeamConfig.key
  public chartConfig = cloneDeep(BarTwoBeamConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
