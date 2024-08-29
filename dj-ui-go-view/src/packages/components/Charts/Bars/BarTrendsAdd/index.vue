<template>
  <v-chart
      ref="vChartRef"
      :theme="themeColor"
      :option="option"
      :update-options="{
      replaceMerge: replaceMergeArr
    }"
      autoresize
  ></v-chart>
</template>

<script setup lang="ts">
import VChart from 'vue-echarts'
import {computed, PropType, ref, watch} from 'vue'
import {use} from 'echarts/core'
import {CanvasRenderer} from 'echarts/renderers'
import {BarChart} from 'echarts/charts'
import {mergeTheme} from '@/packages/public/chart'
import config, {includes} from './config'
import {useChartDataFetch} from '@/hooks'
import {useChartEditStore} from '@/store/modules/chartEditStore/chartEditStore'
import {DatasetComponent, GridComponent, LegendComponent, TooltipComponent} from 'echarts/components'


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

let interval: any = setInterval(function () {
},4000)
watch(
    () => props.chartConfig.option.sort,
    (newData: any) => {
      console.log("进入watch了")
      // 重置数据和定时任务
      clearInterval(interval)
      let data1: any[] = [];
      props.chartConfig.option.series[0].data = [];


      for (let i = 0; i < props.chartConfig.option.dataset.value.length; i++) {
        data1.push([props.chartConfig.option.dataset.value[i], props.chartConfig.option.dataset.name[i]]);
      }
     interval = setInterval(function () {
        //判断上一次插入的是否为最后一条数据
        if (data1.length === 0) {
          //清空柱状图数据
          props.chartConfig.option.series[0].data = [];
          //重新为data1装载数据
          for (let i = 0; i < props.chartConfig.option.dataset.value.length; i++) {
            data1.push([props.chartConfig.option.dataset.value[i], props.chartConfig.option.dataset.name[i]]);
          }
        }
       //排序(当开关打开且数据满加载时)
       if (props.chartConfig.option.sort&&data1.length==props.chartConfig.option.dataset.value.length) {
         data1.sort(function (a, b) {
           return b[0] - a[0];
         });
       }
       //从data1弹出一组数据，插入组件data中
        props.chartConfig.option.series[0].data.push(data1.pop());
        //setOption渲染数据
        vChartRef.value?.setOption(props.chartConfig.option);
      }, 1500);
    },
    {
      immediate: true
    }
);

const {vChartRef} = useChartDataFetch(props.chartConfig, useChartEditStore)
</script>
