<template>
    <!--使用 vue-echarts 框架进行渲染-->
    <v-chart ref="vChartRef" :theme="themeColor" :option="option" :manual-update="isPreview()" autoresize></v-chart>
</template>

<script setup lang="ts">
    import { computed, PropType, reactive, watch } from 'vue'
    import VChart from 'vue-echarts'
    import { use } from 'echarts/core'
    import { CanvasRenderer } from 'echarts/renderers'
    import { PieChart } from 'echarts/charts'
    import config, { includes } from './config'
    // import { CreateComponentType } from '@/packages/index.d'
    import { mergeTheme } from '@/packages/public/chart'
    import { useChartDataFetch } from '@/hooks'
    import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
    import { isPreview } from '@/utils'
    import { DatasetComponent, GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
    // 这里的 themeSetting、themeColor的作用是监听全局样式主题并进行设置，chartConfig 为图表
    // 的配置项数据
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
            //与import连用（官方版）
            // type: Object as PropType<CreateComponentType>,
            required: true
        }
    })
    // 动态注册 ECharts 图表内容
    use([DatasetComponent, CanvasRenderer, PieChart, GridComponent, TooltipComponent, LegendComponent])

    // 应用到图表的 option
    const option = computed(() => {
        return mergeTheme(props.chartConfig.option, props.themeSetting, includes)
    })
    const { vChartRef } = useChartDataFetch(props.chartConfig, useChartEditStore)

    let angle = 0; //角度，用来做简单的动画效果的

    let timerId: string | number | NodeJS.Timeout | undefined;
    function draw() {
        angle = angle + 3;
        props.chartConfig.option.series[0].renderItem=function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
            return {
                type: 'arc',
                shape: {
                    cx: api.getWidth() / 2,
                    cy: api.getHeight() / 2,
                    r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.6,
                    startAngle: ((0 + angle) * Math.PI) / 180,
                    endAngle: ((90 + angle) * Math.PI) / 180,
                },
                style: {
                    stroke: '#0CD3DB',
                    fill: 'transparent',
                    lineWidth: 1.5,
                },
                silent: true,
            };
        };
        props.chartConfig.option.series[1].renderItem=function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
            return {
                type: 'arc',
                shape: {
                    cx: api.getWidth() / 2,
                    cy: api.getHeight() / 2,
                    r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.6,
                    startAngle: ((180 + angle) * Math.PI) / 180,
                    endAngle: ((270 + angle) * Math.PI) / 180,
                },
                style: {
                    stroke: '#0CD3DB',
                    fill: 'transparent',
                    lineWidth: 1.5,
                },
                silent: true,
            };
        }
        props.chartConfig.option.series[2].renderItem=function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
            return {
                type: 'arc',
                shape: {
                    cx: api.getWidth() / 2,
                    cy: api.getHeight() / 2,
                    r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65,
                    startAngle: ((270 + -angle) * Math.PI) / 180,
                    endAngle: ((40 + -angle) * Math.PI) / 180,
                },
                style: {
                    stroke: '#0CD3DB',
                    fill: 'transparent',
                    lineWidth: 1.5,
                },
                silent: true,
            };
        }
        props.chartConfig.option.series[3].renderItem=function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
            return {
                type: 'arc',
                shape: {
                    cx: api.getWidth() / 2,
                    cy: api.getHeight() / 2,
                    r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65,
                    startAngle: ((90 + -angle) * Math.PI) / 180,
                    endAngle: ((220 + -angle) * Math.PI) / 180,
                },
                style: {
                    stroke: '#0CD3DB',
                    fill: 'transparent',
                    lineWidth: 1.5,
                },
                silent: true,
            };
        }
        props.chartConfig.option.series[4].renderItem=function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
            let x0 = api.getWidth() / 2;
            let y0 = api.getHeight() / 2;
            let r = (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65;
            let point = getCirlPoint(x0, y0, r, 90 + -angle);
            return {
                type: 'circle',
                shape: {
                    cx: point.x,
                    cy: point.y,
                    r: 4,
                },
                style: {
                    stroke: '#0CD3DB', //粉
                    fill: '#0CD3DB',
                },
                silent: true,
            };
        }
        props.chartConfig.option.series[5].renderItem=function (params: any, api: { getWidth: () => number; getHeight: () => number }) {
            let x0 = api.getWidth() / 2;
            let y0 = api.getHeight() / 2;
            let r = (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65;
            let point = getCirlPoint(x0, y0, r, 270 + -angle);
            return {
                type: 'circle',
                shape: {
                    cx: point.x,
                    cy: point.y,
                    r: 4,
                },
                style: {
                    stroke: '#0CD3DB', //绿
                    fill: '#0CD3DB',
                },
                silent: true,
            };
        }
        vChartRef.value?.setOption(props.chartConfig.option);
    }
    //获取圆上面某点的坐标(x0,y0表示坐标，r半径，angle角度)
    function getCirlPoint(x0: number, y0: number, r: number, angle: number) {
        let x1 = x0 + r * Math.cos((angle * Math.PI) / 180);
        let y1 = y0 + r * Math.sin((angle * Math.PI) / 180);
        return {
            x: x1,
            y: y1,
        };
    }
    setTimeout(function () {
        if (timerId) {
            clearInterval(timerId);
        }
        timerId = setInterval(function () {
            //用setInterval做动画感觉有问题
            draw();
        }, 100);
    }, 500);

    watch(
        () => props.chartConfig.option.dataset,
        newData => {
            props.chartConfig.option.legend.data=newData.source;
            props.chartConfig.option.series[6].data=newData.source;
            props.chartConfig.option.series[6].name=newData.name;
            vChartRef.value?.setOption(props.chartConfig.option);
        },
        {
            immediate: false,
            deep: false
        }
    )


</script>
