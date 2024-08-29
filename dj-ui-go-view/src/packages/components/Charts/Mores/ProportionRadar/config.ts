import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { ProportionRadarConfig } from './index'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend']

let areaData = dataJson.source;
let data: any[] = [];
areaData.forEach((item) => {
  data.push(item.value);
});
let maxNum = Math.max(...data); //获取数组中最大的值
// 向上取整十，整百，整千，整万的方法
function ceilNumber(number: number) {
  let bite = 0;
  if (number < 10) {
    return 10;
  }
  while (number >= 10) {
    number /= 10;
    bite += 1;
  }
  return Math.ceil(number) * Math.pow(10, bite);
}
let allMax = ceilNumber(maxNum); //将最大的值进行向上取整
areaData.forEach((item) => {
  item.max = allMax;
});
let data1 = [0, 0, 0, 0, 0, 0];
let data2 = [0, 0, 0, 0, 0, 0];
let data3 = [0, 0, 0, 0, 0, 0];
let data4 = [0, 0, 0, 0, 0, 0];
let data5 = [0, 0, 0, 0, 0, 0];
data1[0] = data[0];
data2[1] = data[1];
data3[2] = data[2];
data4[3] = data[3];
data5[4] = data[4];

var colors: never[] = [];
for (let i = 0; i < dataJson.source.length; i++) {
  let a = parseInt(String(Math.random() * 255));
  let b = parseInt(String(Math.random() * 255));
  let c = parseInt(String(Math.random() * 255));
  let color:any = `rgb(${a},${b},${c})`;
  // @ts-ignore
  colors.push(color)
}
export const option = {
  dataset:{ ...dataJson },
  backgroundColor: '#092A4E',
  color: colors,
  legend: {
    trigger: 'item',
    orient: 'vertical',
    top: '40px',
    left: '20px',
    textStyle: {
      color: '#fff',
    },
  },
  tooltip: {
    trigger: 'item',
    formatter: function (params: { name: string; value: string }) {
      return params.name + ': ' + params.value;
    },
  },
  radar: {
    zlevel: 11,
    shape: 'circle',
    scale: true,
    indicator: dataJson.source,
    center: ['50%', '50%'],
    radius: '40%',
    nameGap: '0',
    startAngle: 90,
    name: {
      show: false,
      color: 'red',
    },
    axisLine: {
      show: true,
      lineStyle: {
        color: 'rgba(32,126,255, .5)',
      },
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(32,126,255, .5)',
      },
    },
    splitArea: {
      areaStyle: {
        color: 'rgba(32,126,255, 0.1)',
      },
    },
  },
  polar: {
    radius: '40%',
  },
  angleAxis: {
    zlevel: 0,
    min: 0,
    max: 360,
    axisLabel: {
      show: false,
    },
    splitLine: {
      show: false,
    },
  },
  radiusAxis: {
    zlevel: 0,
    min: 0,
    // max: allMax,
    axisLabel: {
      textStyle: {
        color: '#fff',
      },
    },
    splitLine: {
      show: false,
    },
  },
  series: [
    {
      type: 'radar',
      symbolSize: 12,
      symbol: 'circle',
      tooltip: {
        trigger: 'none',
      },
      itemStyle: {
        normal: {
          label: {
            show: true,
            formatter: function (params: { value: string | number }) {
              if (params.value !== 0) {
                return params.value;
              } else {
                return '';
              }
            },
            color: '#ffffff',
            fontSize: 12,
            distance: 0,
          },
        },
      },
      areaStyle: {
        normal: {
          color: {
            type: 'radial',
            x: 0.5,
            y: 0.5,
            r: 0.5,
            colorStops: [
              {
                offset: 0,
                color: 'rgba(32,126,255, 0.5)', // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(32,126,255, 0.1)', // 100% 处的颜色
              },
            ],
            global: false, // 缺省为 false
          },
        },
      },
      data: [
        {
          value: data,
        },
        {
          value: data1,
          itemStyle: {
            color: '#2e64ff',
          },
          lineStyle: {
            color: 'transparent', //设置折线的颜色
          },
        },
        {
          value: data2,
          itemStyle: {
            color: '#f0d418',
          },
          lineStyle: {
            color: 'transparent', //设置折线的颜色
          },
        },
        {
          value: data3,
          itemStyle: {
            color: '#ff9b42',
          },
          lineStyle: {
            color: 'transparent', //设置折线的颜色
          },
        },
        {
          value: data4,
          itemStyle: {
            color: '#ff6b6b',
          },
          lineStyle: {
            color: 'transparent', //设置折线的颜色
          },
        },
        {
          value: data5,
          itemStyle: {
            color: '#8e42ff',
          },
          lineStyle: {
            color: 'transparent', //设置折线的颜色
          },
        },
        {
          value: [0, 0, 0, 0, 0],
          // name: '违章占比',
          symbolSize: 13,
          itemStyle: {
            color: 'rgba(40, 97, 132)',
          },
        },
      ],
      zlevel: 20,
      z: 4,
    },
    {
      type: 'pie',
      startAngle: 90,
      clockwise: false, //逆时针
      // data: areaData.map((item) => {
      //   return {
      //     name: item.name,
      //     value: item.value,
      //   };
      // }),
      radius: ['45%', '60%'],
      zlevel: -1,
      label: {
        normal: {
          formatter: '{d}%',
          position: 'top',
          fontSize: '24',
          color: '#fff',
        },
      },
    },
  ]
}

export default class Config extends PublicConfigClass implements CreateComponentType {
  public key = ProportionRadarConfig.key
  public chartConfig = cloneDeep(ProportionRadarConfig)
  // 图表配置项
  public option = echartOptionProfixHandle(option, includes)
}
