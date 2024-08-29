<template>
    <v-chart ref="vChartRef" :theme="themeColor" :option="option" autoresize></v-chart>
</template>

<script setup lang="ts">
import { computed, PropType, watch } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { RadarChart, SunburstChart } from 'echarts/charts'
import { useChartDataFetch } from '@/hooks'
import config, { includes, setRadarData, setSunburst } from './config'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'

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

const option = computed(() => {
    return props.chartConfig.option
})

watch(()=> props.chartConfig.option.dataset,
    (newData) => {
        let optionValue = props.chartConfig.option
        optionValue.radar.indicator = newData.radarIndicator
        optionValue.series[0].data = setRadarData(newData.seriesRadarData)
        optionValue.series[1].data = setSunburst(newData.seriesSunburstData)
    },
    {
        immediate: true
    }
)

use([CanvasRenderer, RadarChart, SunburstChart])

useChartDataFetch(props.chartConfig, useChartEditStore, (newData: any) => {

})
</script>

