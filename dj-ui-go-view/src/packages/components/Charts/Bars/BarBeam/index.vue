<template>
  <v-chart
    :theme="themeColor"
    :option="option.value"
    autoresize
  ></v-chart>
</template>

<script setup lang="ts">
  import {ref, computed, watch, PropType, reactive} from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import config, { includes } from './config'
import { mergeTheme } from '@/packages/public/chart'
import { useChartDataFetch } from '@/hooks'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { DatasetComponent, GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'

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

  // 应用到图表的 option
const option = reactive({
  value: {}
})
const dataHandle = (newData: any) => {
  props.chartConfig.option.series[0].data[0].value = props.chartConfig.option.series[2].data[0].value;
  option.value = mergeTheme(props.chartConfig.option, props.themeSetting, includes)
  option.value = props.chartConfig.option
}
// dataset 无法变更条数的补丁
watch(
  () => props.chartConfig.option.dataset,
  newData => {
    try {
      dataHandle(newData)
    } catch (error) {
      console.log(error)
    }
  },
  {
    immediate: true,
    deep: false
  }
)
useChartDataFetch(props.chartConfig, useChartEditStore)
</script>
