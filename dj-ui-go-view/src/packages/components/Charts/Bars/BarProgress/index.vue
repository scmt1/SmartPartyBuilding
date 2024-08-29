<template>
    <v-chart :theme="themeColor" :option="option.value" autoresize></v-chart>
</template>

<script setup lang="ts">
import {PropType, reactive, watch} from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import 'echarts-liquidfill/src/liquidFill.js'
import { CanvasRenderer } from 'echarts/renderers'
import config from './config'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { useChartDataFetch } from '@/hooks'
import {GridComponent, TitleComponent} from "echarts/components";

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

use([CanvasRenderer, GridComponent, TitleComponent])

const option = reactive({
    value: {}
})

watch(
    ()=> props.chartConfig.option.dataset,
    newDate => {
        const value = Number(newDate.value)
        let series = props.chartConfig.option.series

        series[1].data[0].value = value
        series[2].data[0] = 100 - value
        series[3].data[0] = value
        series[4].data[0] = 100 - value

        option.value = props.chartConfig.option
    },
    { deep: false, immediate: true }
)

useChartDataFetch(props.chartConfig, useChartEditStore, (newData: [any]) => {

})

</script>
