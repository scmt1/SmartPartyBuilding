import * as echarts from 'echarts'
import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { CreateComponentType } from '@/packages/index.d'
import { BarProgressConfig } from './index'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = []

export let color = ['#CE7EFE', '#7125FF']

export const option = {
    tooltip: {
        trigger: 'none'
    },
    yAxis: {
        data: ["百分比"],
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        axisLabel: {
            show: false,
        }
    },
    xAxis: {
        splitLine: {
            show: false
        },
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        axisLabel: {
            show: false
        }
    },
    legend: {
        bottom: "42%",
        textStyle: {
            color: "#1FC3CE",
            fontSize: 14
        },
    },
    dataset: dataJson,
    series: [
        {
            // name: '最上层立体圆',
            type: 'pictorialBar',
            symbolSize: [ 10,30],
            symbolOffset: [2, 0],
            z: 12,
            itemStyle: {
                normal: {
                    color: '#293CA0'
                }
            },
            data: [{
                value: 100,
                symbolPosition: 'end'
            }]
        },
        {
            // name: '中间立体圆',
            type: 'pictorialBar',
            symbolSize: [ 10,30],
            symbolOffset: [2, 0],
            z: 12,
            itemStyle: {
                normal: {
                    color: '#B687F9'
                }
            },
            data: [{
                value: dataJson.value,
                symbolPosition: 'end'
            }]
        },
        {
            // name: '最底部立体圆',
            type: 'pictorialBar',
            symbolSize: [ 10,30],
            symbolOffset: [-4, 0],
            z: 12,
            itemStyle: {
                normal: {
                    color: '#A052FE'
                }
            },
            data: [100 - dataJson.value]
        },
        {
            name: '底部立体柱',
            //底部立体柱
            stack: '1',
            type: 'bar',
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                        offset: 0,
                        color: '#CE7EFE'
                    }, {
                        offset: 1,
                        color: '#7125FF'
                    }])
                },
            },
            label: {
                normal: {
                    show: true,
                    position: 'inside',
                    offset: [-20, 0],
                    formatter: function(obj: any) {
                        return (obj.value).toLocaleString() + '%';
                    },
                    textStyle: {
                        align: 'center',
                        baseline: 'middle',
                        fontSize: 14,
                        fontWeight: '400',
                        color: '#fff'
                    }
                },
            },

            silent: true,
            barWidth: 30,
            barGap: '-100%', // Make series be overlap
            data: [dataJson.value]
        },
        {
            name: '上部立体柱',
            //上部立体柱
            stack: '1',
            type: 'bar',
            itemStyle: {
                normal: {
                    color: '#14257B',
                    opacity: .7
                }
            },
            silent: true, //是否不响应和触发鼠标
            barWidth: 30,
            barGap: '-100%', // Make series be overlap
            data: [100 - dataJson.value],

            label: {
                normal: {
                    show: true,
                    position: 'inside',
                    offset: [-20, 0],
                    formatter: function(obj: any) {
                        return (obj.value).toLocaleString() + '%';
                    },
                    textStyle: {
                        align: 'center',
                        baseline: 'middle',
                        fontSize: 14,
                        fontWeight: '400',
                        color: '#fff'
                    }
                }
            }
        }
    ]
}

export default class Config extends PublicConfigClass implements CreateComponentType
{
    public key = BarProgressConfig.key
    public chartConfig = cloneDeep(BarProgressConfig)
    public option = echartOptionProfixHandle(option, includes)
}
