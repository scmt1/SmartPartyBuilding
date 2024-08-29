<template>
    <v-chart :theme="themeColor" :option="option" :key="option.graphic.elements[0].children" autoresize></v-chart>
</template>

<script setup lang="ts">
import {computed, PropType, ref, watch} from 'vue'
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

const option = computed(()=>{
    return props.chartConfig.option
})

useChartDataFetch(props.chartConfig, useChartEditStore, (newData: [any]) => {

})

</script>
