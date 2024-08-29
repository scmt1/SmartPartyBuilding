<template>
  <v-chart ref="vChartRef" :theme="themeColor" :option="option" :manual-update="isPreview()" autoresize></v-chart>
</template>

<script setup lang="ts">
import { computed, PropType, reactive, watch } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
import { mergeTheme } from '@/packages/public/chart'
import config, { includes } from './config'
import { useChartDataFetch } from '@/hooks'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { isPreview } from '@/utils'
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

use([DatasetComponent, CanvasRenderer, PieChart, GridComponent, TooltipComponent, LegendComponent])

const option = computed(() => {
  return mergeTheme(props.chartConfig.option, props.themeSetting, includes)
})

watch(
  () => props.chartConfig.option.type,
  newData => {
    try {
      if (newData === 'nomal') {
        props.chartConfig.option.series[0].radius = '70%'
        props.chartConfig.option.series[0].roseType = false
      } else if (newData === 'ring') {
        props.chartConfig.option.series[0].radius = ['40%', '65%']
        props.chartConfig.option.series[0].roseType = false
      } else {
        props.chartConfig.option.series[0].radius = '70%'
        props.chartConfig.option.series[0].roseType = true
      }
    } catch (error) {
      console.log(error)
    }
  },
  { deep: false, immediate: true }
)
var charPie3currentIndex = 0;

setInterval(function () {
  var dataLen = props.chartConfig.option.dataset.source.length;
  // 取消之前高亮的图形
  vChartRef.value?.dispatchAction({
    type: 'downplay',
    seriesIndex: 0,
    dataIndex: charPie3currentIndex
  });
  charPie3currentIndex = (charPie3currentIndex + 1) % dataLen;
  // 高亮当前图形
  vChartRef.value?.dispatchAction({
    type: 'highlight',
    seriesIndex: 0,
    dataIndex:charPie3currentIndex
  });
  vChartRef.value?.dispatchAction({
    type: 'showTip',
    seriesIndex: 0,
    dataIndex: charPie3currentIndex,
  });
}, 2000);
const { vChartRef } = useChartDataFetch(props.chartConfig, useChartEditStore)
</script>
