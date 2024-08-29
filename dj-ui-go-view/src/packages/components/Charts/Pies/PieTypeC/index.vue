
<!-- /***
*
* 功能文件名：展示渲染文件
* 创建日期：2023-02-14
* 创建人：LuoCX
***/  -->

<template>
    <!--  // 使用 vue-echarts 框架进行渲染 -->
    <v-chart ref="vChartRef" :theme="themeColor" :option="option.value" autoresize> </v-chart>
</template>

  <script setup lang="ts">
      import {PropType, reactive, ref, watch} from 'vue'
  import VChart from 'vue-echarts'
  import { use } from 'echarts/core'
  import { CanvasRenderer } from 'echarts/renderers'
  import { PieChart } from 'echarts/charts'
  import { mergeTheme } from '@/packages/public/chart'
  import config, { includes } from './config'
  import { useChartDataFetch } from '@/hooks'
  import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
  import { DatasetComponent, GridComponent, TooltipComponent, LegendComponent, TitleComponent } from 'echarts/components'

  // 这里的 themeSetting、themeColor的作用是监听全局样式主题并进行设置，chartConfig 为图表的配置项数据
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

  // 动态注册 ECharts 图表内容
  use([DatasetComponent, CanvasRenderer, PieChart, GridComponent, TooltipComponent, LegendComponent, TitleComponent])
  const vChartRef = ref<typeof VChart>()
  // 应用到图表的 option
  const option = reactive({
    value: {}
  })

  const dataHandle = (newData: any) => {
    option.value = mergeTheme(props.chartConfig.option, props.themeSetting, includes)
    option.value = props.chartConfig.option
  }

  // 配置时
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
  // 预览时
  useChartDataFetch(props.chartConfig, useChartEditStore, (resData: number) => {
  })
  </script>
