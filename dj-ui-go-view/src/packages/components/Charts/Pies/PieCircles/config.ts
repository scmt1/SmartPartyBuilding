import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { PieCirclesConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'
import {DataSet} from "@vicons/carbon";

export const includes = ['legend']

  // 复制代码
const chartData = dataJson.source

//色集
const colorList = ['#88D9FF', '#0092FF', '#81EDD2', '#B0FA93', '#63F2FF', '#9999FE'];
const sum = chartData.reduce((per, cur) => per + cur.value, 0);
const gap = (1 * sum) / 100;
const gapData = {
  name: "",
  value: gap,
  itemStyle: {
    color: "transparent",
  },
};

//图标位置显示
let total = 0;
chartData.forEach((item) => {
  total += item.value;
});

const option = {
  dataset: { ...dataJson },
  backgroundColor: '#0F141B',
  title: {
    x: "49%",
    y: "38%",
    itemGap:15,
    textStyle: { //中心标题样式
      color: "#f5f5f6",
      fontSize: 22,
      fontWeight: "bold",
    },
    subtextStyle: {//中心数值样式
      color: "#f5f5f6",
      fontSize: 22,
      fontWeight: "bold",
    },
    textAlign:'center'
  },

  // backgroundColor: "#0F141B",
  tooltip: {        //提示框
    show: true,
    backgroundColor: "rgba(0, 0, 0,.8)",
    textStyle: {
      color: "#fff",
      fontWeight: "bold",
      fontSize: "14"
    },
  },
  // legend: legendData,
  grid: {
    top: 30,
    right: 20,
    bottom: 10,
    left: 10,
  },
  color: colorList,
  series: [
    {
      name: '',
      type: 'pie',
      roundCap: true,
      radius: ['46%', '50%'], //内外半径大小
      center: ['50%', '65%'],
      encode: {
        "itemName": "name",
        "value": "value"
      },
      label: { //标签
        show: true,
        position: 'outside',
        fontWeight: 'bold',
        fontSize: 12,

      },
      labelLine: {//标签引导线
        show: true,
        length: 59,
        length2: 41,
        smooth: false,
        lineStyle: {
          width: 5.5
        }
      },
      emphasis: {//高亮状态的扇区和标签样式
        disabled: false,
        scale: true,
        scaleSize: 25
      }

    },
    {
      type: 'pie',
      radius: ['46%', '40%'],
      center: ['50%', '65%'],
      gap: 1.71,
      itemStyle: {
        opacity: 0.21
      },
      label: {
        show: false
      },
      labelLine: {
        show: false
      },
      silent: true,
    },
    {
      type: 'gauge',
      zlevel: 2,
      splitNumber: 90,
      radius: '40%',
      center: ['50%', '65%'],
      startAngle: 90,
      endAngle: -269.9999,
      axisLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      axisLabel: {
        show: false,
      },
      splitLine: {
        show: true,
        length: 7,
        lineStyle: {
          width: 4,
          color: 'rgb(33,85,130)',
        },
      },
      pointer: {
        show: 0,
      },
      detail: {
        show: 0,
      },
    },
    {
      type: 'pie',
      center: ['50%', '65%'],
      radius: [0, '30.6%'],
      label: {
        show: false
      },
      labelLine: {
        show: false
      },
      itemStyle: {
        color: 'rgba(75, 126, 203,.1)'
      },
      silent: true,
      data: [
        {
          value: 100,
          name: ''
        }
      ]
    }
  ],
};

export default class Config extends PublicConfigClass implements CreateComponentType {
  public key: string = PieCirclesConfig.key

  public chartConfig = cloneDeep(PieCirclesConfig)

  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
