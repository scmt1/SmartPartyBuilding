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
watch(
    () => props.chartConfig.option.dataset,
    newData => {
      props.chartConfig.option.series[0].data=props.chartConfig.option.dataset.yData1
      props.chartConfig.option.series[2].data=props.chartConfig.option.dataset.yData1
      props.chartConfig.option.series[1].data=props.chartConfig.option.dataset.yData2
      props.chartConfig.option.series[3].data=props.chartConfig.option.dataset.yData2
      props.chartConfig.option.xAxis[0].data=props.chartConfig.option.dataset.xData
      props.chartConfig.option.xAxis[1].data=props.chartConfig.option.dataset.xData
    },
    {
      immediate: false,
      deep: false
    }
)
var total = props.chartConfig.option.dataset.xData.length;
var count = 0;
const onRestart = async () => {
  setInterval(function () {
    var curr = count % total;
    vChartRef.value?.dispatchAction({
      type: 'showTip',
      seriesIndex: 0,
      dataIndex: curr,
    });
    count += 1;
    vChartRef.value?.setOption(props.chartConfig.option);
  }, 2000);
}
onRestart()
</script>
