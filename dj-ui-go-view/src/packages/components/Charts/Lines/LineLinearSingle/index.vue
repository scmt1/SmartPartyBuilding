<template>
  <v-chart ref="vChartRef" :theme="themeColor" :option="option.value" :manual-update="isPreview()" autoresize>
  </v-chart>
</template>

<script setup lang="ts">
import { PropType, watch, reactive } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import config, { includes } from './config'
import { mergeTheme } from '@/packages/public/chart'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { chartColorsSearch, defaultTheme } from '@/settings/chartThemes/index'
import { DatasetComponent, GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import { useChartDataFetch } from '@/hooks'
import { isPreview } from '@/utils'

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

use([DatasetComponent, CanvasRenderer, LineChart, GridComponent, TooltipComponent, LegendComponent])

const chartEditStore = useChartEditStore()
const option = reactive({
  value: {}
})

// 初始化与渐变色处理
watch(
  () => chartEditStore.getEditCanvasConfig.chartThemeColor,
  (newColor: keyof typeof chartColorsSearch) => {
    try {
      if (!isPreview()) {
        const themeColor = chartColorsSearch[newColor] || chartColorsSearch[defaultTheme]
        props.chartConfig.option.series.forEach((value: any) => {
          value.lineStyle.shadowColor = themeColor[2]
          value.lineStyle.color.colorStops.forEach((v: { color: string }, i: number) => {
            v.color = themeColor[i]
          })
        })
      }
      option.value = mergeTheme(props.chartConfig.option, props.themeSetting, includes)
      props.chartConfig.option = option.value
    } catch (error) {
      console.log(error)
    }
  },
  {
    immediate: true
  }
)

watch(
  () => props.chartConfig.option.dataset,
  () => {
    option.value = props.chartConfig.option
  },
  {
    deep: false
  }
)
//循环轮播效果
const onRestart = async () => {
  if (props.chartConfig.option && typeof props.chartConfig.option === 'object') {
    setInterval(function(){
      if (props.chartConfig.option.dataZoom[0].endValue == props.chartConfig.option.dataset.source.length ) {
        props.chartConfig.option.dataZoom[0].endValue = 5;
        props.chartConfig.option.dataZoom[0].startValue = 0;
      } else {
        props.chartConfig.option.dataZoom[0].endValue = props.chartConfig.option.dataZoom[0].endValue + 1;
        props.chartConfig.option.dataZoom[0].startValue = props.chartConfig.option.dataZoom[0].startValue + 1;
      }
      vChartRef.value?.setOption(props.chartConfig.option);
    }, 2000)
  }
}
onRestart()
const { vChartRef } = useChartDataFetch(props.chartConfig, useChartEditStore)
</script>
