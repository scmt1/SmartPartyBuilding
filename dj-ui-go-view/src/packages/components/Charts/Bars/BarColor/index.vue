<!-- /***
*
* 功能文件名：展示渲染文件
* 创建日期：2023-02-14
* 创建人：LuoCX
***/  -->

<template>
    <!--  // 使用 vue-echarts 框架进行渲染 -->
    <v-chart ref="vChartRef" :theme="themeColor" :option="option.value"
             :update-options="{
      replaceMerge: replaceMergeArr
    }" autoresize></v-chart>
</template>

<script setup lang="ts">
    import {nextTick, PropType, reactive, ref, watch} from 'vue'
    import VChart from 'vue-echarts'
    import {use} from 'echarts/core'
    import {CanvasRenderer} from 'echarts/renderers'
    import {BarChart} from 'echarts/charts'
    import {mergeTheme} from '@/packages/public/chart'
    import config, {includes} from './config'
    import {useChartDataFetch} from '@/hooks'
    import {useChartEditStore} from '@/store/modules/chartEditStore/chartEditStore'
    import {
        DatasetComponent,
        GridComponent,
        LegendComponent,
        TitleComponent,
        TooltipComponent
    } from 'echarts/components'
    import isObject from "lodash/isObject";
    import {seriesItem} from "@/packages/components/Charts/Bars/BarCommon/config";

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
    use([DatasetComponent, CanvasRenderer, BarChart, GridComponent, TooltipComponent, LegendComponent, TitleComponent])
    const vChartRef = ref<typeof VChart>()
    const replaceMergeArr = ref<string[]>()
    // 应用到图表的 option
    const option = reactive({
        value: {}
    })

    const dataHandle = (newData: any) => {
        if (newData.className) {
            props.chartConfig.option.yAxis[0].data = newData.className
            props.chartConfig.option.yAxis[0].axisLabel.textStyle.color = "#ffffff"
        }
        props.chartConfig.option.series[0].name = newData.seriesName[0]
        props.chartConfig.option.series[1].name = newData.seriesName[1]
        props.chartConfig.option.series[2].name = newData.seriesName[2]
        props.chartConfig.option.series[3].name = newData.seriesName[3]
        props.chartConfig.option.series[0].data = newData.data1
        props.chartConfig.option.series[1].data = newData.data2
        props.chartConfig.option.series[2].data = newData.data3
        props.chartConfig.option.series[3].data = newData.data4
        option.value = mergeTheme(props.chartConfig.option, props.themeSetting, includes)
        option.value = props.chartConfig.option
        vChartRef.value?.setOption(props.chartConfig.option);
    }

    // 配置时
    watch(
        () => props.chartConfig.option.dataset,
        (newData: { dimensions: any }, oldData) => {
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
    const onRestart = async () => {
        setInterval(function () {
            if (props.chartConfig.option.dataZoom[0].endValue == props.chartConfig.option.dataset.className.length) {
                props.chartConfig.option.dataZoom[0].endValue = 4;
                props.chartConfig.option.dataZoom[0].startValue = 0;
            } else {
                props.chartConfig.option.dataZoom[0].endValue = props.chartConfig.option.dataZoom[0].endValue + 1;
                props.chartConfig.option.dataZoom[0].startValue = props.chartConfig.option.dataZoom[0].startValue + 1;
            }
            vChartRef.value?.dispatchAction({
                type: 'showTip',
                seriesIndex: 0,
                dataIndex: props.chartConfig.option.dataZoom[0].startValue + 1,
            });
            vChartRef.value?.setOption(props.chartConfig.option);
        }, 2000);
    }
    onRestart()
    // 预览时
    useChartDataFetch(props.chartConfig, useChartEditStore)
</script>
