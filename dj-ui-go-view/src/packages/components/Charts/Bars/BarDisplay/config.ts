import {echartOptionProfixHandle, PublicConfigClass} from '@/packages/public'
import {BarDisplayConfig} from './index'
import {CreateComponentType} from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import * as echarts from 'echarts'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']


var normalcolor = new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
    offset: 0,
    color: '#ec2e85'
}, {
    offset: 1,
    color: '#ffb069'
}]);


var maxcolor = new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
    offset: 0,
    color: '#009efd'
}, {
    offset: 1,
    color: '#2af598'
}]);


const option = {
    dataset: { ...dataJson },
    timeline: {
        data: dataJson.timeline,
        bottom: '0%',
        axisType: 'category',
        show: true,
        autoPlay: true,
        playInterval: 1000,
        controlPosition: "left",
        checkpointStyle: {
            color: '#04a5f1',
            borderColor: 'rgba(4, 165, 261, .5)'
        },
        itemStyle: {
            normal: {
                color: '#04a5f1'
            },
            emphasis: {
                color: '#04a5f1'
            }
        },
        lineStyle: {
            color: '#ddd'
        },

    },


    options: [
        {
            title: {
                'text': '数值',
            },
            tooltip: {
                'trigger': 'axis'
            },
            legend: {
                "show": true,
                x: 'left',
                'data': [''],
                'selected': {
                    '': true,
                }
            },
            calculable: true,
            grid: {
                'y': 80,
                'y2': 100
            },
            xAxis: [{
                'type': 'category',
                'axisLabel': {
                    'interval': 0
                },
                'data': dataJson.datax
            }],
            yAxis: [{
                'type': 'value',
                'name': '数值',
                'max': 200
            }, {
                'type': 'value',
            }],
            series: [{
                'name': '数值',
                'yAxisIndex': 1,
                'type': 'bar',
                'data': dataJson.dataStrat,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: '#009efd'
                        }, {
                            offset: 1,
                            color: '#2af598'
                        }]),
                    },
                    emphasis: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: '#ec2e85'
                        }, {
                            offset: 1,
                            color: '#ffb069'
                        }]),
                        barBorderRadius: 4
                    },
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        formatter: '{c}',
                        fontFamily: 'Arial',
                        foontWeight: 'normal',
                        color: '#FFFFFFFF'
                    }
                },}]
        },
        dataJson.source[0],
        dataJson.source[1],
        dataJson.source[2],
        dataJson.source[3],
        dataJson.source[4],
        dataJson.source[5]



    ]
};


export default class Config extends PublicConfigClass implements CreateComponentType {
    public key = BarDisplayConfig.key
    public chartConfig = cloneDeep(BarDisplayConfig)
    // 图表配置项
    public option = echartOptionProfixHandle(option, includes)
}
