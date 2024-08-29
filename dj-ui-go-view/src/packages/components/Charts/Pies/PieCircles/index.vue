<template>
<!--使用 vue-echarts 框架进行渲染-->
  <v-chart ref="vChartRef" :theme="themeColor" :option="option" :manual-update="isPreview()" autoresize></v-chart>
</template>

<script setup lang="ts">
import { computed, PropType, reactive, watch } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
import config, { includes } from './config'
// import { CreateComponentType } from '@/packages/index.d'
import { mergeTheme } from '@/packages/public/chart'
import { useChartDataFetch } from '@/hooks'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { isPreview } from '@/utils'
import { DatasetComponent, GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
// 这里的 themeSetting、themeColor的作用是监听全局样式主题并进行设置，chartConfig 为图表
// 的配置项数据
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
    //与import连用（官方版）
    // type: Object as PropType<CreateComponentType>,
    required: true
  }
})
// 动态注册 ECharts 图表内容
use([DatasetComponent, CanvasRenderer, PieChart, GridComponent, TooltipComponent, LegendComponent])

// 应用到图表的 option
const option = computed(() => {
  return mergeTheme(props.chartConfig.option, props.themeSetting, includes)
})

watch(
  () => props.chartConfig.option.type,
  newData => {
    try {
      // if (newData === 'nomal') {
      //   props.chartConfig.option.series[0].radius = '70%'
      //   props.chartConfig.option.series[0].roseType = false
      // } else if (newData === 'ring') {
      //   props.chartConfig.option.series[0].radius = ['40%', '65%']
      //   props.chartConfig.option.series[0].roseType = false
      // } else {
      //   props.chartConfig.option.series[0].radius = '70%'
      //   props.chartConfig.option.series[0].roseType = false
      // }
    } catch (error) {
      console.log(error)
    }
  },
  { deep: false, immediate: true }
)

const { vChartRef } = useChartDataFetch(props.chartConfig, useChartEditStore)
</script>
