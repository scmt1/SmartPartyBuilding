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
    let option = computed(() => {
        return mergeTheme(props.chartConfig.option, props.themeSetting, includes)
    })
    const { vChartRef } = useChartDataFetch(props.chartConfig, useChartEditStore)

    watch(
        () => props.chartConfig.option.dataset,
        newData => {
            console.log(newData)
            props.chartConfig.option.dataset = newData;
            vChartRef.value?.setOption(props.chartConfig.option);
        },
        {
            immediate: false,
            deep: false
        }
    )
    const radius = ['30%', '70%'];
    const defaultPalette = [
        '#5470c6',
        '#91cc75',
        '#fac858',
        '#ee6666',
        '#73c0de',
        '#3ba272',
        '#fc8452',
        '#9a60b4',
        '#ea7ccc'
    ];
    let parliamentOption = (function() {
        let sum = props.chartConfig.option.dataset.reduce(function(sum: any, cur: { value: any }) {
            return sum + cur.value;
        }, 0);
        let angles: number[] = [];
        let startAngle = -Math.PI / 2;
        let curAngle = startAngle;
        props.chartConfig.option.dataset.forEach(function(item: { value: number }) {
            angles.push(curAngle);
            curAngle += (item.value / sum) * Math.PI * 2;
        });
        angles.push(startAngle + Math.PI * 2);

        function parliamentLayout(startAngle: number, endAngle: number, totalAngle: number, r0: number, r1: number, size: number) {
            let rowsCount = Math.ceil((r1 - r0) / size);
            let points = [];
            let r = r0;
            for (let i = 0; i < rowsCount; i++) {
                // Recalculate size
                let totalRingSeatsNumber = Math.round((totalAngle * r) / size);
                let newSize = (totalAngle * r) / totalRingSeatsNumber;
                for (
                    let k = Math.floor((startAngle * r) / newSize) * newSize; k < Math.floor((endAngle * r) / newSize) * newSize - 1e-6; k += newSize
                ) {
                    let angle = k / r;
                    let x = Math.cos(angle) * r;
                    let y = Math.sin(angle) * r;
                    points.push([x, y]);
                }
                r += size;
            }
            return points;
        }
        return {
            grid: {
                top: '15%',
                left: 0,
                right: '1%',
                bottom: 5,
            },
            tooltip : {
                trigger: 'item',
                formatter:"{b} : ({d}%)"
            },
            legend: {
                orient: 'vertical',
                top: 'center',
                icon:"circle",
                right: 180,
                textStyle:{
                    color:'#ffffff',
                    fontSize:16
                },
            },
            series: {
                type: 'custom',
                id: 'distribution',
                data: props.chartConfig.option.dataset,
                coordinateSystem: undefined,
                universalTransition: true,
                animationDurationUpdate: 1000,
                renderItem: function(params: { dataIndex: any }, api: { getWidth: () => number; getHeight: () => number }) {
                    var idx = params.dataIndex;
                    var viewSize = Math.min(api.getWidth(), api.getHeight());
                    var r0 = ((parseFloat(radius[0]) / 100) * viewSize) / 2;
                    var r1 = ((parseFloat(radius[1]) / 100) * viewSize) / 2;
                    var cx = api.getWidth() * 0.5;
                    var cy = api.getHeight() * 0.5;
                    var size = viewSize / 50;
                    var points = parliamentLayout(
                        angles[idx],
                        angles[idx + 1],
                        Math.PI * 2,
                        r0,
                        r1,
                        size + 3
                    );
                    return {
                        type: 'group',
                        children: points.map(function(pt) {
                            return {
                                type: 'circle',
                                autoBatch: true,
                                shape: {
                                    cx: cx + pt[0],
                                    cy: cy + pt[1],
                                    r: size / 2
                                },
                                style: {
                                    fill: defaultPalette[idx % defaultPalette.length]
                                }
                            };
                        })
                    };
                }
            }
        };
    })();
    let currentOption = props.chartConfig.option;
    const onRestart = async () => {
        setInterval(function() {
            currentOption = currentOption === props.chartConfig.option ? parliamentOption : props.chartConfig.option;
            vChartRef.value?.setOption(currentOption);
        }, 3000);
    }
    onRestart()
</script>
