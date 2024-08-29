<template>
    <v-chart
        :theme="themeColor"
        :option="option.value"
        autoresize>
    </v-chart>
</template>

<script setup lang="ts">
import {PropType, reactive, watch} from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import 'echarts-liquidfill/src/liquidFill.js'
import { CanvasRenderer } from 'echarts/renderers'
import { GridComponent, TitleComponent } from 'echarts/components'
import config from './config'
import { isPreview } from '@/utils'
import { chartColorsSearch, defaultTheme } from '@/settings/chartThemes/index'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { useChartDataFetch } from '@/hooks'
import {direction} from "./config";

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

const chartEditStore = useChartEditStore()

const option:any = reactive({
    value: {}
})

// 渐变色处理
watch(
    () => chartEditStore.getEditCanvasConfig.chartThemeColor,
    (newColor: keyof typeof chartColorsSearch) => {
        try {
            if (!isPreview()) {
                const themeColor = chartColorsSearch[newColor] || chartColorsSearch[defaultTheme]
                // 背景颜色
                props.chartConfig.option.series[0].backgroundStyle.color = themeColor[2]
            }
            option.value = props.chartConfig.option
        } catch (error) {
            console.log(error)
        }
    },
    {
        immediate: true
    }
)

const dataHandle = (newData: [any] | string) => {
    let data = [
        {
            value: newData[0].value = Number(newData[0].value)+0.02,
            direction: newData[0].direction
        },
        {
            value: newData[0].value = Number(newData[0].value)-0.01,
            direction: newData[1].direction
        },
        {
            value: newData[0].value = Number(newData[0].value)-0.01,
            direction: newData[2].direction
        },
    ]
    return data
}

watch(
    () => props.chartConfig.option.dataset,
    newData => {
        props.chartConfig.option.title[0].text = newData[1].value
        props.chartConfig.option.title[1].text = newData[2].value
        props.chartConfig.option.title[2].text = (Number(newData[0].value) * 100).toFixed(0)+'%'

        props.chartConfig.option.series[0].data = dataHandle(newData)
        option.value = props.chartConfig.option
    },
    {
        immediate: true,
        deep: false
    }
)

useChartDataFetch(props.chartConfig, useChartEditStore, (newData: any) => {
    let data = [
        {
            value: newData[0].value = Number(newData[0].value),
            direction: direction[1].value
        },
        {
            value: newData[0].value = Number(newData[0].value),
            direction: direction[0].value
        },
        {
            value: newData[0].value = Number(newData[0].value),
            direction: direction[1].value
        },
    ]
    // @ts-ignore
    option.value.series[0].data = dataHandle(data)

    option.value.title[0].text = newData[1].value
    option.value.title[1].text = newData[2].value
    option.value.title[2].text = (Number(newData[0].value) * 100).toFixed(0)+'%'
})
</script>
