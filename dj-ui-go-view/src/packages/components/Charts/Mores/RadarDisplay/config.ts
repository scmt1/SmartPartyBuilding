import {echartOptionProfixHandle, PublicConfigClass} from '@/packages/public'
import {RadarDisplayConfig} from './index'
import {CreateComponentType} from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend']

// 雷达形状
export const RadarShapeEnumList = [
    {label: '多边形', value: 'polygon'},
    {label: '圆形', value: 'circle'}
]

export const option = {
    dataset: dataJson,
    baseOption: {

        timeline: {
            axisType: 'category',
            autoPlay: true,
            show: true,
            playInterval: 2000,
            data: dataJson.timelineData,
            controlPosition: 'left',
            bottom: 0
        },
        title: {
            top: 30,
            right: 20,
            textStyle: {
                color: '#eee'
            }
        },
        legend: {
            orient: 'vertical',
            symbol: '',
            top: 65,
            left:65,
            right: 15,
            data: dataJson.legendData,
            itemGap: 20,
            textStyle: {
                color: '#fff',
                fontSize: 12
            },

        },
        radar: {
            startAngle: 60,
            indicator: [
                {
                    name: '运算求解',
                    max: 100
                },
                {
                    name: '推理运算',
                    max: 100
                },
                {
                    name: '数学建模',
                    max: 100
                },
                {
                    name: '空间想象',
                    max: 100
                },
                {
                    name: '数据处理',
                    max: 100
                },
                {
                    name: '数学表达',
                    max: 100
                }
            ],
            shape: 'polygon',
            splitNumber: 5,
            name: {
                textStyle: {
                    fontFamily: 'sc',
                    fontSize: 14,
                    color: '#fff'
                },
            },

            splitLine: {
                lineStyle: {
                    color: 'rgba(109, 205, 230, 0.5)'
                }
            },
            splitArea: {
                show: false
            },
            axisLine: {
                lineStyle: {
                    color: 'rgba(238, 197, 102, 0.5)'
                }
            }
        },
        series: [
            {
            name: '个人',
            type: 'radar',
            itemStyle: {
                color: '#6DCDE6'
            },
            areaStyle: {
                opacity: .4,
                color: {
                    type: 'radial',
                    x: 0.5,
                    y: 0.5,
                    r: 0.5,
                    colorStops: [{
                        offset: 0,
                        color: '#38A0D6' // 0% 处的颜色
                    }, {
                        offset: 1,
                        color: '#61C3E2' // 100% 处的颜色
                    }],
                    global: false // 缺省为 false
                }
            }
        },
            {
                name: '班级',
                type: 'radar',
                itemStyle: {
                    color: '#FFA53C'
                },
                areaStyle: {
                    opacity: .4,
                    color: {
                        type: 'radial',
                        x: 0.5,
                        y: 0.5,
                        r: 0.5,
                        colorStops: [{
                            offset: 0,
                            color: '#FF457E' // 0% 处的颜色
                        }, {
                            offset: 1,
                            color: '#FFA53C' // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    }
                }
            },
            {
                name: '年级',
                type: 'radar',
                itemStyle: {
                    color: '#E23AF5'
                },
                areaStyle: {
                    opacity: .4,
                    color: {
                        type: 'radial',
                        x: 0.5,
                        y: 0.5,
                        r: 0.5,
                        colorStops: [{
                            offset: 0,
                            color: '#BB42EF' // 0% 处的颜色
                        }, {
                            offset: 1,
                            color: '#E23AF5' // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    }
                }
            }]
    },
    options:dataJson.optionsData
};

export default class Config extends PublicConfigClass implements CreateComponentType {
    public key = RadarDisplayConfig.key
    public chartConfig = cloneDeep(RadarDisplayConfig)
    // 图表配置项
    public option = echartOptionProfixHandle(option, includes)
}
