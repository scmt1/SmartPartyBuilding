<template>
    <v-chart :theme="themeColor" :option="option.value" autoresize></v-chart>
</template>

<script setup lang="ts">
import {computed, PropType, reactive, ref, watch} from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import 'echarts-liquidfill/src/liquidFill.js'
import { CanvasRenderer } from 'echarts/renderers'
import { DatasetComponent, BrushComponent, TooltipComponent } from 'echarts/components'
import config from './config'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { useChartDataFetch } from '@/hooks'

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

let key = ref(1)

use([DatasetComponent, CanvasRenderer, BrushComponent, TooltipComponent])

const getItemList:any = (itemFirst: any, newDate: any, color: []) =>{
    let itemFirstR:any = Number(itemFirst.r)
    let itemList = []
    const size = 30
    for (let i = 0; i < newDate.maxLevels; i++) {
        itemList.push({
            r0: itemFirstR+size*i,
            r: itemFirstR+size*(i+1),
            itemStyle: {
                color: color[i%color.length]
            }
        })
    }
    return itemList
}

const option = reactive({
    value: {}
})

watch(
    () => props.chartConfig.option.dataset,
    (newData) => {
        try {
            props.chartConfig.option.series.data = newData
            props.chartConfig.option.series.levels = [
                {},
                props.chartConfig.option.series.levels[1]
            ].concat(getItemList(props.chartConfig.option.series.levels[1], newData[0], props.chartConfig.option.color))
            option.value = props.chartConfig.option
        } catch (error) {
            console.log(error)
        }
    },
    {
        immediate: true
    }
)

watch(
    () => props.chartConfig.option.color,
    (newData) => {
        try {
            let itemList = props.chartConfig.option.series.levels
            if (itemList.length < 3) {
                return
            }
            for (let i = 2; i < itemList.length; i++) {
                itemList[i].itemStyle.color = props.chartConfig.option.color[(i-2)%props.chartConfig.option.color.length]
            }
            props.chartConfig.option.series
        } catch (error) {
            console.log(error)
        }
    },
    {
        immediate: true,
        deep: true
    }
)

useChartDataFetch(props.chartConfig, useChartEditStore, (newData: any) => {

})
</script>

