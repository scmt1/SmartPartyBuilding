<template>
    <v-chart :theme="themeColor" :option="option" autoresize></v-chart>
</template>

<script setup lang="ts">
import {computed, PropType, reactive, watch} from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart, GaugeChart } from 'echarts/charts'
import config, { includes } from './config'
import { useChartDataFetch } from '@/hooks'
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

use([CanvasRenderer, PieChart, BarChart, GaugeChart])

const option = computed(() => {
    return props.chartConfig.option
})

watch(
    () => props.chartConfig.option.dataset,
    newData => {
        try {
            props.chartConfig.option.series[0].data[0].value = newData
            props.chartConfig.option.series[2].data[0].value = newData
            props.chartConfig.option.series[4].data[0].value = newData
        } catch (error) {
            console.log(error)
        }
    },
    { deep: false, immediate: true }
)

const dataHandle = (newData: any) => {

}
useChartDataFetch(props.chartConfig, useChartEditStore, (newData: any) => {
    props.chartConfig.option.series[0].data[0].value = newData
    props.chartConfig.option.series[2].data[0].value = newData
    props.chartConfig.option.series[4].data[0].value = newData
})
</script>
