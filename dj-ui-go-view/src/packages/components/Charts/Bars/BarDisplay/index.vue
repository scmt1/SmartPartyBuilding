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
const { vChartRef } = useChartDataFetch(props.chartConfig, useChartEditStore)

console.log("----原始props.chartConfig----")
console.log(props.chartConfig)
console.log("----原始vChartRef.value----")
console.log(vChartRef.value)
watch(
    () => props.chartConfig.option.dataset,
    newData => {
      props.chartConfig.option.options[0].series[0].data=props.chartConfig.option.dataset.dataStrat
      props.chartConfig.option.options[0].xAxis[0].data=props.chartConfig.option.dataset.datax
      props.chartConfig.option.options[1]=props.chartConfig.option.dataset.source[0]
      props.chartConfig.option.options[2]=props.chartConfig.option.dataset.source[1]
      props.chartConfig.option.options[3]=props.chartConfig.option.dataset.source[2]
      props.chartConfig.option.options[4]=props.chartConfig.option.dataset.source[3]
      props.chartConfig.option.options[5]=props.chartConfig.option.dataset.source[4]
      props.chartConfig.option.options[6]=props.chartConfig.option.dataset.source[5]
      vChartRef.value?.setOption(props.chartConfig.option);
      console.log("----props.chartConfig----")
      console.log(props.chartConfig)
      console.log("----vChartRef.value----")
      console.log(vChartRef.value)
    },
    {
      immediate: false,
      deep: false
    }
)

</script>
