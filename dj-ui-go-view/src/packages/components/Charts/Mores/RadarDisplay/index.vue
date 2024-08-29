<template>
  <v-chart ref="vChartRef" :theme="themeColor" :option="option" :manual-update="isPreview()" autoresize></v-chart>
</template>

<script setup lang="ts">
import {ref, computed, PropType, watch} from 'vue'
import VChart from 'vue-echarts'
import dataJson from './data.json'
import {use} from 'echarts/core'
import {CanvasRenderer} from 'echarts/renderers'
import {RadarChart} from 'echarts/charts'
import {includes} from './config'
import {mergeTheme} from '@/packages/public/chart'
import {useChartDataFetch} from '@/hooks'
import {CreateComponentType} from '@/packages/index.d'
import {useChartEditStore} from '@/store/modules/chartEditStore/chartEditStore'
import {isPreview} from '@/utils'
import {DatasetComponent, GridComponent, TooltipComponent, LegendComponent} from 'echarts/components'

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
    type: Object as PropType<CreateComponentType>,
    required: true
  }
})

use([DatasetComponent, CanvasRenderer, RadarChart, GridComponent, TooltipComponent, LegendComponent])

const vChartRef = ref<typeof VChart>()

const option = computed(() => {
  return mergeTheme(props.chartConfig.option, props.themeSetting, includes)
})


watch(
    () => props.chartConfig.option.dataset,
    newData => {
      console.log(" props.chartConfig.option")
      console.log(props.chartConfig.option)

      // 更新timelineData
      props.chartConfig.option.baseOption.timeline.data = props.chartConfig.option.dataset.timelineData
      // 更新legendData
      props.chartConfig.option.baseOption.legend.data = props.chartConfig.option.dataset.legendData
      // 更新optionsData
      props.chartConfig.option.options = props.chartConfig.option.dataset.optionsData
      //置空
      // 更新seriesData
      // props.chartConfig.option.baseOption.series = props.chartConfig.option.dataset.seriesData;
      //置空
      props.chartConfig.option.baseOption.series = []

      for (let i = 0; i < props.chartConfig.option.dataset.legendData.length; i++) {
        props.chartConfig.option.baseOption.series.push({
          name: props.chartConfig.option.dataset.legendData[i],
          type: 'radar',
          itemStyle: {
            color: '#6DCDE6'
          },
          areaStyle: {
            opacity: .4,
            color: {
              type: 'radial',
              x: 0.5,
              y: 0.5,
              r: 0.5,
              colorStops: [{
                offset: 0,
                color: '#38A0D6' // 0% 处的颜色
              }, {
                offset: 1,
                color: '#61C3E2' // 100% 处的颜色
              }],
              global: false // 缺省为 false
            }
          }
        })
      }
      vChartRef.value?.setOption(props.chartConfig.option);
      console.log("vChartRef.value")
      console.log(vChartRef.value)

    },
    {
      deep: false,
      immediate: false
    }
)


</script>
