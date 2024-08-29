import {echartOptionProfixHandle, PublicConfigClass} from '@/packages/public'
import {BarTrendsAddConfig} from './index'
import {CreateComponentType} from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import dataJson from './data.json'

export const includes = ['legend', 'xAxis', 'yAxis', 'grid']


export const option = {
    sort: true,
  dataset: dataJson,
    title: {
        text: ''
    },
    xAxis: {},
    yAxis: {
        type: 'category',
        // dataset:dataJson
    },
    series: [{
        barWidth: 10,
        type: 'bar',
        label: {
            show: true,
            position: 'right',
            fontSize: 12,
            color: "#FFFFFFFF",
        },
        itemStyle: {
            normal: {
                barBorderRadius: 14,
                color: "#04c5e7"
            },
        },
        data: []
    }]
};

export default class Config extends PublicConfigClass implements CreateComponentType {
    public key = BarTrendsAddConfig.key
    public chartConfig = cloneDeep(BarTrendsAddConfig)
    // 图表配置项
    public option = echartOptionProfixHandle(option, includes)
}
