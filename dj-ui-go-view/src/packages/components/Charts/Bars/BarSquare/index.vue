<template>
  <v-chart
    ref="vChartRef"
    :theme="themeColor"
    :option="option"
    :manual-update="isPreview()"
    :update-options="{
      replaceMerge: replaceMergeArr
    }"
    autoresize
  ></v-chart>
</template>

<script setup lang="ts">
import { ref, nextTick, computed, watch, PropType } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import config, { includes } from './config'
// import config, { includes, seriesItem } from './config'
import { mergeTheme } from '@/packages/public/chart'
import { useChartDataFetch } from '@/hooks'
import { CreateComponentType } from '@/packages/index.d'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { isPreview } from '@/utils'
import { DatasetComponent, GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import isObject from 'lodash/isObject'

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

use([DatasetComponent, CanvasRenderer, BarChart, GridComponent, TooltipComponent, LegendComponent])

const replaceMergeArr = ref<string[]>()

const option = computed(() => {
  return mergeTheme(props.chartConfig.option, props.themeSetting, includes)
})
// props.chartConfig.option.series[0].data.push(data1.pop());
// dataset 无法变更条数的补丁
const {vChartRef} = useChartDataFetch(props.chartConfig, useChartEditStore);
console.log("----原始props.chartConfig----")
console.log(props.chartConfig)
console.log("----原始vChartRef.value----")
console.log(vChartRef.value)
let arr: any[] = [];
let list: never[] = [];
let series = [];
let legend = [];
let fontSize=16;
let barWidth=26;
let barGap=0;
let maxList: never[]=[];
let max=0;//背景最大值取y轴刻度线（最后注释部分）
let colorStartList=["transparent",'transparent','transparent','transparent']
let colorLeftList=["#000","#000","#000","#000"]
// let colorTopList= ["#73beff",'#ffc083','#a2ff67','#c085ff']
// let colorEndList=["#73beff",'#ffc083','#a2ff67','#c085ff']
function colorRgba(str: string, alpha: string){
  let reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
  var sColor = str.toLowerCase();
  if(sColor=='transparent'){
    return 'transparent'
  }
  if(sColor && reg.test(sColor)){
    if(sColor.length === 4){
      var sColorNew = "#";
      for(let i=1; i<4; i+=1){
        sColorNew += sColor.slice(i,i+1).concat(sColor.slice(i,i+1));
      }
      sColor = sColorNew;
    }
    //处理六位的颜色值
    var sColorChange = [];
    for(let i=1; i<7; i+=2){
      sColorChange.push(parseInt("0x"+sColor.slice(i,i+2)));
    }
    return "rgba(" + sColorChange.join(",") +','+alpha+ ")";
  }else{
    return sColor;
  }
}
let colorTopList=props.chartConfig.option.dataset.chartObj.colorTopList
let colorEndList=props.chartConfig.option.dataset.chartObj.colorEndList

watch(
    () => props.chartConfig.option.dataset,
    newData => {
      // let colorTopList= ["#73beff",'#ffc083','#a2ff67','#c085ff']
      // let colorEndList=["#73beff",'#ffc083','#a2ff67','#c085ff']


      console.log('触发了watch')
      console.log("----props.chartConfig----")
      console.log(props.chartConfig)
      console.log("----vChartRef.value----")
      console.log(vChartRef.value)

      arr = [], maxList = [], list = [];
      props.chartConfig.option.legend.data=[]
      props.chartConfig.option.series=[]


      props.chartConfig.option.dataset.chartObj.series.forEach((ele: { data: any }, i: any) => {
        arr = arr.concat(ele.data);
      });

      maxList = props.chartConfig.option.dataset.chartObj.series[0].data.map(() => {
        return max;
      });

      list = props.chartConfig.option.dataset.chartObj.series[0].data.map((item: any, index: any) => {
        return 1;
      });

      props.chartConfig.option.dataset.chartObj.series.forEach((item: { name: any; data: any }, index: number) => {
        props.chartConfig.option.legend.data.push({
          name: item.name,
          itemStyle: {
            color: colorEndList[index % colorEndList.length],
          },
        });
        //背景顶部切片
        props.chartConfig.option.series.push({
          data: maxList,
          type: 'pictorialBar',
          barMaxWidth: fontSize * 3,
          itemStyle: {
            color: colorEndList[index],
            opacity: 0.1,
          },
          tooltip: {
            show: false,
          },
          symbolPosition: 'end',
          symbol: 'diamond',
          symbolOffset: [
            (-0.5 * (props.chartConfig.option.dataset.chartObj.series.length - 1) +
                index +
                -0.5 * barGap * 0.01 * (props.chartConfig.option.dataset.chartObj.series.length - 1) +
                barGap * 0.01 * index) *
            barWidth,
            '-50%',
          ],
          symbolSize: [barWidth, barWidth * 0.4],
          zlevel: -1,
        });
        //底部切片
        props.chartConfig.option.series.push({
          data: list,
          color: colorStartList[index % colorStartList.length],
          type: 'pictorialBar',
          tooltip: {
            show: false,
          },
          barMaxWidth: fontSize * 3,
          symbol: 'diamond',
          symbolOffset: [
            (-0.5 * (props.chartConfig.option.dataset.chartObj.series.length - 1) +
                index +
                -0.5 * barGap * 0.01 * (props.chartConfig.option.dataset.chartObj.series.length - 1) +
                barGap * 0.01 * index) *
            barWidth,
            '50%',
          ],
          symbolSize: [barWidth, barWidth * 0.5],
        });
        //实际数据顶部切片
        props.chartConfig.option.series.push({
          data: item.data,
          type: 'pictorialBar',
          tooltip: {
            show: false,
          },
          barMaxWidth: fontSize * 3,
          color: colorTopList[index],
          symbolPosition: 'end',
          symbol: 'diamond',
          symbolOffset: [
            (-0.5 * (props.chartConfig.option.dataset.chartObj.series.length - 1) +
                index +
                -0.5 * barGap * 0.01 * (props.chartConfig.option.dataset.chartObj.series.length - 1) +
                barGap * 0.01 * index) *
            barWidth,
            '-50%',
          ],
          symbolSize: [barWidth, barWidth * 0.4],
          zlevel: 2,
        });
        //实际数据侧边切片
        props.chartConfig.option.series.push({
          data: item.data,
          type: 'pictorialBar',
          tooltip: {
            show: false,
          },
          barMaxWidth: fontSize * 3,
          color: {
            x: 0,
            y: 1,
            x2: 0,
            y2: 0,
            type: 'linear',
            global: false,
            colorStops: [
              {
                offset: 0,
                color: 'transparent',
              },
              {
                offset: 0.2,
                color: colorRgba(colorLeftList[index % colorLeftList.length], String(0.2)),
              },
              {
                offset: 1,
                color: colorRgba(colorLeftList[index % colorLeftList.length], String(0.3)),
              },
            ],
          },
          symbolPosition: 'end',
          symbol: 'rect',
          symbolSize: [barWidth / 2, '100%'],
          symbolOffset: [
            (-0.5 * (props.chartConfig.option.dataset.chartObj.series.length - 1) +
                index +
                -0.5 * barGap * 0.01 * (props.chartConfig.option.dataset.chartObj.series.length - 1) +
                barGap * 0.01 * index -
                0.25) *
            barWidth,
            0,
          ],
          zlevel: 1,
        });
        //柱子
        props.chartConfig.option.series.push({
          data: item.data,
          type: 'bar',
          name: item.name,
          barGap: barGap + '%',
          barWidth: barWidth,
          barMaxWidth: fontSize * 3,
          label: {
            show: true,
            position: 'top',
            distance: fontSize * 0.3,
            textStyle: {
              color: colorEndList[index % colorEndList.length],
              fontSize: fontSize,
            },
            // formatter: function (a, b) {
            //     return a.value==0?'':a.value;
            // },
          },
          showBackground: true,
          backgroundStyle: {
            color: {
              x: 0,
              y: 1,
              x2: 0,
              y2: 0,
              type: 'linear',
              global: false,
              colorStops: [
                {
                  offset: 0,
                  color: colorStartList[index % colorStartList.length],
                },
                {
                  offset: 0.2,
                  color: colorRgba(colorEndList[index % colorEndList.length], String(0.2)),
                },
                {
                  offset: 1,
                  color: colorRgba(colorEndList[index % colorEndList.length], String(1)),
                },
              ],
            },
            opacity: 0.1,
          },
          itemStyle: {
            color: {
              x: 0,
              y: 1,
              x2: 0,
              y2: 0,
              type: 'linear',
              global: false,
              colorStops: [
                {
                  offset: 0,
                  color: colorStartList[index % colorStartList.length],
                },
                {
                  offset: 0.2,
                  color: colorRgba(colorEndList[index % colorEndList.length], String(0.2)),
                },
                {
                  offset: 1,
                  color: colorRgba(colorEndList[index % colorEndList.length], String(1)),
                },
              ],
            },
          },
        });
        props.chartConfig.option.xAxis.data=props.chartConfig.option.dataset.chartObj.chartList
      });


    }

);
</script>
