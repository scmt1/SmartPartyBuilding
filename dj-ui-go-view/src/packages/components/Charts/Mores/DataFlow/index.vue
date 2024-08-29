<template>
  <v-chart
          ref="vChartRef"
          :theme="themeColor"
          :option="option"
          :manual-update="isPreview()"
          :update-options="{ replaceMerge: replaceMergeArr }"
          autoresize
  >
  </v-chart>
</template>

<script setup lang="ts">
  import { PropType, computed, watch, ref } from 'vue'
  import config, { includes } from './config'
  import { mergeTheme } from '@/packages/public/chart'
  import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
  import { useChartDataFetch } from '@/hooks'
  import { isPreview } from '@/utils'
  import VChart from "vue-echarts";

  const props = defineProps({
    themeSetting: {
      type: Object,
      required: true
    },
    themeColor: {
      type: Object,
      required: true
    },
    chartConfig: {
      type: Object as PropType<config>,
      required: true
    }
  })
  const vChartRef = ref<typeof VChart>()
  const replaceMergeArr = ref<string[]>()

  const option = computed(() => {
    return mergeTheme(props.chartConfig.option, props.themeSetting, includes)
  })

  // dataset 无法变更条数的补丁
  watch(
    () => props.chartConfig.option.dataset,
    (newData, oldData) => {
      try {

      } catch (error) {
        console.log(error)
      }
    },
    {
      deep: false
    }
  )

  let points = [
    {
      name: '政务诚信',
      desc: {
        type: 1,
      },
    },
    {
      name: '司法公信',
      desc: {
        type: 1,
      },
    },
    {
      name: '社会诚信',
      desc: {
        type: 1,
      },
    },
    {
      name: '商务诚信',
      desc: {
        type: 1,
      },
    }
  ];
  let color = ['#ffffff', '#36fff6', '#ffe93a', '#67f95f'];
  let oneArr = [
    {
      name: '中心点',
      desc: {
        type: 0,
      },
      symbol: 'none',
      symbolSize: 233,
    },
  ];
  let erArr = points;
  let allArr = [...oneArr, ...erArr],
          dataArr: any[] = [];
  // 点
  allArr.forEach((el:any, ind) => {
    if (el.desc.type > 0) {
      el.symbolSize = 30;
      el.symbol =
              'path://M544 552.325V800a32 32 0 0 1-32 32 31.375 31.375 0 0 1-32-32V552.325L256 423.037a32 32 0 0 1-11.525-43.512A31.363 31.363 0 0 1 288 368l224 128 222.075-128a31.363 31.363 0 0 1 43.525 11.525 31.988 31.988 0 0 1-11.525 43.513L544 551.038z m0 0,M64 256v512l448 256 448-256V256L512 0z m832 480L512 960 128 736V288L512 64l384 224z m0 0';
      el.itemStyle = {
        color: color[el.desc.type],
      };
    }
    el.label = {
      normal: {
        show: true,
        position: 'bottom',
        distance: 5,
        color: color[el.desc.type],
      },
    };
  });

  // 圆形分区
  function CalutePointToSplitCircle(arr: any[], option: { stratAngle: any; raduis: any; nodeRadius: any; emptyCenterRadius: any }) {
    const newArray: any[] = [];
    const { length: arLen } = arr;
    let single_angle:any = (360 / arLen).toFixed(2);
    let UtilCalute = {
      calute_x: (ang: number, radius: number) => {
        let num:any
        num = Math.cos((ang * Math.PI) / 180).toFixed(2)
        return (num * radius).toFixed(2);
      },
      calute_y: (ang: number, radius: number) => {
        let num:any
        num = Math.sin((ang * Math.PI) / 180).toFixed(2)
        return (num * radius).toFixed(2);
      },
    };
    // 正东方向开始 逆时针方向
    arr.forEach((e, index) => {
      let itemDesc = e.desc;
      let ang =
              option.stratAngle +
              (itemDesc.angle && typeof itemDesc.angle === 'number' ? itemDesc.angle : single_angle * index);
      // 各节点中心点
      const x = UtilCalute.calute_x(ang, option.raduis);
      const y = UtilCalute.calute_y(ang, option.raduis);
      // 各节点连接到中心的线段的起点  减去节点的半径
      const x1 = UtilCalute.calute_x(ang, option.raduis - option.nodeRadius);
      const y1 = UtilCalute.calute_y(ang, option.raduis - option.nodeRadius);
      // 各节点连接到中心的线段的终点
      const x0 = UtilCalute.calute_x(ang, option.emptyCenterRadius);
      const y0 = UtilCalute.calute_y(ang, option.emptyCenterRadius);

      e.value = [x, y]; // 节点中心点
      e.lineData = [
        [x1, y1],
        [x0, y0],
      ];
      newArray.push(e);
    });
    return newArray;
  }

  // 线配置
  function linesConfig(arr: any[]) {
    let dataArr: ({ coord: any; lineStyle?: undefined; effect?: undefined } | { coord: any; lineStyle: { color: string; curveness: number }; effect: { color: string } })[][] = [];

    function getFormatItem(item: { lineData: { [x: string]: any }; desc: { type: number } }, start: number, end: number) {
      return [
        { coord: item.lineData[start] }, // 起点
        {
          coord: item.lineData[end],
          lineStyle: {
            color: '#ffffff',
            curveness: item.desc.type === 3 ? 0.1 : 0,
          },
          effect: {
            color: color[item.desc.type],
          },
        }, // 终点
      ];
    }

    arr.forEach((el) => {
      switch (el.desc.type) {
        case 0:
          break;
        case 1:
          // 外到内
          dataArr.push(getFormatItem(el, 0, 1));
          break;
        case 2:
          // 内到外
          dataArr.push(getFormatItem(el, 1, 0));
          break;
        case 3:
          // 双向
          dataArr.push(getFormatItem(el, 0, 1));
          dataArr.push(getFormatItem(el, 1, 0));
          break;
      }
    });
    return dataArr;
  }

  // 点分布
  oneArr = CalutePointToSplitCircle(oneArr, {
    stratAngle: 0,
    raduis: 0,
    nodeRadius: 0,
    emptyCenterRadius: 0,
  });
  erArr = CalutePointToSplitCircle(erArr, {
    stratAngle: 0,
    raduis: 40,
    nodeRadius: 5,
    emptyCenterRadius: 10,
  });

  allArr = [...oneArr, ...erArr];
  // 线坐标和配置
  dataArr = linesConfig(allArr);
  // 生成虚线 饼图数据
  function generateData(totalNum: number, bigvalue: number, smallvalue: number, color: string) {
    let dataArr = [];
    for (var i = 0; i < totalNum; i++) {
      if (i % 2 === 0) {
        dataArr.push({
          name: (i + 1).toString(),
          value: bigvalue,
          itemStyle: {
            normal: {
              color: color,
              borderWidth: 0,
            },
          },
        });
      } else {
        dataArr.push({
          name: (i + 1).toString(),
          value: smallvalue,
          itemStyle: {
            normal: {
              color: 'rgba(0,0,0,0)',
              borderWidth: 0,
            },
          },
        });
      }
    }
    return dataArr;
  }
  let dolitData = generateData(100, 25, 20, 'rgb(126,190,255)');
  let threeData = generateData(6, 40, 10, '#2dc0c9');
  function getOption(startAngle: number, radius: number) {
    let option = {
      backgroundColor: 'rgb(61,89,255,0.01)',
      xAxis: {
        show: false,
        type: 'value',
        max: 50,
        min: -51,
      },
      grid: {
        top: 10,
        bottom: 10,
        left: 10,
        right: 10,
      },
      yAxis: {
        show: false,
        type: 'value',
        max: 50,
        min: -50,
      },
      graphic: {
        elements: [
          {
            type: 'text',
            z: 7,
            style: {
              text: '信用制度标准',
              textAlign: 'center',
              fill: '#fff',
              font: '15px "Microsoft YaHei"',
            },
            left: 'center',
            top: 'center',
          },
          {
            type: 'image',
            z: 4,
            style: {
              image: './asset/get/s/data-1641374427371-xPKsRNQ2l.png',
              width: 170,
              height: 170,
            },
            left: 'center',
            top: 'center',
          },
        ],
      },
      series: [
        {
          name: '节点',
          type: 'graph',
          silent: false,
          hoverAnimation: false, // 鼠标悬浮高亮
          cursor: 'default',
          coordinateSystem: 'cartesian2d',
          z: 3,
          lineStyle: {
            width: 2,
            color: 'source',
            type: 'dashed',
          },
          data: allArr,
        },
        {
          name: '线图',
          type: 'lines',
          hoverAnimation: false,
          silent: false,
          cursor: 'default',
          coordinateSystem: 'cartesian2d',
          polyline: false, // 多线段
          z: 1,
          lineStyle: {
            width: 2,
            type: 'dashed',
            curveness: 0,
          },
          effect: {
            show: true,
            period: 8, //箭头指向速度，值越小速度越快
            trailLength: 0, //特效尾迹长度[0,1]值越大，尾迹越长重
            symbol: 'arrow', //箭头图标
            symbolSize: 6,
          },
          emphasis: {
            lineStyle: {
              type: 'dashed',
            },
          },
          data: dataArr,
        },
        {
          name: '不动外圆',
          type: 'pie',
          zlevel: 4,
          silent: true,
          radius: ['60%', '59%'],
          label: {
            normal: {
              show: false,
            },
          },
          labelLine: {
            normal: {
              show: false,
            },
          },
          data: dolitData,
        },
        {
          type: 'pie',
          name: '旋转圆',
          zlevel: 20,
          silent: true,
          radius: ['50%', '49%'],
          hoverAnimation: false,
          startAngle: startAngle,
          data: threeData,
          label: {
            normal: {
              show: false,
            },
          },
          labelLine: {
            normal: {
              show: false,
            },
          },
        },
        {
          name: '缩放圆',
          type: 'pie',
          zlevel: 4,
          silent: true,
          radius: [radius + 1 + '%', radius + '%'],
          label: {
            normal: {
              show: false,
            },
          },
          labelLine: {
            normal: {
              show: false,
            },
          },
          data: dolitData,
        },
      ],
    };
    return option;
  }
  let startAngle = 50; // 初始旋转角度
  let isBig = true; // 缩放动画 标识
  let [minradius, radius, maxradius] = [22, 22, 24]; // 最小、初始、最大 缩放尺寸
  function draw() {
    startAngle = startAngle - 5;
    if (isBig) {
      radius = radius + 0.5;
      if (radius > maxradius) {
        isBig = false;
      }
    } else {
      radius = radius - 0.5;
      if (radius < minradius) {
        isBig = true;
      }
    }
    let option = getOption(startAngle, radius);
    vChartRef.value?.setOption(option, true);
  }

  setInterval(draw, 200);
  useChartDataFetch(props.chartConfig, useChartEditStore)
</script>
