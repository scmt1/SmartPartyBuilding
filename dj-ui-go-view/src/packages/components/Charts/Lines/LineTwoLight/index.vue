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
  >
  </v-chart>
</template>

<script setup lang="ts">
  import {PropType, computed, watch, ref, nextTick, reactive} from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import config, { includes} from './config'
import { mergeTheme } from '@/packages/public/chart'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { useChartDataFetch } from '@/hooks'
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

use([DatasetComponent, CanvasRenderer, LineChart, GridComponent, TooltipComponent, LegendComponent])

const replaceMergeArr = ref<string[]>()

const option = computed(() => {
  return mergeTheme(props.chartConfig.option, props.themeSetting, includes)
})

// dataset 无法变更条数的补丁
watch(
  () => props.chartConfig.option.dataset,
  (newData: any, oldData) => {
    try {
      props.chartConfig.option.series[2].data[0].coords = newData.coords1;
      props.chartConfig.option.series[3].data[0].coords = newData.coords2;
    } catch (error) {
      console.log(error)
    }
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
      /*提示框跟随*/
      vChartRef.value?.dispatchAction({
        type: 'showTip',
        seriesIndex: 0,
        dataIndex: props.chartConfig.option.dataZoom[0].startValue +1,
      });
      vChartRef.value?.setOption(props.chartConfig.option);
    }, 2000)
  }
}
onRestart()
const { vChartRef } = useChartDataFetch(props.chartConfig, useChartEditStore)
</script>
