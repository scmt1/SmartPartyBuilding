
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
 import { PropType, reactive, watch, ref, nextTick } from 'vue'
import config, { includes } from './config'
import VChart from 'vue-echarts'
import 'echarts-gl'
import { use, registerMap } from 'echarts/core'
import { EffectScatterChart, MapChart } from 'echarts/charts'
import { CanvasRenderer } from 'echarts/renderers'
import { useChartDataFetch } from '@/hooks'
import { mergeTheme } from '@/packages/public/chart'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { isPreview } from '@/utils'
import mapJsonWithoutHainanIsLands from './data.json'
import { DatasetComponent, GridComponent, TooltipComponent, GeoComponent, VisualMapComponent } from 'echarts/components'

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
  use([
    MapChart,
    DatasetComponent,
    CanvasRenderer,
    GridComponent,
    TooltipComponent,
    GeoComponent,
    EffectScatterChart,
    VisualMapComponent
  ])
  // 应用到图表的 option
  const option = reactive({
    value: mergeTheme(props.chartConfig.option, props.themeSetting, includes)
  })
  const vChartRef = ref<typeof VChart>()
  
//异步时先注册空的 保证初始化不报错
  registerMap('泸州', { geoJSON:{} as any, specialAreas: {} })


  // const dataHandle = (newData: any) => {
  //   let config = props.chartConfig.option

  //   option.value = mergeTheme(props.chartConfig.option, props.themeSetting, includes)
  //   option.value = props.chartConfig.option
  // }

  // 处理
const hainanLandsHandle = async (newData: boolean) => {
    registerMap('泸州', { geoJSON: mapJsonWithoutHainanIsLands as any, specialAreas: {} })
}
  // 手动触发渲染
const vEchartsSetOption = () => {
  option.value = props.chartConfig.option
  vChartRef.value?.setOption(props.chartConfig.option)

}
  // 配置时
  watch(
    () => props.chartConfig.option.mapRegion.showHainanIsLands,
    async newData => {
      try {
        await hainanLandsHandle(newData)
        vEchartsSetOption()
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
    let d = parseFloat(`${resData}`) * 100
    // // @ts-ignore
    // option.value.title.text = d.toFixed(2) + '%'
    // // @ts-ignore
    // option.value.series[0].data[0].value[0] = d
    // // @ts-ignore
    // option.value.series[0].data[1].value[0] = 100 - d
  })
  </script>
  