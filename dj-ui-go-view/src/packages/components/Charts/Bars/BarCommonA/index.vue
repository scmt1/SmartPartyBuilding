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
import { mergeTheme } from '@/packages/public/chart'
import { useChartDataFetch } from '@/hooks'
import { CreateComponentType } from '@/packages/index.d'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { isPreview } from '@/utils'
import { DatasetComponent, GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import isObject from 'lodash/isObject'
import dataJson from "@/packages/components/Charts/Bars/BarCommon/data.json";

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
const vChartRef = ref<typeof VChart>()
const replaceMergeArr = ref<string[]>()

const option = computed(() => {
  return mergeTheme(props.chartConfig.option, props.themeSetting, includes)
})

// dataset 无法变更条数的补丁
watch(
  () => props.chartConfig.option.dataset,
  (newData: { dimensions: any }, oldData) => {
    try {
      props.chartConfig.option.xAxis[0].data=props.chartConfig.option.dataset.xData;
      props.chartConfig.option.xAxis[1].data=props.chartConfig.option.dataset.xData;
      props.chartConfig.option.series[0].data=props.chartConfig.option.dataset.yData1;
      props.chartConfig.option.series[2].data=props.chartConfig.option.dataset.yData1;
      props.chartConfig.option.series[1].data=props.chartConfig.option.dataset.yData2;
      props.chartConfig.option.series[3].data=props.chartConfig.option.dataset.yData3;
      props.chartConfig.option.series[0].name=props.chartConfig.option.dataset.legendData[0];
      props.chartConfig.option.series[1].name=props.chartConfig.option.dataset.legendData[1];
      props.chartConfig.option.series[2].name=props.chartConfig.option.dataset.legendData[0];
      props.chartConfig.option.series[3].name=props.chartConfig.option.dataset.legendData[1];
      props.chartConfig.option.legend.data=props.chartConfig.option.dataset.legendData;
    } catch (error) {
      console.log(error)
    }
  },
  {
    deep: false
  }
)
watch(
  () => props.chartConfig.option.series[0].symbolSize[0],
  (newData: any, oldData) => {
    try {
      props.chartConfig.option.series[0].symbolSize[0]=newData;
      props.chartConfig.option.series[1].symbolSize[0]=newData;
      props.chartConfig.option.series[2].barWidth=newData;
      props.chartConfig.option.series[3].barWidth=newData;
      props.chartConfig.option.series[4].barWidth=newData;
      props.chartConfig.option.series[5].barWidth=newData;
      props.chartConfig.option.series[6].symbolSize[0]=newData;
      props.chartConfig.option.series[7].symbolSize[0]=newData;
      props.chartConfig.option.series[8].symbolSize[0]=newData/2;
      props.chartConfig.option.series[9].symbolSize[0]=newData/2;
    } catch (error) {
      console.log(error)
    }
  },
  {
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
useChartDataFetch(props.chartConfig, useChartEditStore)
</script>
